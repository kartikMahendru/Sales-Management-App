package project.avishkar.salesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_manager);

        login=findViewById(R.id.login);
        signUp_button=findViewById(R.id.signUp_button);
        name_field = findViewById(R.id.name);
        email_field = findViewById(R.id.emailid);
        num_field = findViewById(R.id.mobile);
        org_field = findViewById(R.id.organisation_name);
        password_field = findViewById(R.id.password);


        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_data();
                go_back_to_main();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_back_to_main();
            }
        });


        // for underline purpose(UI)
        SpannableString text = new SpannableString("Login");
        text.setSpan(new UnderlineSpan(), 0, 5, 0);
        login.setText(text);
    }


    void go_back_to_main(){
        Intent intent = new Intent(SignUpSalesManager.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void write_data() {

        SalesManager salesManager = new SalesManager(name_field.getText().toString(),
                email_field.getText().toString(), num_field.getText().toString(),
                password_field.getText().toString());

        databaseRef = FirebaseDatabase.getInstance().getReference(org_field.getText().toString());
        String id = org_field.getText().toString() + "-" + num_field.getText().toString().substring(5);
        databaseRef.child(id).setValue(salesManager);
        Toast.makeText(this, "Sales Manager UID : " + id, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SignUpSalesManager.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
