package no.hiof.gruppe30.timelisteapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GroupPageActivity extends BaseActivity {

    private TextView title;
    private ImageView image;
    private TextView description;

    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.group_page_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        title = findViewById(R.id.text_title);
        Log.d("create", "1");
        image = findViewById(R.id.image);
        Log.d("create", "2");
        description = findViewById(R.id.text_desc);
        Log.d("create", "3");

        String bTitle = "default";
        String bDesc = "default";
        Bitmap bImage;

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(!(bd.get("title").equals(null))){
            bTitle = (String) bd.get("title");
            title.setText(bTitle);
        }
        if(!(bd.get("desc").equals(null))){
            bDesc = (String) bd.get("desc");
            description.setText(bDesc);
        }
        if(bd.get("groupImage") != null){
            bImage = (Bitmap) bd.getParcelable("groupImage");
            image.setImageBitmap(bImage);
        }



    }
}
