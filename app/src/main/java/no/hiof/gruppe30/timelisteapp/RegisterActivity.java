package no.hiof.gruppe30.timelisteapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class RegisterActivity extends BaseActivity {

    Button loginn;
    Button register;
    ProgressBar progresss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginn = findViewById(R.id.LoginBtn);
        progresss = findViewById(R.id.progressBar);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        mDrawerLayout.addView(contentView, 0);

        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progresss.setVisibility(view.VISIBLE);
                loginn.setVisibility(view.INVISIBLE);
            }
        });





    }
}
