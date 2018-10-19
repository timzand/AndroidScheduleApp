package no.hiof.gruppe30.timelisteapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.widget.Toast.makeText;

public class CreateGroupActivity extends BaseActivity {

    public static final int PIC_CROP = 2;
    private static final int PIC_IMAGE = 1;
    private DatabaseReference database;
    private EditText inputGroupTitle;
    private EditText inputGroupDescription;
    private Bitmap selectedImage;


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

                Context context = getApplicationContext();
                CharSequence text = "Gruppe opprettet";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


                //Push ny gruppe til database
                //Ha en sjekk at gruppetittel ikke eksisterer fra før av????


                Intent intent = new Intent(getApplicationContext(), GroupPageActivity.class);
                intent.putExtra("title", title);
                Log.d("create", "1");
                intent.putExtra("desc", description);
                Log.d("crea", "2");

                selectedImage = Bitmap.createScaledBitmap(selectedImage, 360, 150, true);

                intent.putExtra("groupImage", selectedImage);
                Log.d("create", "3");
                startActivity(intent);
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
