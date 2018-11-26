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

    public class calender {
        String beskrivelse;
        String dag;
        String fra;
        String til;
        Boolean repeat;
        calender() {

        }
        calender(String d, String f, String t, String b, Boolean r) {
            this.dag = d;
            this.fra = f;
            this.til = t;
            this.repeat = r;
            this.beskrivelse = b;
        }
    }

    private FirebaseAuth fAuth;
    FirebaseUser user;
    TextView tt;
    private RecyclerView list;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase fData;
    private DatabaseReference database;
    private DatabaseReference rolesRef;
    private DatabaseReference timeplanRef;
    ArrayList<String> roles;
    ArrayList<String> groups;
    ArrayList<calender> calenders;
    String ghettoKalender;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        mDrawerLayout.addView(contentView, 0);

        tt = findViewById(R.id.t);
        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        user = fAuth.getCurrentUser();
        database = fData.getReference();
        tt.setText("Welcome " + user.getEmail() + " here is your scedule ");

        //list.setHasFixedSize(true);

        fData = FirebaseDatabase.getInstance();

        database = fData.getReference();
        timeplanRef = database.child("timeplan");
        rolesRef = database.child("roles");

        roles = new ArrayList<String>();
        groups = new ArrayList<String>();
        calenders = new ArrayList<calender>();
        ghettoKalender = "";
        text = findViewById(R.id.kalender);

        rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //groups
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    //roles in groups
                    for(DataSnapshot dataRoles: data.getChildren()) {
                        //userids in roles in groups
                        for(DataSnapshot userIds: dataRoles.getChildren()){
                            if(userIds.getKey().equalsIgnoreCase(user.getUid())) {
                                roles.add(dataRoles.getKey());
                                groups.add(data.getKey());
                            }
                        }
                    }
                }
                loadSchedules();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void loadSchedules() {
        for(String s: groups) {
            Log.v("INFO", "123"+ s);
            Log.v("INFO", "123"+ roles.get(groups.indexOf(s)));
        }

        timeplanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(String group: groups) {
                    if(dataSnapshot.child(group).exists()) {
                        for(DataSnapshot data: dataSnapshot.child(group).getChildren()) {
                            if(data.getKey().equalsIgnoreCase(roles.get(groups.indexOf(group)))) {
                                Log.v("INFO", "Fant timeliste" + data);
                                for(DataSnapshot days: data.getChildren()) {
                                    calenders.add(new calender((String) days.getKey(), (String) days.child("fra").getValue(), (String) days.child("til").getValue(), (String) days.child("beskrivlelse").getValue() ,(Boolean) days.child("gjenta").getValue()));
                                }
                            }
                        }
                    }

                }
                loadCalender();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void loadCalender() {
        for(calender c: calenders) {
            ghettoKalender+= c.dag + "\n";
            ghettoKalender+= c.fra + " - " + c.til + "\n" + c.beskrivelse + "\n";



            if(c.repeat == false) {
                calenders.remove(c);
            }
            else {
                ghettoKalender+="Gjentar seg hver uke!" + "\n\n";
            }
            Log.v("INFO", ghettoKalender);

        }
        text.setText(ghettoKalender);
    }
}
