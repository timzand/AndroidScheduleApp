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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    private FirebaseDatabase fData;
    private FirebaseAuth fAuth;
    FirebaseUser user;
    private DatabaseReference database;
    List<String> groupsGlobal;
    int subMenuSelected;



    //https://developer.android.com/training/implementing-navigation/nav-drawer

    protected DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        subMenuSelected = -1;

        groupsGlobal = new ArrayList<String>();

        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();

        database = fData.getReference();
        user = fAuth.getCurrentUser();

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

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch(menuItem.getItemId()) {
                            case R.id.nav_dashboard:
                                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.nav_creategrp:
                                //Endrer activity til opprett gruppe
                                Intent intent2 = new Intent(BaseActivity.this, CreateGroupActivity.class);
                                startActivity(intent2);
                                return true;
                            case R.id.nav_joingrp:
                                //Endre activity til
                            case R.id.nav_3:
                                Intent intent3 = new Intent(BaseActivity.this, LoginActivity.class);
                                startActivity(intent3);
                                return true;
                        }



                        return true;
                    }
                });
    }

    @Override
    protected void onResume() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        if(navView != null && navView.getMenu() != null) {
            //loadUserGroups();
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        if(subMenuSelected != -1) {
            Intent intent = new Intent(getApplicationContext(), GroupPageActivity.class);
            intent.putExtra("subMenuGroup", groupsGlobal.get(subMenuSelected));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        loadUserGroups();
        return super.onCreateOptionsMenu(menu);
    }

    void loadUserGroups() {

        Log.v("INFO", "Drawer options created");
        DatabaseReference membersRef = database.child("members");


        membersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> grp = new ArrayList<String>();
                DataSnapshot thisUser = dataSnapshot.child(user.getUid());
                for(DataSnapshot data: thisUser.getChildren()) {
                    Log.v("INFO", "datasnapshot " + data.getKey());
                    grp.add(data.getKey());
                    Log.v("INFO", "array " + grp.get(grp.size()-1));
                }

                Log.v("INFO", "size " + grp);
                addSubMenuItems(grp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void addSubMenuItems(List<String> groups) {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu navMenu = navView.getMenu();
        SubMenu submenu = navMenu.addSubMenu("Dine Grupper:");
        int i = 0;
        for (String s: groups) {

            Log.v("INFO", "groupsArray: " + groups.get(i));

            final int lel = i;
            submenu.add(i,i, i, groups.get(i)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getApplicationContext(), GroupPageActivity.class);
                    intent.putExtra("subMenuGroup", groupsGlobal.get(lel));
                    startActivity(intent);
                    return true;
                }
            });
            i++;
        }
        submenu.setGroupCheckable(0, true, true);
        groupsGlobal = groups;
    }
}

