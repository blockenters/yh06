package com.block.youtube;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.youtube.model.Video;
import com.bumptech.glide.Glide;


public class PhotoActivity extends AppCompatActivity {

    ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Video video = (Video) getIntent().getSerializableExtra("video");

        imgPhoto = findViewById(R.id.imgPhoto);

        Glide.with(PhotoActivity.this).load(video.url).into(imgPhoto);

    }
}