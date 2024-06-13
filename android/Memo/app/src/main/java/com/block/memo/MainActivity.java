package com.block.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.block.memo.api.NetworkClient;
import com.block.memo.api.UserApi;
import com.block.memo.config.Config;
import com.block.memo.model.UserRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button btnAdd;
    RecyclerView recyclerView;


    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그인을 한 유저인지 아닌지를 체크한다.
        // 로그인을 안했으면, 로그인 액티비티를 띄우고,
        // 로그인을 했으면, 인증토큰을 가져온다.

        SharedPreferences sp = getSharedPreferences(Config.SP_NAME, MODE_PRIVATE);
        token = sp.getString("token", "");

        if(token.isEmpty()){
            // 로그인 액티비티를 띄운다.
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        progressBar = findViewById(R.id.progressBar);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if( item.getItemId() == R.id.menuLogout ){

            showAlertDialog();

        }


        return super.onOptionsItemSelected(item);
    }

    void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setTitle("로그아웃");
        builder.setMessage("정말 로그아웃 하시겠습니까??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // 로그아웃 API 호출!

                Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);

                UserApi api = retrofit.create(UserApi.class);

                Call<UserRes> call = api.logout("Bearer " + token);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if(response.isSuccessful()){

                           SharedPreferences sp = getSharedPreferences(Config.SP_NAME, MODE_PRIVATE);
                           SharedPreferences.Editor editor = sp.edit();
                           editor.putString("token", "");
                           editor.commit();

                           Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                           startActivity(intent);

                           finish();
                           return;


                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable throwable) {

                    }
                });


            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}





