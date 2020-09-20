package com.example.vlsm.binding;

import android.util.Log;
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

public class ProjectListAdapter extends SelectableAdapter<ProjectListAdapter.ProjectViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = ProjectListAdapter.class.getSimpleName();

    private static final int ITEM_COUNT = 25;
    private ArrayList<Project> items;

    ProjectViewHolder.ClickListener clickListener;

    public ProjectListAdapter(ProjectViewHolder.ClickListener clickListener) {
        super();
        this.clickListener = clickListener;

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
        return new ProjectViewHolder(v,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project projectItem = items.get(position);

        holder.txt_project_name.setText(projectItem.getProjectName() + " nodes");
        holder.txt_date_project_creation.setText(projectItem.getDateTimeCreation());

        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

 /*   @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }*/

    public static class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView txt_project_name;
        TextView txt_date_project_creation;
        View selectedOverlay;
        private ClickListener listener;

        public ProjectViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            txt_project_name =   itemView.findViewById(R.id.txt_NameProject);
            txt_date_project_creation =  itemView.findViewById(R.id.txt_DateProject);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            this.listener = listener;


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getPosition());
            }

            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
        }

    }

}
