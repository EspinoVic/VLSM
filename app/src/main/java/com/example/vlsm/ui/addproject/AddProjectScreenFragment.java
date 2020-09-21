package com.example.vlsm.ui.addproject;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.vlsm.R;
import com.example.vlsm.alb.Adapter;
import com.example.vlsm.binding.ProjectListAdapter;
import com.example.vlsm.binding.SubRedListAdapter;
import com.example.vlsm.calculate.IP;
import com.example.vlsm.data.model.Project;
import com.example.vlsm.data.model.SubRed;
import com.example.vlsm.ui.home.HomeFragment;
import com.example.vlsm.ui.home.HomeViewModel;

import java.util.ArrayList;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProjectScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectScreenFragment extends Fragment implements SubRedListAdapter.SubRedViewHolder.ClickListener {

    private AddProjectViewModel addProjectViewModel;
    private SubRedListAdapter adapterSubRedesProject;
    private ActionModeCallback actionModeCallback = new AddProjectScreenFragment.ActionModeCallback();
    private ActionMode actionMode;



    public AddProjectScreenFragment() {
        // Required empty public constructor
    }


    public static AddProjectScreenFragment newInstance(String param1, String param2) {
        AddProjectScreenFragment fragment = new AddProjectScreenFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private EditText txt_NameProject ;
    private EditText txt_startIP ;
    private EditText startMask ;
    private EditText txt_numGroupNodes ;
    private EditText descriptionGroup ;
    private Button btn_addSubRed ;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_add_project_screen, container, false);

        /*addProjectViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);*/
        addProjectViewModel = new ViewModelProvider(this).get(AddProjectViewModel.class);

         this.txt_NameProject = root.findViewById(R.id.txt_NameProject);
         this.txt_startIP = root.findViewById(R.id.txt_startIP);
         this.startMask = root.findViewById(R.id.startMask);
         this.txt_numGroupNodes = root.findViewById(R.id.txt_numGroupNodes);
         this.descriptionGroup = root.findViewById(R.id.descriptionGroup);
         this.btn_addSubRed = root.findViewById(R.id.btn_addSubRed);

        recyclerView =  root.findViewById(R.id.recycler_subredlist_fromProject);
        /*recyclerView.setAdapter(new Adapter(null));*/
        this.adapterSubRedesProject = new SubRedListAdapter(this);
        recyclerView.setAdapter(adapterSubRedesProject);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        /*
        Before calling this fragment, the view model is changed
        null when new project
        not null when project exists
        */
        addProjectViewModel.getProject().observe(getViewLifecycleOwner(), new Observer<Project>() {
            @Override
            public void onChanged(Project project) {
                if(project!=null){
                    txt_NameProject.setText(project.getProjectName());
                    txt_startIP.setText(project.getIpProject().toString());
                    startMask.setText(project.getMask()+"");

                    adapterSubRedesProject.setItems(project.getListNodos());
                }

            }
        });

       /*AA addProjectViewModel.getProjectNodes().observe(getViewLifecycleOwner(), new Observer<ArrayList<SubRed>>() {
            @Override
            public void onChanged(ArrayList<SubRed> subReds) {
                if(subReds!=null){
                    adapterSubRedesProject.setItems(subReds);
                }else{
                    adapterSubRedesProject.setItems(new ArrayList<SubRed>());*//*empty*//*

                }
            }
        });*/

        /**/
/*        addProjectViewModel.getCurrentNodeToAdd().observe(getViewLifecycleOwner(), new Observer<SubRed>() {
            @Override
            public void onChanged(SubRed subRed) {
                *//*It'll be null only the first time, when the viewModel is created*//*
                if(subRed == null){
                    return;
                }else{
                    adapterSubRedesProject.addItem(subRed);
                }
            }
        });*/

        btn_addSubRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateNewNode()){

                    try {
                        Project project = addProjectViewModel.getProject().getValue();
                        if(project == null){
                            addProjectViewModel.setProject(
                                    new Project(
                                            txt_NameProject.getText().toString(),
                                            new IP(txt_startIP.getText().toString()),
                                            Integer.parseInt(startMask.getText().toString())
                                    )
                            );
                        }
                        project =  addProjectViewModel.getProject().getValue();
                        ArrayList<SubRed> subReds = project.addSubRed(
                                Integer.parseInt(String.valueOf(txt_numGroupNodes.getText())),
                                descriptionGroup.getText().toString()
                        );

                        adapterSubRedesProject.notifyDataSetChanged();

                       /* addProjectViewModel.setProjectNodeList(null);
                        addProjectViewModel.setProjectNodeList(subReds);*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return  root;
    }



    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            /*actionMode = startSupportActionMode(actionModeCallback);*/
            actionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(this.actionModeCallback);

        }

        toggleSelection(position);

        return true;
    }


    private void toggleSelection(int position) {
        adapterSubRedesProject.toggleSelection(position);
        int count = adapterSubRedesProject.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " Project (s) ");
            actionMode.invalidate();
        }
    }


    boolean validateNewNode(){

        if(txt_NameProject.getText().toString().equals("")){
            txt_NameProject.setError("Field required.");
        }
        if(txt_startIP.getText().toString().equals("")){

            txt_startIP.setError("Insert a IP.");
        }
        IP startIP;
        try{
             startIP = new IP(txt_startIP.getText().toString());
        }catch (Exception exce){
            txt_startIP.setError("Insert a valid IP.");
            return false;
        }

        if(startMask.getText().toString().isEmpty()){
            startMask.setText(Project.calculateDefaultMask(startIP)+"");
        }


        int numNodes = 0;
        try{
            numNodes = Integer.parseInt(String.valueOf(this.txt_numGroupNodes.getText()));

        }catch (Exception ex){
            this.txt_numGroupNodes.setError("Only integer numbers");
            return false;
        }
        if(String.valueOf(descriptionGroup.getText()).length()<4){
            this.descriptionGroup.setError("Description name length should be more than 4 chars");
            return false;
        }
        return true;
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = AddProjectScreenFragment.ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.selection_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    // TODO: actually remove items
                    adapterSubRedesProject.removeItems(adapterSubRedesProject.getSelectedItems());
                    mode.finish();
                    Log.d(TAG, "menu_remove");
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapterSubRedesProject.clearSelection();
            actionMode = null;
        }
    }
}