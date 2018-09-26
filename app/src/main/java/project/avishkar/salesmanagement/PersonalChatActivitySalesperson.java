package project.avishkar.salesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 9/25/18.
 */

public class PersonalChatActivitySalesperson extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    private Button sendButton;
    private EditText chatBox;
    private String SalespersonName;
    private String ManagerName;
    private String tmp1,tmp2;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_personal_chat);
        recyclerView=findViewById(R.id.recylerview_message_list);

        //getting current users info
        SessionManager sm = new SessionManager(getApplicationContext());
        final HashMap<String, String> mp = sm.getUserDetails();

        Intent i = getIntent();
        SalespersonName = i.getStringExtra("SalespersonName");
        ManagerName = i.getStringExtra("ManagerName");
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatsTable");
        sendButton = findViewById(R.id.button_chatbox_send);
        chatBox = findViewById(R.id.edittext_chatbox);
        tmp1 = ManagerName.replaceAll("\\s+","");
        tmp2 = SalespersonName.replaceAll("\\s+","");

        databaseReference.child(tmp1+"-"+tmp2).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                databaseReference.child(tmp1+"-"+tmp2).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<BaseMessage> mMessages=new ArrayList<>();

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            BaseMessage bm = dataSnapshot1.getValue(BaseMessage.class);
                            mMessages.add(bm);

                        }
                        messageListAdapter=new MessageListAdapter(getApplicationContext(),mMessages);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(messageListAdapter);
                        messageListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = chatBox.getText().toString();
                //getting current timestamp
                Long tsLong = System.currentTimeMillis()/1000 - 19800;
                String ts = tsLong.toString();
                if(!message.equals("")) {
                    BaseMessage baseMessage = new BaseMessage(message, ts, SalespersonName, mp.get("role"));
                    databaseReference = FirebaseDatabase.getInstance().getReference("ChatsTable");

                    String key1 = databaseReference.child(tmp1 + "-" + tmp2).push().getKey();
                    databaseReference.child(tmp1 + "-" + tmp2).child(key1).setValue(baseMessage);


                    databaseReference.child(tmp1 + "-" + tmp2).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<BaseMessage> ListOfMessages = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                BaseMessage bm = dataSnapshot1.getValue(BaseMessage.class);
                                ListOfMessages.add(bm);

                            }
                            messageListAdapter = new MessageListAdapter(getApplicationContext(), ListOfMessages);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(messageListAdapter);
                            messageListAdapter.notifyDataSetChanged();

                            chatBox.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "message is empty!!", Toast.LENGTH_SHORT).show();

                }



            }
        });

    }
}
