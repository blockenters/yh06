package com.block.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    EditText editPassword2;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editPassword2 = findViewById(R.id.editPassword2);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 입력사항들을 모두 가져온다.
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String password2 = editPassword2.getText().toString().trim();

                // 2. 필수항목 체크
                if( email.isEmpty() || password.isEmpty() || password2.isEmpty() ){
                    Snackbar.make(btnRegister,
                            R.string.register_check1,
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 3. 이메일 형식이 맞는지 체크
                if( email.contains("@") == false ) {
                    Snackbar.make(btnRegister,
                            R.string.register_check2,
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 4. 만약에 비번 길이 제약이 있다면,
                if(password.length() < 4 ||  password.length() > 12 ){
                    Snackbar.make(btnRegister,
                            "비밀번호 길이를 확인하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 5. 비번이 같은지 확인
                if ( password.equals(password2) == false ){
                    Snackbar.make(btnRegister,
                            "비밀번호가 일치하지 않습니다.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 이메일 주소를 앱 내 저장소에 저장하는 방법
                SharedPreferences sp =
                        getSharedPreferences("avata_app", MODE_PRIVATE);
                SharedPreferences.Editor editor =  sp.edit();
                editor.putString("email", email);
                editor.commit();



                // 아바타 액티비티를 띄운다.
                Intent intent = new Intent(MainActivity.this, AvataActivity.class);
//                intent.putExtra("email", email);
                startActivity(intent);

                finish();
            }
        });

    }
}







