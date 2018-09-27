package project.avishkar.salesmanagement.Leaderboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import project.avishkar.salesmanagement.InventoryItem;
import project.avishkar.salesmanagement.R;
import project.avishkar.salesmanagement.SalesPerson;
import project.avishkar.salesmanagement.SessionManager;

import static java.lang.Thread.sleep;

/**
 * Created by user on 9/26/18.
 */

public class LeaderBoardSalesperson extends AppCompatActivity{


    private ArrayList<SalesPerson> SalespersonList;
    private ArrayList<String> performanceIndex;
    // private int totalProfit;
    private LeaderBoardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton fabButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        final HashMap<String, String> mp = sessionManager.getUserDetails();

        SalespersonList = new ArrayList<>();
        performanceIndex = new ArrayList<>();

        progressBar=findViewById(R.id.leaderboard_progress);
        progressBar.setVisibility(View.VISIBLE);
        ListenerRunner1();

        fabButton = findViewById(R.id.fabButton);

        ///////////////adding intent/////////////////////////
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String currId = mp.get("id");
                for(int i=0;i<10;i++)
                    System.out.println("inside fab");
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Salesperson");
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            for(int i=0;i<10;i++)
                                System.out.println("inside listener");
                            if(dataSnapshot1.getKey().equals(currId)){
                                for(int i=0;i<10;i++)
                                    System.out.println("inside if");
                                //Intent intent = new Intent(LeaderBoardSalesperson.this, RecommendationAlgo.class);
                                SalesPerson salesPerson = dataSnapshot1.getValue(SalesPerson.class);
                                ArrayList<Double> arrayList = new ArrayList<>();
                                for(int i=0;i<10;i++)
                                    System.out.println("size performanceIndex: "+performanceIndex.size());
                                for(int i=0;i<performanceIndex.size();i++){
                                    arrayList.add(Double.parseDouble(performanceIndex.get(i)));
                                }
                                String currName = salesPerson.getName();
                                Double PiTopper = Collections.max(arrayList);
                                int i;
                                for(i=0;i<SalespersonList.size();i++){
                                    if(SalespersonList.get(i).getName().equals(currName))
                                        break;
                                }
                                for(int j=0;j<10;j++)
                                    System.out.println(i);
                                Double PiCurr = Double.parseDouble(performanceIndex.get(i));

                                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LeaderBoardSalesperson.this);
                                final View mView = getLayoutInflater().inflate(R.layout.recommendation_algo_view, null);
                                TextView target=mView.findViewById(R.id.target);

                                Double targetProfit = (PiCurr + (PiTopper - PiCurr))*1.10;
                                target.setText(String.valueOf(targetProfit));

                                CircleProgressbar circleProgressbar=mView.findViewById(R.id.circular_progress);
                                circleProgressbar.setForegroundProgressColor(getResources().getColor(R.color.colorPrimary));
                                circleProgressbar.setBackgroundProgressWidth(15);
                                circleProgressbar.setForegroundProgressWidth(20);
                                circleProgressbar.enabledTouch(true);
                                circleProgressbar.setRoundedCorner(true);
                                circleProgressbar.setClockwise(true);
                                int animationDuration = 1000; // 2500ms = 2,5s
                                circleProgressbar.setProgressWithAnimation(100, animationDuration);
                                mBuilder.setView(mView);

                                final AlertDialog dialog = mBuilder.create();

                                dialog.show();
                                //startActivity(intent);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



    }
    synchronized void ListenerRunner1(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salesperson");
        //iterating on each salesperson
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    final SalesPerson salesPerson = dataSnapshot1.getValue(SalesPerson.class);
                    String key = dataSnapshot1.getKey();

                    //iterating over inventory of each salesperson
                    ListenerRunner2(key,salesPerson.getName(),salesPerson);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    synchronized void ListenerRunner2(String key, final String name, final SalesPerson salesPerson){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salesperson");
        databaseReference.child(key).child("Inventory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalProfit=0;
                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                    InventoryItem inventoryItem = dataSnapshot2.getValue(InventoryItem.class);
                    totalProfit += inventoryItem.getSold()*inventoryItem.getProfit();

                }
                ListenerRunner3(name, salesPerson, totalProfit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    synchronized void ListenerRunner3(final String name, final SalesPerson salesPerson, final int totalProfit){

        final Long currTimeInSecs = System.currentTimeMillis()/1000;

        // getting signUp Time Stamp
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("LeaderBoard");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Long signupTimeInSecs = 0L;
                for(DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()){

                    LeaderBoardObject leaderBoardObject = dataSnapshot3.getValue(LeaderBoardObject.class);

                    if(leaderBoardObject.getSalespersonName().equals(name)){

                        signupTimeInSecs = Long.parseLong(leaderBoardObject.getTimestamp());
                        Long timeDiff = ((Long)(currTimeInSecs - signupTimeInSecs)/86400)+1;
                        String performanceCurr = String.valueOf((totalProfit*1.0)/timeDiff);
                        int index = performanceCurr.lastIndexOf('.');
                        performanceCurr = performanceCurr.substring(0,index+2);
                        performanceIndex.add(performanceCurr);
                        SalespersonList.add(salesPerson);
                        populateData();
                        progressBar.setVisibility(View.GONE);

                        break;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    synchronized void populateData(){

            mRecyclerView=findViewById(R.id.leaderboard_recycler_view);
            mAdapter= new LeaderBoardAdapter(getApplicationContext(), SalespersonList, performanceIndex);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

    }
}
