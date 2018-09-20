package project.avishkar.salesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mehul Garg on 01-09-2018.
 */

public class SignUpSalesManager extends AppCompatActivity {
    private TextInputEditText name_field;
    private TextInputEditText email_field;
    private TextInputEditText num_field;
    private TextInputEditText org_field;
    private TextInputEditText password_field;
    private TextView login;
    private Button signUp_button;
    private ProgressBar progressBar;
    private DatabaseReference databaseRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_manager);

        auth = FirebaseAuth.getInstance();
        login=findViewById(R.id.login);
        signUp_button=findViewById(R.id.signUp_button);
        name_field = findViewById(R.id.name);
        email_field = findViewById(R.id.emailid);
        num_field = findViewById(R.id.mobile);
        org_field = findViewById(R.id.organisation_name);
        password_field = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);


        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_data(view);
                Intent intent = new Intent(SignUpSalesManager.this, manager_main.class);
                startActivity(intent);
                finish();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSalesManager.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // for underline purpose(UI)
        SpannableString text = new SpannableString("Login");
        text.setSpan(new UnderlineSpan(), 0, 5, 0);
        login.setText(text);
    }

    void write_data(View v){

        String name = name_field.getText().toString();
        String email = email_field.getText().toString();
        String num = num_field.getText().toString();
        String password = password_field.getText().toString();
        String org = org_field.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)
                || TextUtils.isEmpty(num) || TextUtils.isEmpty(org)){
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(getApplicationContext(), "All fields not filled !!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        SalesManager salesManager = new SalesManager(name, num, password, email, org);
        //Added data to database
        databaseRef = FirebaseDatabase.getInstance().getReference("Manager");
        String key = databaseRef.push().getKey();
        databaseRef.child(key).setValue(salesManager);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.createLoginSession(key,"Manager");

        //register user on fireBase Authentication
        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpSalesManager.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpSalesManager.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpSalesManager.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {

                            startActivity(new Intent(SignUpSalesManager.this, manager_main.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpSalesManager.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
