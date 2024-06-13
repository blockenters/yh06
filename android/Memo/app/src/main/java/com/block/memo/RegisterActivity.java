package com.block.memo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.memo.api.NetworkClient;
import com.block.memo.api.UserApi;
import com.block.memo.model.User;
import com.block.memo.model.UserRes;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    EditText editNickname;
    Button btnRegister;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editNickname = findViewById(R.id.editNickname);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 액티비티 실행
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 유저가 입력한 데이터들을 가져온다.
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String nickname = editNickname.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty() || nickname.isEmpty()){
                    Snackbar.make(btnRegister,
                            "필수항목입니다. 모두입력하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // 2. 이메일 형식이 올바른지 체크
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                if(pattern.matcher(email).matches() == false){
                    Snackbar.make(btnRegister,
                            "이메일 형식을 바르게 작성하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 회원가입 API를 호출한다.

                // 1. 레트로핏 변수 생성 : api 패키지에 NetworkClient.java 파일이 있어야 한다.
                Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterActivity.this);

                // 2. api 패키지에 있는 인터페이스를 클래스 객체로 생성 : api 패키지에 인터페이스가 있어야 한다.
                UserApi api = retrofit.create(UserApi.class);

                // 3. 보낼 데이터를 만든다.
                User user = new User(email, password, nickname);

                // 4. api 함수를 만든다.
                Call<UserRes> call = api.register(user);

                // 5. api 를 호출한다.
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        Log.i("MEMO REGISTER", ""+ response.code());

                        if(response.isSuccessful()){


                        }else{

                        }

                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable throwable) {

                    }
                });

            }
        });

    }
}


