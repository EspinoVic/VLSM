package com.example.vlsm.binding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vlsm.R;
import com.example.vlsm.data.model.Project;
import com.example.vlsm.data.model.SubRed;

import java.util.ArrayList;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>{

    @SuppressWarnings("unused")
    private static final String TAG = ProjectListAdapter.class.getSimpleName();

    private static final int ITEM_COUNT = 25;
    private ArrayList<Project> items;

    public ProjectListAdapter() {
        super();

        // Create some items
        items = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; ++i) {
            items.add(new Project("vic Project","192.168.1651.12"));
        }
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item_lista, parent, false);
        return new ProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project projectItem = items.get(position);

        holder.txt_project_name.setText(projectItem.getProjectName() + " nodes");
        holder.txt_date_project_creation.setText(projectItem.getDateTimeCreation());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

 /*   @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }*/

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView txt_project_name;
        TextView txt_date_project_creation;


        public ProjectViewHolder(View itemView) {
            super(itemView);

            txt_project_name =   itemView.findViewById(R.id.txt_NameProject);
            txt_date_project_creation =  itemView.findViewById(R.id.txt_DateProject);



        }
    }

}
