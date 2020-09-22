package com.example.vlsm.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.vlsm.MainActivity;
import com.example.vlsm.R;
import com.example.vlsm.binding.ProjectListAdapter;
import com.example.vlsm.binding.SubRedListAdapter;
import com.example.vlsm.calculate.IP;
import com.example.vlsm.data.model.Project;
import com.example.vlsm.data.model.SubRed;
import com.example.vlsm.ui.addproject.AddProjectViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements ProjectListAdapter.ProjectViewHolder.ClickListener {

    private HomeViewModel homeViewModel;

    private ProjectListAdapter projectListAdapter;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private AddProjectViewModel addProjectViewModel;
    private int editing = -1;
    private FirebaseFirestore db;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        projectListAdapter = new ProjectListAdapter(this);
        this.addProjectViewModel = new ViewModelProvider(requireActivity()).get(AddProjectViewModel.class);
    }
    FloatingActionButton fabAdd;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        fabAdd = root.findViewById(R.id.fab_addProject);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_addProjectScreenFragment);


                /*Toast.makeText(getContext(),"asdasd",Toast.LENGTH_LONG).show();*/
            }
        });

        RecyclerView recyclerView =  root.findViewById(R.id.recycler_projec_list);
        /*recyclerView.setAdapter(new Adapter(null));*/

        recyclerView.setAdapter(projectListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        this.db = FirebaseFirestore.getInstance();
        db.collection("data").document(MainActivity.username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Map<String, Object> projectsUser = documentSnapshot.getData();
                            Map<String, Object> projects = (Map<String, Object>) projectsUser.get("projects");

                            ArrayList<Project> projectsFetch = new ArrayList<>();




                            for (String key : projects.keySet()) {/*0,1,2,3...n*/

                                HashMap<String,Object> projectMap = (HashMap<String, Object>) projects.get(key);
                                String dateCreated = (String) projectMap.get("dateCreated");
                                int maskProject = Integer.parseInt((String) projectMap.get("mask"));
                                String projectName = (String) projectMap.get("projectName");
                                String projectIP = (String) projectMap.get("startIP");

                                Project project = new Project(projectName,new IP(projectIP),maskProject,dateCreated);

                                HashMap<String,Object> projectSubReds = (HashMap<String, Object>) projectMap.get("subreds");
                                for(String keySubRed:projectSubReds.keySet()){/*0,1,2...*/
                                    HashMap<String,Object> subRedMap = (HashMap<String, Object>) projectSubReds.get(keySubRed);

                                    try {
                                        int nodesAmount = Integer.parseInt((long) subRedMap.get("nodesAmount")+"");

                                        project.addSubRed(nodesAmount , (String) subRedMap.get("description"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                projectsFetch.add(project);

                            }
                            projectListAdapter.setItems(projectsFetch);

                            /*for(int projectIndex = 0; projectIndex<projects.size();projectIndex++){
                                projects.get
                            }*/


                            /*projectListAdapter.setItems(arrayList);*/


                        }else{/*if doesnt exist projecs for that user, then*/
                            projectListAdapter.setItems(new ArrayList<Project>());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Can't get the data from the server")
                                .setTitle("Please try later.")
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                        builder.create().show();
                    }
                })
        ;

        this.addProjectViewModel.getProject().observe(getViewLifecycleOwner(), new Observer<Project>() {
            @Override
            public void onChanged(Project project) {
                if(project!=null){
                    if(project.CURRENT_STATE == Project.STATE_EDIT_FINISH){/*existing modified*/
                        projectListAdapter.notifyItemChanged(editing);
                        saveProjectsChange();
                    }else
                    if(project.CURRENT_STATE == Project.STATE_EDITING){/*existing modified*/
                        /*ignore here*/
                    }else
                    {/*new created*/
                        projectListAdapter.addItem(project);
                        saveProjectsChange(/*project*/);
                        addProjectViewModel.getProject().setValue(null);/*Clean the shared element*/
                    }

                }
            }
        });
        return root;
    }
    public void saveProjectsChange(/*Project project*/){
        Map<String,Object> mapProjects = new HashMap<>();
        /* String list = new Gson().toJson(projectListAdapter.getItems());*/
        for(int amountProjects = 0; amountProjects<projectListAdapter.getItems().size();amountProjects++){
            Project projectTemp = projectListAdapter.getItem(amountProjects);

            Map<String,Object> mapProjec = new HashMap<>();
            mapProjec.put("projectName",projectTemp.getProjectName());
            mapProjec.put("startIP",projectTemp.getIpProject().getIp());
            mapProjec.put("mask",projectTemp.getMask()+"");
            mapProjec.put("dateCreated",projectTemp.getDateTimeCreation());

            Map<String,Object> mapSubreds = new HashMap<>();
            for(int amountNodes = 0;amountNodes<projectTemp.getListNodos().size();amountNodes++){
                Map<String,Object> mapSubred = new HashMap<>();
                mapSubred.put("description", projectTemp.getListNodos().get(amountNodes).getSubredDescriptcion());
                mapSubred.put("nodesAmount", projectTemp.getListNodos().get(amountNodes).getNodesAmount());

                mapSubreds.put(amountNodes+"",mapSubred);
            }

            mapProjec.put("subreds",mapSubreds);
            mapProjects.put(amountProjects+"",mapProjec);/*index mejor*/
        }
                   /*     for(Project projectTemp : projectListAdapter.getItems()){

                            mapProjects.put("projectName",mapProjec);*//*index mejor*//*
                        }
*/


        Map<String,Object> projects = new HashMap<>();
        projects.put("projects",mapProjects);
        db.collection("data").document(MainActivity.username)
                .set(projects);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        }else{
            Project projectToEdit = projectListAdapter.getItem(position);
            projectToEdit.CURRENT_STATE = Project.STATE_EDITING;
            addProjectViewModel.getProject().setValue(projectToEdit);
            Navigation.findNavController(fabAdd).navigate(R.id.action_nav_home_to_addProjectScreenFragment);
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
    /**
     * Toggle the selection state of an item.
     *
     * If the item was the last one in the selection and is unselected, the
     * selection is stopped.
     * Note that the selection must already be started (actionMode must not be
     * null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        projectListAdapter.toggleSelection(position);
        int count = projectListAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " Project (s) ");
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

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
                    projectListAdapter.removeItems(projectListAdapter.getSelectedItems());
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
            projectListAdapter.clearSelection();
            actionMode = null;
        }
    }
}