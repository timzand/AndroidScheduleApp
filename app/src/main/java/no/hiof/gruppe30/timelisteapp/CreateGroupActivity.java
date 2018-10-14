package no.hiof.gruppe30.timelisteapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupActivity extends BaseActivity {

    private DatabaseReference database;
    private EditText inputGroupTitle;
    private EditText inputGroupDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_create_group, null, false);
        mDrawerLayout.addView(contentView, 0);

        database = FirebaseDatabase.getInstance().getReference();

        inputGroupTitle = findViewById(R.id.input_groupTitle);
        inputGroupDescription = findViewById(R.id.input_groupDescription);

        Button btnRegister = findViewById(R.id.btn_registerGroup);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = inputGroupTitle.getText().toString();
                String description = inputGroupDescription.getText().toString();

                //Push ny gruppe til database
                //Ha en sjekk at gruppetittel ikke eksisterer fra før av????
            }
        });
    }

}
