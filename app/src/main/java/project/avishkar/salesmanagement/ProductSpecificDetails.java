package project.avishkar.salesmanagement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by user on 9/22/18.
 */

public class ProductSpecificDetails extends AppCompatActivity  {

    private RecyclerView mRecyclerView;
    private SalespersonDetailsAdapter mAdapter;
    private ProgressBar mProgressBar;
    ArrayList<SalesPerson> salespersonNames;
    ArrayList<String> sold_number;
    ArrayList<String> profit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_specific_salesperson_list);
        final String itemName = getIntent().getStringExtra("itemName");
        mProgressBar=findViewById(R.id.progressBar4);
        salespersonNames= new ArrayList<>();
        sold_number= new ArrayList<>();
        profit= new ArrayList<>();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Item");
        actionBar.setSubtitle(itemName);
        mProgressBar.setVisibility(View.VISIBLE);
        ListenerRunner1();
    }

    synchronized void ListenerRunner1(){

        SessionManager sm = new SessionManager(getApplicationContext());
        HashMap<String, String> mp = sm.getUserDetails();
        final String id = mp.get("id");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Manager");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(id)){
                        SalesManager sm = snapshot.getValue(SalesManager.class);
                        for(int i=0;i<10;i++)
                            System.out.println("TAG 1 :: " + sm.getName());
                        ListenerRunner2(sm.getName());
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

   synchronized void ListenerRunner2(final String managerName){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salesperson");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    SalesPerson sp = dataSnapshot1.getValue(SalesPerson.class);
                    if(sp.getManagerName().equals(managerName)){
                        salespersonNames.add(sp);

                        for(int i=0;i<10;i++)
                            System.out.println("TAG 2 :: " + sp.getName());

                        ListenerRunner3(dataSnapshot1.getKey());

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    synchronized void ListenerRunner3(String keySalesperson){

        final String itemName = getIntent().getStringExtra("itemName");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salesperson");
        databaseReference.child(keySalesperson).child("Inventory")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    InventoryItem it = dataSnapshot1.getValue(InventoryItem.class);
                    if(it.getItemName().equals(itemName)){
                        sold_number.add(String.valueOf(it.getSold()));
                        profit.add(String.valueOf(it.getProfit()));
                        for(int i=0;i<10;i++)
                        System.out.println("TAG 3 :: " + it.getSold());
                        populatingData();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    synchronized void populatingData(){


        for(int i=0;i<100;i++)
        System.out.println(""+salespersonNames.size()+" "+sold_number.size());

        mRecyclerView=findViewById(R.id.recyclerViewProduct);
        mAdapter= new SalespersonDetailsAdapter(getApplicationContext(), salespersonNames, sold_number, profit);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter.notifyDataSetChanged();
    }
}
