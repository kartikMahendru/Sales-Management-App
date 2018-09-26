package project.avishkar.salesmanagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mehul Garg on 22-09-2018.
 */

public class SalespersonInventoryAdapter extends RecyclerView.Adapter<SalespersonInventoryAdapter.MyViewHolder> {

    private ArrayList <InventoryItem> list;
    private Context context;

    public SalespersonInventoryAdapter(){

    }

    public SalespersonInventoryAdapter(Context context,ArrayList<InventoryItem> list){
        this.list=list;
        this.context=context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item_salesperson,parent,false);

        return new SalespersonInventoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.itemName.setText(list.get(position).getItemName());
        holder.sold.setText(String.valueOf(list.get(position).getSold()));
        holder.progressBar.setProgress(list.get(position).getSold());
        holder.remaining.setText(String.valueOf(list.get(position).getTotal_available()));
        holder.profitText.setText(String.valueOf(list.get(position).getSold() * list.get(position).getProfit()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView itemName, sold, remaining, profitText;
        private ImageView edit;
        private ProgressBar progressBar;

        public MyViewHolder(View itemView){
            super(itemView);
            itemName=itemView.findViewById(R.id.item_name);
            sold=itemView.findViewById(R.id.items_sold);
            progressBar=itemView.findViewById(R.id.progressBar2);
            remaining=itemView.findViewById(R.id.remaining_items_text);
            profitText=itemView.findViewById(R.id.profitText);
        }
    }
}
