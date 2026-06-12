package com.example.transportbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class activity_Places_Info extends AppCompatActivity {

    String name,imageUrl,description,location,label;
    Double latitude,longitude;
    ImageView imageView;
    TextView nameText,descText,locationText;
    LinearLayout view_getDirections;
    Intent intent,mapIntent;
    Uri geoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_places_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_places_info), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fn_placesInfo();
    }
    private void fn_placesInfo(){
        imageView = findViewById(R.id.info_place_image);
        nameText = findViewById(R.id.info_place_name);
        descText = findViewById(R.id.info_place_desc);
        locationText = findViewById(R.id.info_place_location);
        view_getDirections = findViewById(R.id.view_getDirections);

        intent = getIntent();
        name = intent.getStringExtra("name");
        imageUrl = intent.getStringExtra("imageUrl");
        description = intent.getStringExtra("description");
        location = intent.getStringExtra("location");
        latitude = intent.getDoubleExtra("latitude",0.0);
        longitude = intent.getDoubleExtra("longitude",0.0);

        nameText.setText(name);
        descText.setText(description);
        locationText.setText(location);

        Glide.with(this).load(imageUrl).into(imageView);

        view_getDirections.setOnClickListener(view -> {
            label = nameText.getText().toString();
            geoUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + Uri.encode(label) + ")");
            mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
            startActivity(Intent.createChooser(mapIntent,"Open Maps"));
        });
    }
}