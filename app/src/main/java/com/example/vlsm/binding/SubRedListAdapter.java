package com.example.vlsm.binding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vlsm.R;
import com.example.vlsm.data.model.SubRed;

import java.util.ArrayList;

public class SubRedListAdapter  extends RecyclerView.Adapter<SubRedListAdapter.SubRedViewHolder>{

    @SuppressWarnings("unused")
    private static final String TAG = SubRedListAdapter.class.getSimpleName();

    private static final int ITEM_COUNT = 25;
    private ArrayList<SubRed> items;

    public SubRedListAdapter() {
        super();

        // Create some items
        items = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT; ++i) {
            items.add(new SubRed(2,"Vic DEsc" + i,"start ip","end ip","1111"));
        }
    }

    @NonNull
    @Override
    public SubRedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_subred_item, parent, false);
        return new SubRedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubRedViewHolder holder, int position) {
        SubRed subRedItem = items.get(position);

        holder.txt_sum_nodos_subred.setText(subRedItem.getNodesAmount() + " nodes");
        holder.txt_subredDescription.setText(subRedItem.getSubredDescriptcion());
        holder.txt_subredStartIP.setText(subRedItem.getSubredStartIP());
        holder.txt_subredFinaltIP.setText(subRedItem.getSubredEndIP());
        holder.txtSubredMaskInfo.setText(subRedItem.getSubredStartMask());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

 /*   @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }*/

    public static class SubRedViewHolder extends RecyclerView.ViewHolder {
        TextView txt_sum_nodos_subred;
        TextView txt_subredDescription;
        TextView txt_subredStartIP;
        TextView txt_subredFinaltIP;
        TextView txtSubredMaskInfo;

        public SubRedViewHolder(View itemView) {
            super(itemView);

            txt_sum_nodos_subred =   itemView.findViewById(R.id.txt_number_nodos_subred);
            txt_subredDescription =  itemView.findViewById(R.id.txt_subred_description);

            txt_subredStartIP = itemView.findViewById(R.id.txt_subred_start_ip);
            txt_subredFinaltIP = itemView.findViewById(R.id.txt_subred_final_ip);
            txtSubredMaskInfo = itemView.findViewById(R.id.txt_subred_mask_info);

        }
    }

}
