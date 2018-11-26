package no.hiof.gruppe30.timelisteapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//suport https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
public class MainActivity extends BaseActivity {

    private FirebaseAuth fAuth;
    FirebaseUser user;
    TextView tt;
    private RecyclerView list;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase fData;
    private DatabaseReference database;
    private DatabaseReference members;
    private DatabaseReference timeplan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        mDrawerLayout.addView(contentView, 0);

        tt = findViewById(R.id.t);
        list = findViewById(R.id.recyclerView);
        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        user = fAuth.getCurrentUser();
        database = fData.getReference();
        tt.setText("Welcome " + user.getEmail() + " here is your scedule ");

        //list.setHasFixedSize(true);

        members = database.child("roles");
        timeplan = database.child("timeplan");

        members.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        timeplan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        Log.v("INFO", "1 t" );
        ArrayList<String> timePlan = new ArrayList<>();
        timePlan.add("Horse");

        String me = user.getUid();

        database.child("members").child(me);





        Log.v("INFO", "2 t" );
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, timePlan);
        list.setAdapter(mAdapter);
        Log.v("INFO", "3 t" );



    }
}
