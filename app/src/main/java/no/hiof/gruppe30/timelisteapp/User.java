package no.hiof.gruppe30.timelisteapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;

import no.hiof.gruppe30.timelisteapp.BaseActivity;

public class User extends BaseActivity {

    private FirebaseAuth fAuth;
    EditText name, pass, email, phone;
    Button cName, cPass, cEmail, cPhone;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);



        //FireBase
        fAuth = FirebaseAuth.getInstance();
         user = fAuth.getCurrentUser();

        //UI
        //name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        //phone = findViewById(R.id.phone);
        //cName = findViewById(R.id.cN);
        cPass = findViewById(R.id.cP);
        cEmail = findViewById(R.id.cE);
        //cPhone = findViewById(R.id.cNumb);

        //Inf
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_user, null, false);
        mDrawerLayout.addView(contentView, 0);

        //set info
        //name.setText((CharSequence) user.getDisplayName());
        pass.setText("*****");
        email.setText(user.getEmail());
        //phone.setText(user.getPhoneNumber());

        cPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.updatePassword(pass.getText().toString());
            }
        }
        );

        cEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String pass;
                pass = "2";
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(),"123456");
                user.reauthenticate(credential);
                user.updateEmail(email.getText().toString());
            }
        });

        /*cPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCredential credential = EmailAuthProvider.getCredential("","");
                user.reauthenticate(credential);
                user.updatePhoneNumber((PhoneAuthCredential) phone.getText());
            }
        });*/

    }


}
