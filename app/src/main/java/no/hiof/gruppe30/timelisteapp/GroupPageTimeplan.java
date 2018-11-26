package no.hiof.gruppe30.timelisteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupPageTimeplan extends AppCompatActivity {



    private ArrayList<String> roles;
    private ArrayList<String> userIds;
    private ArrayList<String> users;
    private ArrayList<String> roleList;
    private ArrayList<String> roleToUser;
    private ArrayList<GroupPageRoller.groupmember> groupMembers;
    private String title;
    private ArrayList<String> days;

    //btn
    private Button leggtil;
    private CheckBox repetBox;
    private EditText tidFra1;
    private EditText tidTil1;
    private EditText bb;

    //spinner
    Spinner Sroles;
    Spinner daysp;
    private FirebaseDatabase fData;
    private FirebaseAuth fAuth;
    private DatabaseReference database;
    private DatabaseReference roleRef;
    private DatabaseReference timeplanRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page_timeplan);

        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();
        database = fData.getReference();



        roles = new ArrayList<String>();
        users = new ArrayList<String>();

        userIds = new ArrayList<String>();
        roleToUser = new ArrayList<String>();
        groupMembers = new ArrayList<GroupPageRoller.groupmember>();

        days = new ArrayList<String>();

        addDaystoSpinner();

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        title = (String) bd.get("title");
        Log.v("INFO", "timeplanrolle: " + title);


        Sroles = (Spinner) findViewById(R.id.spinner_role);
        daysp = (Spinner) findViewById(R.id.spinner_day);
        repetBox = (CheckBox) findViewById(R.id.checkBox);
        tidFra1 = (EditText) findViewById(R.id.fra);
        tidTil1 = (EditText) findViewById(R.id.til);
        leggtil = (Button) findViewById(R.id.btn_addTimeplan);
        bb = (EditText) findViewById(R.id.tb);

        roleRef = database.child("roles");
        roleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean exists = false;
                for(DataSnapshot data: dataSnapshot.child(title).getChildren()) {

                    Log.v("INFO", data.getKey());
                    addRolestoSpinner(data.getKey());



                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "CreateGroup: Database Error", Toast.LENGTH_SHORT).show();
            }
        });
        //timeplanRef = database.child("timeplan");

        leggtil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.v("INFO", "1 " + title);
                        String role = (String) Sroles.getSelectedItem().toString();
                        String day = (String) daysp.getSelectedItem().toString();

                        String tidFra = (String) tidFra1.getText().toString();
                        String tidTil = (String) tidTil1.getText().toString();
                        String beskrivelse = (String) bb.getText().toString();
                        //tid var

                        Log.v("INFO", "2 " + role);


                        if(dataSnapshot.child(title).child(role).exists()) {

                            Log.v("INFO", "3 " + day);
                            Log.v("INFO", "4 " + tidFra);
                            Log.v("INFO", "5 " + tidTil);

                            if(repetBox.isChecked()){
                                database.child("timeplan").child(title).child(role).child(day).setValue(true);
                                database.child("timeplan").child(title).child(role).child(day).child(tidFra).setValue(true);
                                database.child("timeplan").child(title).child(role).child(day).child(tidTil).setValue(true);
                                database.child("timeplan").child(title).child(role).child(day).child(beskrivelse).setValue(true);
                                Log.v("INFO", "6 works" );

                            }
                            else{

                                database.child("timeplan").child(title).child(role).child(day).setValue(false);
                                database.child("timeplan").child(title).child(role).child(day).child(tidFra).setValue(false);
                                database.child("timeplan").child(title).child(role).child(day).child(tidTil).setValue(false);
                                database.child("timeplan").child(title).child(role).child(day).child(beskrivelse).setValue(true);
                            }



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "AddRole: Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




    }
    void addRolestoSpinner(String item) {
        Sroles = (Spinner) findViewById(R.id.spinner_role);
        roles.add(item);
        Log.v("INFOjkjj", roles.get(roles.size()-1));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, roles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sroles.setAdapter(dataAdapter);
    }

    void addDaystoSpinner() {
        daysp = (Spinner) findViewById(R.id.spinner_day);
        days.add("Mandag");
        days.add("Tirsdag");
        days.add("Onsdag");
        days.add("Torsdag");
        days.add("Fredag");
        days.add("Lørdag");
        days.add("Søndag");
        Log.v("INFOjkjj", days.get(days.size()-1));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, days);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysp.setAdapter(dataAdapter);
    }
}
