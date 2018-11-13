package no.hiof.gruppe30.timelisteapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupPageActivity extends BaseActivity {

    private TextView title;
    private ImageView image;
    private TextView description;
    private FirebaseDatabase fData;
    private DatabaseReference database;

    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.group_page_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        fData = FirebaseDatabase.getInstance();

        database = fData.getReference();

        title = findViewById(R.id.text_title);
        image = findViewById(R.id.image);
        description = findViewById(R.id.text_desc);

        String bTitle = "default";
        String bDesc = "default";
        Bitmap bImage;

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd.get("title") != null && !(bd.get("title").equals(null))){
            bTitle = (String) bd.get("title");
            title.setText(bTitle);
        }
        if(bd.get("desc") != null && !(bd.get("desc").equals(null))){
            bDesc = (String) bd.get("desc");
            description.setText(bDesc);
        }
        if(bd.getParcelable("groupImage") != null && bd.getParcelable("groupImage") != null){
            bImage = (Bitmap) bd.getParcelable("groupImage");
            image.setImageBitmap(bImage);
        }
        if(bd.get("subMenuGroup") != null && !(bd.get("subMenuGroup").equals(null))) {
            final String title2 = (String) bd.get("subMenuGroup");
            title.setText(title2);

            DatabaseReference groupRef = database.child("groups");

            //Aktiveres ikke nedenfor

            groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.v("INFO", "datasnapshot singlevlalueevent");
                    //DataSnapshot thisUser = dataSnapshot.child("groups");
                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        Log.v("INFO", "datasnapshot " + data.getValue());
                        Log.v("INFO", "datasnapshot " + data.getKey());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
}
