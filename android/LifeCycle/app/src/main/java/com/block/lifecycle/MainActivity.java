package com.block.lifecycle;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    EditText editName;
    EditText editAge;

    TextView txtAge;

    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            Log.i("LIFE MAIN", "onActivityResult 실행");
                            // 내가 실행한 액티비티로 부터, 데이터를 받아오는 부분
                            if ( o.getResultCode() == 1000 ){
                                Log.i("LIFE MAIN", "세컨드 액티비티로부터 데이터 받는다.");
                                int age = o.getData().getIntExtra("age", 0);
                                txtAge.setText("10년 후 나이는 "+ age +"살");
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("LIFE MAIN", "onCreate 실행");

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        txtAge = findViewById(R.id.txtAge);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString().trim();
                String strAge = editAge.getText().toString().trim();

                int age = Integer.parseInt(strAge);

                // 다른 액티비티를 실행시키는 코드
                // 인텐트를 만든다.
                // 인텐트란, 어떤 액티비티가 어떤 액티비티를 띄우겠다 라는 (의도)
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                // 액티비티에 데이터를 전달하는 방법
                intent.putExtra("name",  name);
                intent.putExtra("age", age);
                launcher.launch(intent);
//                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("LIFE MAIN", "onStart 실행");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("LIFE MAIN", "onResume 실행");

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("LIFE MAIN", "onPause 실행");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("LIFE MAIN", "onStop 실행");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("LIFE MAIN", "onDestroy 실행");
    }
}