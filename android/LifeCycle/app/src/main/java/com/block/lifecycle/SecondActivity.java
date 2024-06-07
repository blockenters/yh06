package com.block.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtAge;

    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.i("LIFE MAIN", "두번째 액티비티 onCreate 실행");

        // 데이터가 넘어온게 있으면, 데이터를 받아준다.
        String name = getIntent().getStringExtra("name");
        age = getIntent().getIntExtra("age", 0);

        age = age + 10;

        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);

        txtName.setText("이름은 " + name );
        txtAge.setText("10년 후 나이는 " + age + "세");

        // 백버튼 눌렀을때 동작하는 코드 작성.
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent();
                intent.putExtra("age" , age);
                setResult(1000, intent);
                finish();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LIFE MAIN", "두번째 액티비티 onStart 실행");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("LIFE MAIN", "두번째 액티비티 onResume 실행");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LIFE MAIN", "두번째 액티비티 onPause 실행");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LIFE MAIN", "두번째 액티비티 onStop 실행");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LIFE MAIN", "두번째 액티비티 onDestroy 실행");


    }
}






