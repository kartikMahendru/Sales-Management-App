package project.avishkar.salesmanagement.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import project.avishkar.salesmanagement.ManagerMain;
import project.avishkar.salesmanagement.R;
import project.avishkar.salesmanagement.SalesManager;
import project.avishkar.salesmanagement.SalesPerson;
import project.avishkar.salesmanagement.SalespersonMain;
import project.avishkar.salesmanagement.SessionManager;

public class MainActivity extends AppCompatActivity {

    private TextView manager,salesperson;
    private Button login;
    private TextInputEditText inputEmail, inputPassword;
    private Button forgot_password;
    private FirebaseAuth auth;
    private RadioGroup radioGroup_type;
    private ProgressBar progressBar;
    private DatabaseReference databaseRef;
    private String email, password;
    private int flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            SessionManager sm = new SessionManager(getApplicationContext());
            HashMap<String,String> details = sm.getUserDetails();
            String tmp1 = details.get("id");
            String tmp2 = details.get("role");
            if(!TextUtils.isEmpty(tmp1)&& !TextUtils.isEmpty(tmp2)){
                Toast.makeText(this, "User "+tmp1+" logged in", Toast.LENGTH_SHORT).show();

                if(tmp2.equals("Manager")){
                    goto_Manager();
                }
                else {
                    goto_Salesperson();
                }
            }
        }

        setContentView(R.layout.login);

        manager = findViewById(R.id.Manager);
        salesperson=findViewById(R.id.salesperson);
        login=findViewById(R.id.login_press);
        radioGroup_type = findViewById(R.id.radioGroup_type_person);
        inputEmail = findViewById(R.id.emailid);
        inputPassword = findViewById(R.id.password);
        forgot_password = findViewById(R.id.btn_reset_password);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpSalesManager.class);
                startActivity(intent);
                finish();
            }
        });

        salesperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpSalesperson.class);
                startActivity(intent);
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Snackbar snackbar = Snackbar.make(v, "Enter Email!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Snackbar snackbar = Snackbar.make(v, "Enter Password!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {

                                    if(RadioButtonSelect(radioGroup_type.getCheckedRadioButtonId()).equals("Manager")){

                                        databaseRef = FirebaseDatabase.getInstance().getReference("Manager");
                                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                    SalesManager sm1 = snapshot.getValue(SalesManager.class);
                                                    if(sm1.getEmail().equals(email)) {
                                                        SessionManager sm = new SessionManager(getApplicationContext());
                                                        sm.createLoginSession(snapshot.getKey(), "Manager");
                                                        flag = 1;
                                                        progressBar.setVisibility(View.GONE);
                                                        goto_Manager();
                                                        break;
                                                    }
                                                }
                                                // authentication done but chose wrong radio button
                                                if(flag != 1){
                                                    Toast.makeText(getApplicationContext(),"This email is not registerd as manager!!",Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else{
                                        databaseRef = FirebaseDatabase.getInstance().getReference("Salesperson");
                                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    SalesPerson sm1 = snapshot.getValue(SalesPerson.class);
                                                    if(sm1.getEmailId().equals(email)){
                                                        SessionManager sm = new SessionManager(getApplicationContext());
                                                        flag=1;
                                                        sm.createLoginSession(snapshot.getKey().toString(), "Salesperson");
                                                        progressBar.setVisibility(View.GONE);
                                                        goto_Salesperson();
                                                    }
                                                }
                                                if(flag != 1){
                                                    Toast.makeText(getApplicationContext(),"This Email is not registerd as Salesperson!!",Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }
                            }
                        });

            }
        });

        // for underline purpose(UI)
        SpannableString text = new SpannableString("Sign up as a Manager");
        text.setSpan(new UnderlineSpan(), 0, 20, 0);
        manager.setText(text);

        // for underline purpose(UI)
        SpannableString text1 = new SpannableString("Sign up as a Salesperson");
        text1.setSpan(new UnderlineSpan(), 0, 24, 0);
        salesperson.setText(text1);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                if(TextUtils.isEmpty(email)){

                    Snackbar snackbar = Snackbar.make(view,"Enter email in above blank and click again!!",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    }

                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
    }



    void goto_Manager() {
        Intent intent=new Intent(MainActivity.this,ManagerMain.class);
        startActivity(intent);
        finish();
    }

    void goto_Salesperson() {
        Intent intent=new Intent(MainActivity.this,SalespersonMain.class);
        startActivity(intent);
        finish();
    }

    String RadioButtonSelect(int selectId){
        RadioButton radioButton = findViewById(selectId);
        return radioButton.getText().toString();
    }
}