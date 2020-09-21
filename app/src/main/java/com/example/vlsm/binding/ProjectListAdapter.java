package com.example.vlsm.binding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vlsm.R;
import com.example.vlsm.calculate.IP;
import com.example.vlsm.data.model.Project;
import com.example.vlsm.data.model.SubRed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectListAdapter extends SelectableAdapter<ProjectListAdapter.ProjectViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = ProjectListAdapter.class.getSimpleName();

    private static final int ITEM_COUNT = 2;
    private ArrayList<Project> items;

    ProjectViewHolder.ClickListener clickListener;

    public ProjectListAdapter(ProjectViewHolder.ClickListener clickListener) {

        this(clickListener,new ArrayList<Project>());
    }

    public ProjectListAdapter(ProjectViewHolder.ClickListener clickListener,ArrayList<Project> items) {
        super();
        this.clickListener = clickListener;

        // Create some items
        this.items = items;
       /* for (int i = 0; i < ITEM_COUNT; ++i) {
            items.add(new Project("vic Project",new IP("192.168.168.12"),24));
        }*/
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
    public void addItem(Project item){
        items.add( 0,item);
        //notifyItemInserted(items.size()-1);
        notifyItemInserted(0);

    }
 /*   @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }*/
    public void removeItem(int position) {
     items.remove(position);
     notifyItemRemoved(position);

    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            items.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    public void removeItems(List<Integer> positions) {
         // Reverse-sort the list
         Collections.sort(positions, new Comparator<Integer>() {
             @Override
             public int compare(Integer lhs, Integer rhs) {
                 return rhs - lhs;
             }
         });//los ordena de mayor a menor, (arriba a abajo), para que no se altere la position, o sea, si se borra el 1, entonces el 2, sería el nuevo 1
         //en cambio si se borra el 6 primero, no afectaria.

         // Split the list in ranges
         while (!positions.isEmpty()) {
             if (positions.size() == 1) {
                 removeItem(positions.get(0));
                 positions.remove(0);
             } else {
                 int count = 1;
                 while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                     ++count;
                 }//creo es para detectar un removeRange
    //posicion count, es igual a posicion count - 1
                 if (count == 1) {//si la cuenta es 1, elimina solo ese item//si no, llama al removeRange
                     removeItem(positions.get(0));
                 } else {
                     removeRange(positions.get(count - 1), count);
                 }

                 for (int i = 0; i < count; ++i) {
                     positions.remove(0);//remueve el (los) elementos de la lista posiciones.Siempre remueve 0, o sea, el top.
                 }
             }
         }
     }



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
