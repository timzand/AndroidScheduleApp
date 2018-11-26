package no.hiof.gruppe30.timelisteapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends BaseActivity {

    Button loginn;
    Button register;
    ProgressBar progresss;
    EditText username, password;
    //FireBase
    private FirebaseDatabase fData;
    private FirebaseAuth fAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fData = FirebaseDatabase.getInstance();

        database = fData.getReference();

        loginn = findViewById(R.id.LoginBtn);
        progresss = findViewById(R.id.progressBar);
        register = findViewById(R.id.LoginBtn);
        // init Firebase auth
        fAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_register, null, false);
        mDrawerLayout.addView(contentView, 0);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progresss.setVisibility(view.VISIBLE);
                register.setVisibility(view.INVISIBLE);
                registerU(username.getText().toString(), password.getText().toString());
            }
        });





    }

    private void registerU(final String userN, String passwords) {


        fAuth.createUserWithEmailAndPassword(userN, passwords).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    FirebaseUser user = fAuth.getCurrentUser();
                    database = fData.getReference();

                    //Utvid her, med brukernavn, alder? osv...
                    String email = EncodeString(userN);
                    database.child("users").child(email).child("userid").setValue(user.getUid());
                    database.child("users").child(email).child("name").setValue("John Doe");



                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    // If sign in fails


                }
            }
        });
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }


}
