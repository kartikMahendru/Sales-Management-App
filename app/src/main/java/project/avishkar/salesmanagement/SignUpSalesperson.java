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

public class SignUpSalesperson extends AppCompatActivity {

    private TextInputEditText name_field;
    private TextInputEditText email_field;
    private TextInputEditText num_field;
    private TextInputEditText inviteCode_field;
    private TextInputEditText password_field;
    private TextView login;
    private Button signUp;
    DatabaseReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_salesperson);

        login=findViewById(R.id.login);
        signUp=findViewById(R.id.signUp_button);
        name_field = findViewById(R.id.name);
        email_field = findViewById(R.id.emailid);
        num_field = findViewById(R.id.mobile);
        inviteCode_field = findViewById(R.id.invite_code);
        password_field = findViewById(R.id.password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_back_to_main();
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_data();
                go_back_to_main();
            }
        });


        SpannableString text = new SpannableString("Login");
        text.setSpan(new UnderlineSpan(), 0, 5, 0);
        login.setText(text);

    }

    void go_back_to_main(){
        Intent intent = new Intent(SignUpSalesperson.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void write_data(){

        SalesPerson salesPerson = new SalesPerson(name_field.getText().toString(),
                email_field.getText().toString(), num_field.getText().toString(),
                password_field.getText().toString());

        String[] ref = inviteCode_field.getText().toString().split("-",2);

        databaseRef = FirebaseDatabase.getInstance().getReference(ref[0]);
        String id = ref[0] + "-" + ref[1] + "-" + name_field.getText().toString().substring(0,4);
        databaseRef.child(inviteCode_field.getText().toString()).child(id).setValue(salesPerson);
        Toast.makeText(this, "Sales Person UID : "+id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SignUpSalesperson.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
