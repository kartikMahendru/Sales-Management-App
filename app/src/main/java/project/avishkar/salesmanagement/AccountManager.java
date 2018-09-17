package project.avishkar.salesmanagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AccountManager extends AppCompatActivity {


    Button update_email, update_mobile, change_password;

    protected ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_manager);

        imageView=findViewById(R.id.user_pic);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boy);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        update_email = (Button) findViewById(R.id.emailid);
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
                        Toast.makeText(AccountManager.this, "Email-id updated successfully !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        update_mobile = (Button) findViewById(R.id.mobile);
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
                        Toast.makeText(AccountManager.this, "Mobile no. updated successfully !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        change_password = (Button) findViewById(R.id.change_pass);
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
                        Toast.makeText(AccountManager.this, "Password updated successfully !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
