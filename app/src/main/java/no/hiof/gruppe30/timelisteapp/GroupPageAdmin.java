package no.hiof.gruppe30.timelisteapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class GroupPageAdmin extends AppCompatActivity {

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page_admin);

        Button btnTimeplan = (Button) findViewById(R.id.btn_timeplan);
        Button btnRoller = (Button) findViewById(R.id.btn_roles);

        Bundle bd = getIntent().getExtras();
        bd.get("title");

        btnTimeplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupPageTimeplan.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });

        btnRoller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupPageRoller.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });




    }


}
