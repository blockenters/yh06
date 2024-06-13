package com.block.memo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.memo.api.NetworkClient;
import com.block.memo.api.UserApi;
import com.block.memo.config.Config;
import com.block.memo.model.User;
import com.block.memo.model.UserRes;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 액티비티 실행
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Snackbar.make(btnLogin,
                            "필수항목입니다. 모두 입력하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 이메일 형식 체크 (회원가입의 코드 C&P )

                // 로그인 API 호출!

                // 0. 다이얼로그 표시한다.
                showProgress();

                // 1. 레트로핏 변수 만든다.
                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);

                // 2. API 객체 생성한다.
                UserApi api = retrofit.create(UserApi.class);

                // 3. 보낼 데이터를 만든다.
                User user = new User(email, password);

                // 4. api 함수를 만든다.
                Call<UserRes> call = api.login(user);

                // 5. 네트워크로 호출한다.
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {

                        Log.i("MEMO LOGIN", ""+response.code());

                        dismissProgress();

                        if(response.isSuccessful()){

                            UserRes userRes = response.body();

                            SharedPreferences sp = getSharedPreferences(Config.SP_NAME, MODE_PRIVATE);

                            SharedPreferences.Editor editor = sp.edit();

                            Log.i("MEMO LOGIN", userRes.accessToken);

                            editor.putString("token", userRes.accessToken);

                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                            return;

                        }else{

                        }

                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable throwable) {
                        dismissProgress();
                        // 유저에게 알리고 로그남기고 리턴.
                    }
                });

            }
        });

    }

    // 서버에 데이터를 저장하거나, 수정하거나, 삭제하는 경우에 사용한다!
    Dialog dialog;
    void showProgress(){
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(this));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void dismissProgress(){
        dialog.dismiss();
    }

}



