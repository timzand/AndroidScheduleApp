package no.hiof.gruppe30.timelisteapp;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class GroupPageRoller extends AppCompatActivity {

    ArrayList<String> roles;
    ArrayList<String> users;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bd = getIntent().getExtras();
        title = (String) bd.get("title");

        ConstraintLayout layout = new ConstraintLayout(this);
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("one");
        spinnerArray.add("two");
        spinnerArray.add("three");
        spinnerArray.add("four");
        spinnerArray.add("five");

        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);

        layout.addView(spinner);


        setContentView(R.layout.group_page_roller);

        EditText roleName = (EditText) findViewById(R.id.edit_addRole);
        Button btnAddRole = (Button) findViewById(R.id.btn_addRole);

        btnAddRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}