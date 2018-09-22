package project.avishkar.salesmanagement;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<InventoryItem> list;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName,unalloted,sold,total;
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
        }
    }

    public InventoryAdapter(Context context,ArrayList<InventoryItem> list)
    {
        this.context=context;
        this.list=list;
    }

    @Override
    public InventoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InventoryAdapter.MyViewHolder holder, final int position) {
        InventoryItem item=list.get(position);
        Log.d("Setting for: ", String.valueOf(position));
        holder.total.setText(String.valueOf(item.getTotal_available()));
        holder.sold.setText(String.valueOf(item.getSold()));
        holder.unalloted.setText(String.valueOf(item.getNotAlloted()));
        holder.itemName.setText(item.getItemName());
        holder.progressBar.setMax(item.getTotal_available());
        holder.progressBar.setProgress(item.getSold());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,ProductSpecificDetails.class);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* new FancyAlertDialog.Builder((Activity) context)
                        .setTitle("Warning!!!")
                        .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                        .setMessage("Do you really want to Delete ?")
                        .setNegativeBtnText("No")
                        .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                        .setPositiveBtnText("Delete")
                        .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                        .setAnimation(Animation.POP)
                        .isCancellable(true)
                        .setIcon(R.drawable.ic_delete, Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
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
                        .build(); */

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

                                        //////////////////////////////////////////
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
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}