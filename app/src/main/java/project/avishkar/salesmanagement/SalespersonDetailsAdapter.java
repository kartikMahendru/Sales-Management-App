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
 * Created by user on 9/22/18.
 */

public class SalespersonDetailsAdapter extends RecyclerView.Adapter<SalespersonDetailsAdapter.MyViewHolder>{

    private ArrayList<String> sold, profit;
    private ArrayList<SalesPerson> names;
    private Context context;

    public SalespersonDetailsAdapter()
    {

    }

    public SalespersonDetailsAdapter(Context context, ArrayList<SalesPerson> names, ArrayList<String> sold, ArrayList<String> profit)
    {
        this.context=context;
        this.names=names;
        this.sold=sold;
        this.profit=profit;
    }

    @Override
    public SalespersonDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_item,parent,false);
        return new SalespersonDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        for(int i=0;i<100;i++)
            System.out.println(position);

        holder.userName.setText(names.get(position).getName());
        holder.soldItems.setText(sold.get(position));
        holder.profitText.setText(String.valueOf(Integer.parseInt(sold.get(position)) * Integer.parseInt(profit.get(position))));
        holder.spinnerImage.setVisibility(View.VISIBLE);
        ImageSetter.setImage(holder.itemView.getContext(),holder.userPic,names.get(position).getEmailId(),holder.spinnerImage);
        holder.spinnerImage.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView userName,soldItems,profitText;
        private ImageView userPic;
        private ProgressBar spinnerImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_name);
            soldItems=itemView.findViewById(R.id.soldText);
            userPic=itemView.findViewById(R.id.user_pic);
            profitText=itemView.findViewById(R.id.profitText);
            spinnerImage=itemView.findViewById(R.id.progressBar9);
        }
    }
}
