package project.avishkar.salesmanagement.Chat;

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

import project.avishkar.salesmanagement.R;
import project.avishkar.salesmanagement.SessionManager;

/**
 * Created by user on 9/24/18.
 */

public class ChatRoom extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ChatRoomAdapter messageListAdapter;
    private Button sendButton;
    private EditText chatBox;
    private String SalespersonName;
    private String ManagerNumber;
    private DatabaseReference databaseReference, databaseReference1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_personal_chat);

        recyclerView=findViewById(R.id.recylerview_message_list);
        // getting salesperson name
        final Intent it = getIntent();
        SalespersonName = it.getStringExtra("Name");
        ManagerNumber = it.getStringExtra("ManagerNumber");

        //getting current users info
        SessionManager sm = new SessionManager(getApplicationContext());
        final HashMap<String, String> mp = sm.getUserDetails();

        sendButton = findViewById(R.id.button_chatbox_send);
        chatBox = findViewById(R.id.edittext_chatbox);
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatRoom");

        databaseReference.child(ManagerNumber).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                databaseReference.child(ManagerNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<BaseMessage> mMessages=new ArrayList<>();

                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            BaseMessage bm = dataSnapshot1.getValue(BaseMessage.class);
                            mMessages.add(bm);

                        }
                        messageListAdapter=new ChatRoomAdapter(getApplicationContext(),mMessages);
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
                Long tsLong = System.currentTimeMillis() / 1000 - 19800;
                String ts = tsLong.toString();
                if (message.equals("")) {
                    Toast.makeText(getApplicationContext(), "message is empty!!", Toast.LENGTH_SHORT).show();
                } else {
                    BaseMessage baseMessage = new BaseMessage(message, ts, SalespersonName, mp.get("id"));
                    databaseReference = FirebaseDatabase.getInstance().getReference("ChatRoom");

                    String key1 = databaseReference.child(ManagerNumber).push().getKey();
                    databaseReference.child(ManagerNumber).child(key1).setValue(baseMessage);

                    databaseReference.child(ManagerNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<BaseMessage> mMessages = new ArrayList<>();

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                BaseMessage bm = dataSnapshot1.getValue(BaseMessage.class);
                                mMessages.add(bm);

                            }
                            messageListAdapter = new ChatRoomAdapter(getApplicationContext(), mMessages);
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
            }});


    }

}
