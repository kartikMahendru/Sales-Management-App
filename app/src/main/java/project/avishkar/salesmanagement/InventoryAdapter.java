package project.avishkar.salesmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<InventoryItem> list;
    private Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, unalloted, sold, total, profitText;
        public ImageView delete;
        public ProgressBar progressBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.item_name);
            unalloted=itemView.findViewById(R.id.unallocated);
            sold=itemView.findViewById(R.id.sold);
            total=itemView.findViewById(R.id.total_item);
            delete=itemView.findViewById(R.id.delete_icon);
            progressBar=itemView.findViewById(R.id.progressBar2);
            profitText=itemView.findViewById(R.id.profitText);

        }
    }

    public InventoryAdapter(Context context,ArrayList<InventoryItem> list, Activity activity)
    {
        this.context=context;
        this.list=list;
        this.activity=activity;
    }

    @Override
    public InventoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InventoryAdapter.MyViewHolder holder, final int position) {
        final InventoryItem item=list.get(position);
        Log.d("Setting for: ", String.valueOf(position));
        holder.total.setText(String.valueOf(item.getTotal_available()));
        holder.sold.setText(String.valueOf(item.getSold()));
        holder.unalloted.setText(String.valueOf(item.getTotal_available() - item.getSold()));
        holder.itemName.setText(item.getItemName());
        holder.progressBar.setMax(item.getTotal_available());
        holder.progressBar.setProgress(item.getSold());
        holder.profitText.setText(String.valueOf(item.getProfit() * item.getSold()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,ProductSpecificDetails.class);
                intent.putExtra("itemName",item.getItemName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FancyAlertDialog.Builder(activity)
                        .setTitle("Warning!!!")
                        .setBackgroundColor(Color.parseColor("#00A144"))  //Don't pass R.color.colorvalue
                        .setMessage("Do you really want to delete this item?")
                        .setNegativeBtnText("No")
                        .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                        .setPositiveBtnText("Yes")
                        .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                        .setAnimation(Animation.POP)
                        .isCancellable(true)
                        .setIcon(R.drawable.ic_error_outline_black_24dp, Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {

                                SessionManager sm = new SessionManager(context);
                                HashMap<String, String> details = sm.getUserDetails();
                                final String id = details.get("id");
                                final String role = details.get("role");

                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(role);

                                databaseRef.child(id).child("Inventory")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                int counter = -1;
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    counter++;
                                                    if(counter==position)
                                                    {
                                                        snapshot.getRef().removeValue();

                                                        //////////////////////////////////////////////////////////////////////////////////////////////
                                                        DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference("Salesperson");

                                                        databaseRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                DatabaseReference databaseRef2 = FirebaseDatabase.getInstance().getReference("Salesperson");
                                                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                                    String id1 = dataSnapshot1.getKey();
                                                                    databaseRef2.child(id1).child("Inventory")
                                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                                    int counter = -1;
                                                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                        counter++;
                                                                                        if(counter==position)
                                                                                        {
                                                                                            snapshot.getRef().removeValue();
                                                                                            break;
                                                                                        }
                                                                                    }

                                                                                }
                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                            }
                                                        });

                                                        break;
                                                    }
                                                }

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });


                                Toast.makeText(context , "Item deleted successfully !", Toast.LENGTH_LONG).show();
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                // do nothing here
                            }
                        })
                        .build();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}