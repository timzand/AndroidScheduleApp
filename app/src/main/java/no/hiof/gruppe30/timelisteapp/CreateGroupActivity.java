package no.hiof.gruppe30.timelisteapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static android.widget.Toast.makeText;

public class CreateGroupActivity extends BaseActivity {

    public static final int PIC_CROP = 2;
    private static final int PIC_IMAGE = 1;
    private DatabaseReference database;
    private EditText inputGroupTitle;
    private EditText inputGroupDescription;
    private Bitmap selectedImage;
    private String creator; // navn av skaper av gruppe
    private Date creationDate;
    private FirebaseDatabase fData;
    private FirebaseAuth fAuth;
    FirebaseUser user;
    private String userId;
    String title;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_create_group, null, false);
        mDrawerLayout.addView(contentView, 0);


        fAuth = FirebaseAuth.getInstance();
        fData = FirebaseDatabase.getInstance();

        database = fData.getReference();
        user = fAuth.getCurrentUser();
        creator = user.getEmail();
        userId = user.getUid();

        inputGroupTitle = findViewById(R.id.input_groupTitle);
        inputGroupDescription = findViewById(R.id.input_groupDescription);

        Button btnRegister = findViewById(R.id.btn_registerGroup);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = inputGroupTitle.getText().toString();
                description = inputGroupDescription.getText().toString();

                Log.v("INFO", title + '\n' + description + '\n' + userId + '\n' + creator + '\n');

                //Push ny gruppe til database
                //Ha en sjekk at gruppetittel ikke eksisterer fra før av????
                final DatabaseReference groupRef = database.child("groups");
                groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        if(dataSnapshot.child(title).exists()){
                            exists = true;
                            Log.w("FIND", "CHILD EXISTS");
                        }

                        if(exists) {
                            Toast.makeText(getApplicationContext(),"Gruppenavn er allerede i bruk",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Gruppe opprettet",Toast.LENGTH_LONG).show();
                            groupRef.child(title).child("description").setValue(description);
                            groupRef.child(title).child("user").setValue(userId);
                            database.child("members").child(userId).child(title).setValue(true);
                            Intent intent = new Intent(getApplicationContext(), GroupPageActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("desc", description);
                            selectedImage = Bitmap.createScaledBitmap(selectedImage, 360, 150, true);
                            intent.putExtra("groupImage", selectedImage);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "CreateGroup: Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button btnImagePick = findViewById(R.id.btn_pickImage);
        btnImagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PIC_IMAGE);
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PIC_IMAGE) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView img = findViewById(R.id.image_group);
                img.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Feil ved opplasting", Toast.LENGTH_LONG).show();
            }
        }
    }

    //https://stackoverflow.com/questions/15228812/crop-image-in-android

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
