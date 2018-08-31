package project.avishkar.salesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mehul Garg on 01-09-2018.
 */

public class SignUpSalesperson extends AppCompatActivity {

    private TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_salesperson);

        login=findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSalesperson.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // for underline purpose(UI)
        SpannableString text = new SpannableString("Login");
        text.setSpan(new UnderlineSpan(), 0, 5, 0);
        login.setText(text);

    }
}
