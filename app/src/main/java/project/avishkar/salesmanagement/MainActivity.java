package project.avishkar.salesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView manager,salesperson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        manager = findViewById(R.id.Manager);
        salesperson=findViewById(R.id.salesperson);

        // setting on click for sign up for retailer
        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpSalesManager.class);
                startActivity(intent);
                finish();
            }
        });


        // setting on click for sign up for salesperson
        salesperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpSalesperson.class);
                startActivity(intent);
                finish();
            }
        });


        // for underline purpose(UI)
        SpannableString text = new SpannableString("Sign up as a Retailer");
        text.setSpan(new UnderlineSpan(), 0, 21, 0);
        manager.setText(text);

        // for underline purpose(UI)
        SpannableString text1 = new SpannableString("Sign up as a Salesperson");
        text1.setSpan(new UnderlineSpan(), 0, 24, 0);
        salesperson.setText(text1);

    }
}
