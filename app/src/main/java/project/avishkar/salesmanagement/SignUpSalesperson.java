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
                Intent intent = new Intent(SignUpSalesperson.this, salesperson_main.class);
                startActivity(intent);
                finish();
            }
        });


        SpannableString text = new SpannableString("Login");
        text.setSpan(new UnderlineSpan(), 0, 5, 0);
        login.setText(text);

    }

    void write_data(){

        String name = name_field.getText().toString();
        String email = email_field.getText().toString();
        String num = num_field.getText().toString();
        String password = password_field.getText().toString();
        String managerName = manager_name.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)
                || TextUtils.isEmpty(num) || TextUtils.isEmpty(managerName)){
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(getApplicationContext(), "All fields not filled !!", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        SalesPerson salesPerson = new SalesPerson(name, num, password, managerName, email);

        databaseRef = FirebaseDatabase.getInstance().getReference("Salesperson");
        String key = databaseRef.push().getKey();
        databaseRef.child(key).setValue(salesPerson);
        Toast.makeText(this, "Sales Person UID : "+key, Toast.LENGTH_SHORT).show();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.createLoginSession(key,"Salesperson");

        //register user on fireBase Authentication
        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpSalesperson.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpSalesperson.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpSalesperson.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(SignUpSalesperson.this, salesperson_main.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SignUpSalesperson.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
