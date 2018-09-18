package project.avishkar.salesmanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private ArrayList<InventoryItem> data;
    private Context context;

    public CustomAdapter(Context context,ArrayList<InventoryItem> data)
    {
        this.data=data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InventoryItem item=data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_inventory, null);
        }
        TextView itemName=convertView.findViewById(R.id.itemName);
        TextView available=convertView.findViewById(R.id.available_units);
        TextView sold=convertView.findViewById(R.id.sold_quantity);


        // fill the views

        itemName.setText(item.getItemName());
        available.setText(String.valueOf(item.getTotal_available()));
        sold.setText(String.valueOf(item.getSold()));

        return convertView;
    }
}
