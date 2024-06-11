package com.block.simpleimage;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.simpleimage.model.Posting;
import com.bumptech.glide.Glide;

public class PhotoActivity extends AppCompatActivity {

    ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Posting posting = (Posting) getIntent().getSerializableExtra("posting");

        imgPhoto = findViewById(R.id.imgPhoto);

        Glide.with(PhotoActivity.this)
                .load( posting.url )
                .into( imgPhoto );

    }
}






