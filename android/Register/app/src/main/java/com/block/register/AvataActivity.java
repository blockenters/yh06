package com.block.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class AvataActivity extends AppCompatActivity {

    ImageView imgAvata;
    Button btnRabbit;
    Button btnTurtle;
    Button btnOk;

    // 유저가 아바타를 선택하면, 선택한 아바타가 무엇인지 저장하는 용도
    // 0 : 아무것도 선택 안한것,  1 : 토끼,   2 : 거북이
    int imageType;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avata);

//        email = getIntent().getStringExtra("email");

        imgAvata = findViewById(R.id.imgAvata);
        btnRabbit = findViewById(R.id.btnRabbit);
        btnTurtle = findViewById(R.id.btnTurtle);
        btnOk = findViewById(R.id.btnOk);

        btnRabbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAvata.setImageResource(R.drawable.rabbit);
                imageType = 1;
            }
        });

        btnTurtle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAvata.setImageResource(R.drawable.turtle);
                imageType = 2;
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( imageType == 0 ){
                    Snackbar.make(btnOk,
                            "아바타를 선택하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 알러트 다이얼로그를 띄운다.
                showAlertDialog();
            }
        });

    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AvataActivity.this);
        builder.setCancelable(true);
        builder.setTitle("회원가입 완료");
        builder.setMessage("완료하시겠습니까???");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AvataActivity.this, WelcomeActivity.class );
//                intent.putExtra("email" , email);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


}







