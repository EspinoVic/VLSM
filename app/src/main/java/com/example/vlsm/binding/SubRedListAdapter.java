package com.example.vlsm.binding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vlsm.R;
import com.example.vlsm.calculate.IP;
import com.example.vlsm.data.model.SubRed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SubRedListAdapter extends SelectableAdapter<SubRedListAdapter.SubRedViewHolder>{

    @SuppressWarnings("unused")
    private static final String TAG = SubRedListAdapter.class.getSimpleName();

    private ArrayList<SubRed> items;
    SubRedViewHolder.ClickListener clickListener;

    public SubRedListAdapter(SubRedViewHolder.ClickListener clickListener) {
        this(clickListener,new ArrayList<SubRed>());

    }
    public SubRedListAdapter(SubRedViewHolder.ClickListener clickListener,ArrayList<SubRed> items) {
        super();
        this.clickListener = clickListener;
        // Create some items
        this.items = items;
        /*for (int i = 0; i < ITEM_COUNT; ++i) {
            try {
                items.add(new SubRed(2,"Vic DEsc" + i,new IP("192.168.1.0")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
       /* try {
            items.add(new SubRed(70,"Vic DEsc" + 70,new IP("192.168.2.0")));
            items.add(new SubRed(20,"Vic DEsc" + 20,new IP("192.168.2.0")));
            items.add(new SubRed(10,"Vic DEsc" + 10,new IP("192.168.2.0")));
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    public void setItems(ArrayList<SubRed> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubRedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_subred_item, parent, false);
        return new SubRedViewHolder(v,this.clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubRedViewHolder holder, int position) {
        SubRed subRedItem = items.get(position);

        holder.txt_sum_nodos_subred.setText( subRedItem.getSubredSize()+" for " + subRedItem.getNodesAmount() + " nodes");
        holder.txt_subredDescription.setText(subRedItem.getSubredDescriptcion());

        String text ="Start IP: " + subRedItem.getSubredStartIP().toString();
        holder.txt_subredStartIP.setText(text);

        text ="End IP: " +subRedItem.getSubredEndIP().toString();
        holder.txt_subredFinaltIP.setText(text);

        text ="Mask: " +subRedItem.getSubredMask();
        holder.txtSubredMaskInfo.setText(text);

        holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SubRed item){
        items.add( 0,item);
        //notifyItemInserted(items.size()-1);
        notifyItemInserted(0);

    }
    public void addItem(SubRed item,int indexInsertion){
        items.add( indexInsertion,item);
        //notifyItemInserted(items.size()-1);
        notifyItemInserted(indexInsertion);
    }
    public void addItem(SubRed item,boolean end){
        int indexInsert = end?items.size():0;
        items.add( indexInsert,item);
        notifyItemInserted(indexInsert);

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

    public static class SubRedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView txt_sum_nodos_subred;
        TextView txt_subredDescription;
        TextView txt_subredStartIP;
        TextView txt_subredFinaltIP;
        TextView txtSubredMaskInfo;
        View selectedOverlay;
        private final ClickListener listener;

        public SubRedViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);

            txt_sum_nodos_subred =   itemView.findViewById(R.id.txt_number_nodos_subred);
            txt_subredDescription =  itemView.findViewById(R.id.txt_subred_description);

            txt_subredStartIP = itemView.findViewById(R.id.txt_subred_start_ip);
            txt_subredFinaltIP = itemView.findViewById(R.id.txt_subred_final_ip);
            txtSubredMaskInfo = itemView.findViewById(R.id.txt_subred_mask_info);

            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            this.listener = clickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }

            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
        }
    }

}
