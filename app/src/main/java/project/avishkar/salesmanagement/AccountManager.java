package project.avishkar.salesmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountManager extends AppCompatActivity {


    TextView update_email, update_mobile, change_password, logout;

    protected ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        imageView=findViewById(R.id.user_pic);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boy);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        update_email = (TextView) findViewById(R.id.emailid);
        update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AccountManager.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_box_manager_email, null);

                Button update = (Button) mView.findViewById(R.id.update);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // update email id in firebase

                        dialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(view , "Email-id updated successfully !", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        });

        update_mobile = (TextView) findViewById(R.id.mobile);
        update_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AccountManager.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_box_manager_mobile, null);

                Button update = (Button) mView.findViewById(R.id.update);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // update mobile no. in firebase

                        dialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(view , "Mobile no. updated successfully !", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        });

        change_password = (TextView) findViewById(R.id.change_pass);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AccountManager.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_box_manager_change_password, null);

                Button update = (Button) mView.findViewById(R.id.update);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // authenticate old password and update new

                        dialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(view , "Password updated successfully !", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        });

        logout = (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountManager.this, MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
