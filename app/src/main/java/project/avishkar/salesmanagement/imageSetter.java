package project.avishkar.salesmanagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class imageSetter {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_UPLOADS);
    public static void setImage(final Context context, final ImageView view,final String currEmail)
    {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Upload upload=snapshot.getValue(Upload.class);
                    if(upload.getEmail().equals(currEmail)){
                        Glide.with(context).load(upload.getUrl()).apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.boy)).into((ImageView) view);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
