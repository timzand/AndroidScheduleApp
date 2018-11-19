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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends BaseActivity {

    Button loginn;
    Button register;
    ProgressBar progresss;
    EditText username, password;
    TextView infoa;

    //FireBase
    //FirebaseApp Fapp;
    private FirebaseAuth fAuth;
   // FirebaseDatabase database;
   // DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginn = findViewById(R.id.LoginBtn);
        register = findViewById(R.id.reg);
        progresss = findViewById(R.id.progressBar);

        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        infoa = findViewById(R.id.info1);

        fAuth = FirebaseAuth.getInstance();

        //username.setText(currentUser.getEmail());



        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_login, null, false);
        mDrawerLayout.addView(contentView, 0);

        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progresss.setVisibility(view.VISIBLE);
                //loginn.setVisibility(view.INVISIBLE);
                signIn(username.getText().toString(), password.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent2);
            }
        });


        if(fAuth.getCurrentUser() != null) {
            infoa.setText("Hello " + fAuth.getCurrentUser().getEmail());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    private void signIn(final String userN, String password) {
        fAuth.signInWithEmailAndPassword(userN,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    FirebaseUser user = fAuth.getCurrentUser();
                    infoa.setText("Hello " + user.getEmail());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    // If sign in fails
                    infoa.setText("user not found" );
                    progresss.setVisibility(View.INVISIBLE);                }
            }
        });





        //Fapp.sign

        /*
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userN).exists()){
                    //User login = dataSnapshot.child(userN).getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
    }
}
