package project.avishkar.salesmanagement;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 9/26/18.
 */

public class LeaderBoardSalesperson extends AppCompatActivity{


    private ArrayList<SalesPerson> SalespersonList;
    private ArrayList<String> performanceIndex;
    private int totalProfit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> mp = sessionManager.getUserDetails();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salesperson");
        //iterating on each salesperson
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    final SalesPerson salesPerson = dataSnapshot1.getValue(SalesPerson.class);
                    SalespersonList.add(salesPerson);
                    String key = dataSnapshot1.getKey();

                    //iterating over inventory of each salesperson

                    databaseReference.child(key).child("Inventory").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            totalProfit=0;
                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                InventoryItem inventoryItem = dataSnapshot2.getValue(InventoryItem.class);
                                totalProfit += inventoryItem.getSold()*inventoryItem.getProfit();

                            }
                            final Long currTimeInSecs = System.currentTimeMillis()/1000;

                            // getting signUp Time Stamp
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("LeaderBoard");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Long signupTimeInSecs = 0L;
                                    for(DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()){

                                        LeaderBoardObject leaderBoardObject = dataSnapshot3.getValue(LeaderBoardObject.class);
                                        if(leaderBoardObject.getSalespersonName().equals(salesPerson.getName())){

                                            signupTimeInSecs = Long.parseLong(leaderBoardObject.getTimestamp());
                                            Long timeDiff = ((Long)(currTimeInSecs - signupTimeInSecs)/86400)+1;
                                            String performanceCurr = String.valueOf((totalProfit*1.0)/timeDiff);
                                            performanceIndex.add(performanceCurr);
                                            populateData();
                                            break;
                                        }

                                    }





                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void populateData(){
        //do populating steps
    }
}
