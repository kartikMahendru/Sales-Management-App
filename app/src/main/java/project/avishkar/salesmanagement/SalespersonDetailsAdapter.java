package project.avishkar.salesmanagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 9/22/18.
 */

public class SalespersonDetailsAdapter extends RecyclerView.Adapter<SalespersonDetailsAdapter.MyViewHolder>{

    private ArrayList<SalesPerson> list;
    private Context context;
    public SalespersonDetailsAdapter()
    {

    }

    public SalespersonDetailsAdapter(Context context, ArrayList<SalesPerson> list)
    {
        this.context=context;
        this.list=list;
    }

    @Override
    public SalespersonDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_item,parent,false);
        return new SalespersonDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.userName.setText(list.get(position).getName());
        holder.soldItems.setText(String.valueOf(list.get(position).getNumber()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView userName,soldItems;

        public MyViewHolder(View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_name);
            soldItems=itemView.findViewById(R.id.soldText);
        }
    }
}
