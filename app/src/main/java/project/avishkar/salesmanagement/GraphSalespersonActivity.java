package project.avishkar.salesmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphSalespersonActivity extends AppCompatActivity {

    private BarChart barChart;
    private String id;
    private ArrayList<BarEntry> entries;
    private int flag, store;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_salesperson);
        barChart=findViewById(R.id.chart);
        entries=new ArrayList<>();

        progressBar = findViewById(R.id.progressBar13);

        progressBar.setVisibility(View.VISIBLE);

        SessionManager sm = new SessionManager(getApplicationContext());
        HashMap<String, String> details = sm.getUserDetails();
        id = details.get("id");

        // fetching salesperson's name
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salesperson");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(id)){

                        //found current salesperson
                        final String salespersonName = snapshot.getValue(SalesPerson.class).getName();
                        flag = 0;

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("GraphSalesperson");
                        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                    GraphObject go = snapshot1.getValue(GraphObject.class);
                                    if(go.getName().equals(salespersonName)){

                                        // add in the list
                                        if(flag == 0)
                                        {
                                            entries.add(new BarEntry(0, Float.parseFloat(go.getProfit())));
                                            flag = 1;
                                            store = Integer.parseInt(go.getDate().substring(0,2));
                                        }
                                        else
                                        {
                                            int currentDay =  ((Integer.parseInt(go.getDate().substring(0,2)) + 30) - store) % 30;
                                            entries.add(new BarEntry(currentDay , Float.parseFloat(go.getProfit())));
                                        }
                                    }
                                }

                                progressBar.setVisibility(View.GONE);
                                BarDataSet dataSet=new BarDataSet(entries,"Profit");
                                dataSet.setValueTextSize(15f);
                                BarData data = new BarData(dataSet);
                                data.setBarWidth(0.9f); // set custom bar width
                                barChart.animateY(3000,Easing.EasingOption.EaseInBounce);
                                barChart.setData(data);
                                barChart.setFitBars(true); // make the x-axis fit exactly all bars
                                barChart.invalidate(); // refresh

                                XAxis xAxis = barChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setTextSize(15f);
                                xAxis.setGranularity(1f);
                                xAxis.setTextColor(Color.RED);
                                xAxis.setDrawAxisLine(true);
                                xAxis.setDrawGridLines(false);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
