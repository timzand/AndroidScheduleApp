package no.hiof.gruppe30.timelisteapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class GroupPageActivity extends BaseActivity {

    private TextView title;
    private ImageView image;
    private TextView description;
    private FirebaseDatabase fData;
    private FirebaseAuth fAuth;
    FirebaseUser user;
    private DatabaseReference database;
    int memberCount;
    String bTitle;

    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.group_page_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        title = findViewById(R.id.text_title);
        image = findViewById(R.id.image);
        description = findViewById(R.id.text_desc);

        memberCount = 0;
        final TextView textMembersCount = (TextView) findViewById(R.id.text_membersCount);

        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();

        database = fData.getReference();
        user = fAuth.getCurrentUser();

        bTitle = "default";
        String bDesc = "default";
        Bitmap bImage;

        final Button btnAdmin = (Button) findViewById(R.id.btn_administrate);
        final Button btnInvite = (Button) findViewById(R.id.btn_addMember);


        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupPageAdmin.class);
                intent.putExtra("title", bTitle);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();

        if(bd != null) {
            if (bd.get("title") != null) {
                bTitle = (String) bd.get("title");
                title.setText(bTitle);
                DatabaseReference groupsRef = database.child("groups");
                groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            if(data.getKey().equals(bTitle)) {
                                String desc = (String) data.child("description").getValue();
                                description.setText(desc);

                                if(user.getUid().equals((String) data.child("user").getValue())){
                                    Log.v("INFO", "You are admin of this group!");
                                    btnAdmin.setVisibility(View.VISIBLE);
                                    btnInvite.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            if (bd.get("desc") != null) {
                bDesc = (String) bd.get("desc");
                description.setText(bDesc);
            }
            if ((bd.get("groupImage") != null)) {
                bImage = (Bitmap) bd.getParcelable("groupImage");
                image.setImageBitmap(bImage);
            }
            if (bd.get("gTitle") != null) {
                final String gTitle = (String) bd.get("gTitle");
                title.setText(gTitle);
                bTitle = gTitle;

                DatabaseReference groupsRef = database.child("groups");
                DatabaseReference mRef = database.child("members");
                groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            if(data.getKey().equals(gTitle)) {
                                String desc = (String) data.child("description").getValue();
                                description.setText(desc);

                                if(user.getUid().equals((String) data.child("user").getValue())){
                                    Log.v("INFO", "You are admin of this group!");
                                    btnAdmin.setVisibility(View.VISIBLE);
                                    btnInvite.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int temp = 0;
                        for(DataSnapshot users: dataSnapshot.getChildren()) {
                            for(DataSnapshot groupsPerUser: users.getChildren()) {
                                Log.v("INFO", groupsPerUser.getKey() + " - " );
                                if(groupsPerUser.getKey().equals(gTitle)){
                                    temp++;
                                }
                            }
                        }
                        memberCount = temp;
                        textMembersCount.setText("" + temp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
