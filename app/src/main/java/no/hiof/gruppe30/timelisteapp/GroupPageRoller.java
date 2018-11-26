package no.hiof.gruppe30.timelisteapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupPageRoller extends AppCompatActivity {

    public class groupmember {
        public String email;
        public String role;
        public groupmember(String email, String role) {
            this.email = email;
            this.role = role;
        }
    }

    private ArrayList<String> roles;
    private ArrayList<String> userIds;
    private ArrayList<String> users;
    private ArrayList<String> roleList;
    private ArrayList<String> roleToUser;
    private ArrayList<groupmember> groupMembers;
    private String title;

    private FirebaseDatabase fData;
    private FirebaseAuth fAuth;
    private DatabaseReference database;
    private DatabaseReference roleRef;
    EditText roleName;
    Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page_roller);

        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();

        database = fData.getReference();

        roles = new ArrayList<String>();
        users = new ArrayList<String>();
        userIds = new ArrayList<String>();
        roleToUser = new ArrayList<String>();
        groupMembers = new ArrayList<groupmember>();

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        title = (String) bd.get("title");
        Log.v("INFO", "timeplanrolle: " + title);

        spinner = (Spinner) findViewById(R.id.spinner_roles);


        roleName = (EditText) findViewById(R.id.edit_addRole);
        Button btnAddRole = (Button) findViewById(R.id.btn_addRole);

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

        btnAddRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.v("INFO", "1" + title);
                        String role = (String) roleName.getText().toString();
                        Log.v("INFO", "2" + role);
                        if(!dataSnapshot.child(title).child(role).exists()) {
                            database.child("roles").child(title).child(role).setValue(true);
                            addRolestoSpinner(role);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "AddRole: Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        generateMembers();
    }

    void addRolestoSpinner(String item) {
        spinner = (Spinner) findViewById(R.id.spinner_roles);
        roles.add(item);
        Log.v("INFOjkjj", roles.get(roles.size()-1));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, roles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    void generateMembers() {
        roleRef = database.child("roles");
        roleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(title).exists()) {
                    for(DataSnapshot data: dataSnapshot.child(title).getChildren()) {
                        for(DataSnapshot data2: data.getChildren()) {
                            Log.v("INFO", data2.getKey() + " -1-1- " + data.getKey());
                            roles.add(data.getKey());
                            userIds.add(data2.getKey());

                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "AddRole: Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference userRef = database.child("users");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    for(String g: userIds) {
                        if(data.child("userid").getValue().equals(userIds)) {

                            users.add(data.getKey());

                        }
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "AddRole: Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        Log.v("INFO", "1" + users);
        Log.v("INFO", "2" + roles);
        Log.v("INFO", "3" + userIds);

        Button b = new Button(this);
        ConstraintLayout buttonLayout = (ConstraintLayout) findViewById(R.id.layout_roller_members);
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.layout_roller);
        int i = 0;
        int y = 0;
        b.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        b.setText("TEST");
        b.setId(i);
        //add button to the layout
        buttonLayout.addView(b);
        Spinner sp = (Spinner) findViewById(R.id.spinner_roles);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(buttonLayout);
        constraintSet.connect(i, ConstraintSet.LEFT, buttonLayout.getId(), ConstraintSet.LEFT, 0);
        constraintSet.connect(i, ConstraintSet.TOP, buttonLayout.getId(), ConstraintSet.TOP, 0);
        constraintSet.applyTo(buttonLayout);


        for(String s: users) {
            b = new Button(this);
            Log.v("INFO", "gmember " + s);
            if(i==0) {
                b.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                b.setText(s + " -     Rolle: " + roles.get(users.indexOf(s)));
                b.setId(y);
                //add button to the layout
                buttonLayout.addView(b);

                ConstraintSet constraintSet2 = new ConstraintSet();
                constraintSet2.clone(buttonLayout);
                constraintSet2.connect(y, ConstraintSet.LEFT, buttonLayout.getId(), ConstraintSet.LEFT, 0);
                constraintSet2.connect(y, ConstraintSet.TOP, buttonLayout.getId(), ConstraintSet.TOP, 0);
                constraintSet2.applyTo(buttonLayout);

            }
            else {
                b.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                b.setText(s + " -     Rolle: " + roles.get(users.indexOf(s)));;
                b.setId(i);
                //add button to the layout
                buttonLayout.addView(b);

                ConstraintSet constraintSet2 = new ConstraintSet();
                constraintSet2.clone(buttonLayout);
                constraintSet2.connect(i, ConstraintSet.TOP, y, ConstraintSet.BOTTOM, 0);
                constraintSet2.applyTo(buttonLayout);
                y=i;
            }
            i++;


        }


    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }


}

