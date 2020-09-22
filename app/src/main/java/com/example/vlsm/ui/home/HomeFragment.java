package com.example.vlsm.ui.home;

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

import com.example.vlsm.R;
import com.example.vlsm.binding.ProjectListAdapter;
import com.example.vlsm.binding.SubRedListAdapter;
import com.example.vlsm.data.model.Project;
import com.example.vlsm.ui.addproject.AddProjectViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment implements ProjectListAdapter.ProjectViewHolder.ClickListener {

    private HomeViewModel homeViewModel;

    private ProjectListAdapter projectListAdapter;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private AddProjectViewModel addProjectViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        projectListAdapter = new ProjectListAdapter(this);
        this.addProjectViewModel = new ViewModelProvider(requireActivity()).get(AddProjectViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fabAdd = root.findViewById(R.id.fab_addProject);
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
       /* homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        this.addProjectViewModel.getProject().observe(getViewLifecycleOwner(), new Observer<Project>() {
            @Override
            public void onChanged(Project project) {
                if(project!=null){
                    projectListAdapter.addItem(project);
                    addProjectViewModel.getProject().setValue(null);/*Clean the shared element*/
                }
            }
        });
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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