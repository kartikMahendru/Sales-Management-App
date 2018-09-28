package project.avishkar.salesmanagement.Graph;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphSalesperson {

    public static void create(final String profit, final String salespersonName) {

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        final String date = simpleDateFormat.format(new Date());

        for(int i=0;i<100;i++)
        System.out.println(date);

        final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("GraphSalesperson");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int flag=0;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    GraphObject mGraph=dataSnapshot1.getValue(GraphObject.class);
                    if(mGraph.getName().equals(salespersonName))
                    {
                        if(mGraph.getDate().equals(date))
                        {
                            flag=1;
                            GraphObject newGraph=new GraphObject(salespersonName,String.valueOf(Integer.parseInt(profit)+Integer.parseInt(mGraph.getProfit())),date);
                            mDatabaseReference.child(dataSnapshot1.getKey()).setValue(newGraph);
                            break;
                        }
                    }
                }

                // adding new node if name and date is not matched
                if(flag == 0)
                {
                    GraphObject newGraph=new GraphObject(salespersonName,String.valueOf(Integer.parseInt(profit)),date);
                    String key=mDatabaseReference.push().getKey();
                    mDatabaseReference.child(key).setValue(newGraph);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
