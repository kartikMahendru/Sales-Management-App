package project.avishkar.salesmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AccountManager extends AppCompatActivity {


    private TextView update_email, update_mobile, change_password, logout, update_name, update_org;
    private String currEmail, currMobile, currName, currOrg, currPass;
    private FirebaseAuth auth;
    protected ImageView imageView;
    private DatabaseReference reference;
    private SalesPerson sp1;
    private SalesManager sm1, sm2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        auth = FirebaseAuth.getInstance();

        imageView=findViewById(R.id.user_pic);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boy);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        SessionManager sm = new SessionManager(getApplicationContext());
        HashMap<String, String> details = sm.getUserDetails();
        final String id = details.get("id");
        final String role = details.get("role");

          if(role.equals("Manager")) {

              reference = FirebaseDatabase.getInstance().getReference("Manager");

              final ProgressDialog dialog = ProgressDialog.show(AccountManager.this, "Loading...","Please wait..." , true);
              dialog.show();
              Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                  public void run() {
                      //your code here
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        sm1 = snapshot.getValue(SalesManager.class);
                        //Toast.makeText(getApplicationContext(),snapshot.getKey()+" ++ "+id,Toast.LENGTH_LONG).show();
                        if(snapshot.getKey().equals(id)){

                            currName = sm1.getName();
                            currPass = sm1.getPassword();
                            //Toast.makeText(getApplicationContext(), ""+sm1.getName(), Toast.LENGTH_SHORT).show();
                            currOrg = sm1.getOrgName();
                            currMobile = sm1.getNumber();
                            currEmail = sm1.getEmail();
                            break;
                        }

                    }

                    update_name.setText(currName);
                    update_org.setText(currOrg);
                    update_email.setText(currEmail);
                    update_mobile.setText(currMobile);
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
                });


                  }
              }, 4000);  // 4000 milliseconds

        }
        else
        {
            reference = FirebaseDatabase.getInstance().getReference("Salesperson");

            final ProgressDialog dialog = ProgressDialog.show(AccountManager.this, "Loading...","Please wait..." , true);
            dialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    //your code here
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        sp1 = snapshot.getValue(SalesPerson.class);
                        if (snapshot.getKey().equals(id)) {

                            currPass = sp1.getPassword();
                            currName = sp1.getName();
                            currMobile = sp1.getNumber();
                            currEmail = sp1.getEmailId();

                            final String currManager = sp1.getManagerName();
                            reference = FirebaseDatabase.getInstance().getReference("Manager");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        sm2 = snapshot.getValue(SalesManager.class);
                                        if (sm2.getName().equals(currManager)) {
                                            currOrg = sm2.getOrgName();
                                            break;
                                        }
                                    }
                                    update_name.setText(currName);
                                    update_org.setText(currOrg);
                                    update_email.setText(currEmail);
                                    update_mobile.setText(currMobile);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

                });
                }
            }, 4000);  // 4000 milliseconds
        }

        //Toast.makeText(this,currOrg+"**"+currName,Toast.LENGTH_LONG).show();
        update_name = (TextView) findViewById(R.id.name);

        update_org = (TextView) findViewById(R.id.organisation);
        //update_org.setText(currOrg);

        update_email = (TextView) findViewById(R.id.emailid);
        //update_email.setText(currEmail);
        update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AccountManager.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_box_manager_email, null);
                mBuilder.setTitle("Update email-id");

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
        //update_mobile.setText(currMobile);
        update_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AccountManager.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_box_manager_mobile, null);
                mBuilder.setTitle("Update mobile number");

                Button update = (Button) mView.findViewById(R.id.update);
                final EditText updated_number = (EditText) mView.findViewById(R.id.mobile);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // update mobile no. in firebase

                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Mobile no. updated successfully!", Toast.LENGTH_LONG)
                                .show();
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
                mBuilder.setTitle("Change password");

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
                SessionManager sm = new SessionManager(getApplicationContext());
                sm.logoutUser();
                auth.signOut();
                Intent intent=new Intent(AccountManager.this, MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
