package project.avishkar.salesmanagement.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.avishkar.salesmanagement.InventoryItem;
import project.avishkar.salesmanagement.Leaderboard.LeaderBoardObject;
import project.avishkar.salesmanagement.R;
import project.avishkar.salesmanagement.SalesManager;
import project.avishkar.salesmanagement.SalesPerson;
import project.avishkar.salesmanagement.SalespersonMain;
import project.avishkar.salesmanagement.SessionManager;

/**
 * Created by Mehul Garg on 01-09-2018.
 */

public class SignUpSalesperson extends AppCompatActivity {

    private TextInputEditText name_field;
    private TextInputEditText email_field;
    private TextInputEditText num_field;
    private TextInputEditText manager_name;
    private TextInputEditText password_field;
    private TextView login;
    private Button signUp;
    private ProgressBar progressBar;
    DatabaseReference databaseRef;
    private FirebaseAuth auth;
    private int tmp = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_salesperson);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        login=findViewById(R.id.login);
        signUp=findViewById(R.id.signUp_button);
        name_field = findViewById(R.id.name);
        email_field = findViewById(R.id.emailid);
        num_field = findViewById(R.id.mobile);
        manager_name = findViewById(R.id.manager_name);
        password_field = findViewById(R.id.password);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSalesperson.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_data();
            }
        });


        SpannableString text = new SpannableString("Login");
        text.setSpan(new UnderlineSpan(), 0, 5, 0);
        login.setText(text);

    }

    void write_data(){

        final String name = name_field.getText().toString();
        final String email = email_field.getText().toString();
        final String num = num_field.getText().toString();
        final String password = password_field.getText().toString();
        final String managerName = manager_name.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)
                || TextUtils.isEmpty(num) || TextUtils.isEmpty(managerName)){

                Toast.makeText(getApplicationContext(), "All fields not filled !!", Toast.LENGTH_SHORT).show();
                return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isValidMobile(num)){
            Toast.makeText(getApplicationContext(), "Invalid mobile number !!", Toast.LENGTH_SHORT).show();
            return;
        }
        // email check
        if(!isValidEmail(email)){
            Toast.makeText(getApplicationContext(), "Invalid email !!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        databaseRef = FirebaseDatabase.getInstance().getReference("Salesperson");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    SalesPerson sp = dataSnapshot1.getValue(SalesPerson.class);
                    if(sp.getEmailId().equals(email)){
                        Toast.makeText(getApplicationContext(),"This email-id is already registered !!", Toast.LENGTH_LONG).show();
                        tmp = -1;
                        break;
                    }
                    if(sp.getNumber().equals(num)){
                        Toast.makeText(getApplicationContext(),"This mobile number is already registered !!", Toast.LENGTH_LONG).show();
                        tmp = -1;
                        break;
                    }
                }
                databaseRef = FirebaseDatabase.getInstance().getReference("Manager");
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int x=0;
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                            SalesManager salesManager = dataSnapshot1.getValue(SalesManager.class);
                            if(salesManager.getName().equals(managerName)){
                                x=1;
                                break;
                            }
                        }
                        if(x == 0){
                            Toast.makeText(getApplicationContext(),"This Manager does not exist !!", Toast.LENGTH_LONG).show();
                            tmp = -1;
                        }
                        if(tmp != -1){

                            SalesPerson salesPerson = new SalesPerson(name, num, password, managerName, email);
                            //Added data to database
                            databaseRef = FirebaseDatabase.getInstance().getReference("Salesperson");
                            final String key = databaseRef.push().getKey();
                            databaseRef.child(key).setValue(salesPerson);

                            //added chat room with manager
                            //String tmp1 = managerName.replaceAll("\\s+","");
                            //String tmp2 = name.replaceAll("\\s+","");
                            //databaseRef = FirebaseDatabase.getInstance().getReference("ChatsTable");
                            //String key1 = databaseRef.child(tmp1+"-"+tmp2).push().getKey();
                            //databaseRef.child(key1).setValue(new BaseMessage("sample","0000","sampler1","role"));


                            // session created using sharedPreference
                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            sessionManager.createLoginSession(key,"Salesperson");

                            //adding node under LeaderBoard
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("LeaderBoard");
                            String key1 = databaseReference1.push().getKey();

                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();

                            LeaderBoardObject leaderBoardObject = new LeaderBoardObject(name, ts);
                            databaseReference1.child(key1).setValue(leaderBoardObject);
                            //register user on fireBase Authentication

                            //create user
                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUpSalesperson.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //Toast.makeText(SignUpSalesManager.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                            if (!task.isSuccessful()) {
                                                Toast.makeText(SignUpSalesperson.this, "Authentication failed." + task.getException(),
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                            else {

                                                // checking if any items were present in manager's inventory which are to be allotted to new salesperson
                                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Manager");
                                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                            if(snapshot.getValue(SalesManager.class).getName().equals(managerName)){
                                                                // Manager found now go to his inventory

                                                                // Taking a reference of current salesperson
                                                                final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Salesperson").child(key);

                                                                databaseReference.child(snapshot.getKey()).child("Inventory")
                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                                                                    InventoryItem it = snapshot1.getValue(InventoryItem.class);
                                                                                    InventoryItem itNew = new InventoryItem(it.getItemName(),it.getTotal_available() - it.getSold(), 0, it.getProfit());
                                                                                    databaseReference1.child("Inventory").child(databaseReference1.child("Inventory").push().getKey()).setValue(itNew);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                startActivity(new Intent(SignUpSalesperson.this, SalespersonMain.class));
                                                                finish();

                                                                break;
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
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
    public static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SignUpSalesperson.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
