package no.hiof.gruppe30.timelisteapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    private FirebaseDatabase fData;
    private FirebaseAuth fAuth;
    FirebaseUser user;
    private DatabaseReference database;
    ArrayList<String> groups;
    int selectedSubMenu;

    //https://developer.android.com/training/implementing-navigation/nav-drawer

    protected DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        groups = new ArrayList<String>();

        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();


        database = fData.getReference();
        user = fAuth.getCurrentUser();

        selectedSubMenu = -1; //-1

        //Legger til toolbaren til activitien
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Legger til hamburgerikon på drawermenyen
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        Log.v("INFO", ""+selectedSubMenu);
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch(menuItem.getItemId()) {
                            case R.id.nav_dashboard:
                                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                                return true;
                            case R.id.nav_creategrp:
                                //Endrer activity til opprett gruppe
                                Intent intent2 = new Intent(BaseActivity.this, CreateGroupActivity.class);
                                finish();
                                startActivity(intent2);
                                return true;
                            case R.id.nav_joingrp:
                                //Endre activity til
                            case R.id.nav_user:
                                Intent intent4 = new Intent(BaseActivity.this, User.class);
                                finish();
                                startActivity(intent4);
                                return true;
                            case R.id.nav_logout:
                                fAuth.signOut();
                                Intent intent5 = new Intent(BaseActivity.this, LoginActivity.class);
                                finish();
                                startActivity(intent5);
                                return true;
                        }
                        if(selectedSubMenu != -1) {
                            Intent intent = new Intent(BaseActivity.this, GroupPageActivity.class);
                            intent.putExtra("gTitle", groups.get(menuItem.getItemId()));
                            Log.v("INFO", ""+menuItem.getItemId());
                            startActivity(intent);
                            return true;
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onResume() {
        selectedSubMenu = -1;

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navView.getMenu();
        final SubMenu subMenu = m.addSubMenu("Grupper: ");
        Log.v("INFO", "Drawer options created");
        DatabaseReference membersRef = database.child("members");
        membersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                ArrayList<String> listo = new ArrayList<String>();
                DataSnapshot thisUser = dataSnapshot.child(user.getUid());
                for(DataSnapshot data: thisUser.getChildren()) {
                    Log.v("INFO", "datasnapshot " + data.getKey());
                    listo.add(data.getKey());
                    subMenu.add(0, i, 0, data.getKey());
                    i++;
                }

                selectedSubMenu = i;
                groups = listo;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return super.onCreateOptionsMenu(menu);
    }
}

