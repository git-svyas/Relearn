package com.example.mobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class AboutUs extends AppCompatActivity {

    private ImageView youtubeShareTv;
    private ImageView linkedInShareTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        youtubeShareTv = findViewById(R.id.share_youtube);
        linkedInShareTv = findViewById(R.id.share_linkedin);

        youtubeShareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/c/SurajVT")));
            }
        });

        linkedInShareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/srj347/")));
            }
        });



    }
}