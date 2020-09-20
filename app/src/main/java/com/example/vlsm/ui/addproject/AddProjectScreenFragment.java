package com.example.vlsm.ui.addproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
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

import com.example.vlsm.R;
import com.example.vlsm.alb.Adapter;
import com.example.vlsm.binding.ProjectListAdapter;
import com.example.vlsm.binding.SubRedListAdapter;
import com.example.vlsm.ui.home.HomeFragment;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProjectScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectScreenFragment extends Fragment implements SubRedListAdapter.SubRedViewHolder.ClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SubRedListAdapter adapterSubRedesProject;
    private ActionModeCallback actionModeCallback = new AddProjectScreenFragment.ActionModeCallback();
    private ActionMode actionMode;

    public AddProjectScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProjectScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProjectScreenFragment newInstance(String param1, String param2) {
        AddProjectScreenFragment fragment = new AddProjectScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_project_screen, container, false);

        RecyclerView recyclerView =  root.findViewById(R.id.recycler_subredlist_fromProject);
        /*recyclerView.setAdapter(new Adapter(null));*/
        this.adapterSubRedesProject = new SubRedListAdapter(this);
        recyclerView.setAdapter(adapterSubRedesProject);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
 /*       DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);*/
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