package project.avishkar.salesmanagement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AccountManager extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private String downloadUrl;
    private TextView update_email, update_mobile, change_password, logout, update_name, update_org;
    private String currEmail, currMobile, currName, currOrg, currPass;
    private FirebaseAuth auth;
    protected ImageView imageView,imageChooser,imageUploader;
    private DatabaseReference reference;
    private SalesPerson sp1;
    private SalesManager sm1, sm2;
    private Upload upload;
    private byte[] imageDataInByte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_UPLOADS);

        imageChooser=findViewById(R.id.image_picker);
        imageUploader=findViewById(R.id.image_uploader);
        auth = FirebaseAuth.getInstance();

        imageView=findViewById(R.id.user_pic);
        final ProgressBar spinnerImage = findViewById(R.id.progressBar8);

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

                            // downloading profile pic
                            spinnerImage.setVisibility(View.VISIBLE);
                            imageSetter.setImage(getApplicationContext(),imageView,currEmail);
                            spinnerImage.setVisibility(View.GONE);
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
                                    imageSetter.setImage(getApplicationContext(),imageView,currEmail);
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



        // file choosing and uploading code here


        imageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        imageUploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }

    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            filePath = data.getData();
            try {

                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                imageDataInByte = byteArrayOutputStream.toByteArray();
                Log.d("TAG: : ",""+filePath.toString());
                Glide.with(getApplicationContext()).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final String fileName=Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath);
            final StorageReference sRef = storageReference.child(fileName);

            sRef.putBytes(imageDataInByte)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "File uploaded", Toast.LENGTH_LONG).show();

                            Task<Uri> s= sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    downloadUrl=task.getResult().toString();
                                    upload = new Upload(currEmail,downloadUrl);

                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            int flag = 0;
                                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                if(snapshot.getValue(Upload.class).getEmail().equals(currEmail)){
                                                    mDatabase.child(snapshot.getKey()).setValue(upload);
                                                    for(int i=0;i<100;i++)
                                                        System.out.println("in the loop if");
                                                    flag = 1;
                                                    break;
                                                }
                                            }
                                            if(flag != 1){
                                                String uploadId = mDatabase.push().getKey();
                                                for(int i=0;i<100;i++)
                                                    System.out.println("in flag!=1 condition");
                                                mDatabase.child(uploadId).setValue(upload);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    for(int i=0;i<100;i++)
                                        System.out.println(downloadUrl);
                                }
                            });

                            Log.d("TAG:: ",filePath+"");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload failed!!",Toast.LENGTH_LONG ).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded "+ (int)progress + "%...");
                        }
                    });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No image selected!!", Toast.LENGTH_LONG).show();
        }
    }
}
