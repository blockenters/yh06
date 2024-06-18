package com.block.postingtabbar;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class PhotoActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // 유저가 누른 이미지의 주소를 받아와야한다.
        String imageUrl = getIntent().getStringExtra("imageUrl");

        imageView = findViewById(R.id.imageView);

        Glide.with(PhotoActivity.this).load(imageUrl).into(imageView);

    }
}