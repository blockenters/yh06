package com.block.postingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.block.postingapp.adapter.PostingAdapter;
import com.block.postingapp.api.NetworkClient;
import com.block.postingapp.api.PostingApi;
import com.block.postingapp.config.Config;
import com.block.postingapp.model.Posting;
import com.block.postingapp.model.PostingList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button btnAdd;
    RecyclerView recyclerView;
    ArrayList<Posting> postingArrayList = new ArrayList<>();
    PostingAdapter adapter;

    String token;

    // 페이징 처리에 필요한 멤버 변수들!!
    int offset = 0;
    int limit = 5;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences(Config.SP_NAME, MODE_PRIVATE);
        token = sp.getString("token", "");
        if(token.isEmpty()){
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(lastPosition + 1 == totalCount){

                    if(count == limit){
                        // 데이터를 추가로 요청한다.
                        addNetworkData();
                    }
                }

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


    }

    private void addNetworkData() {

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);

        PostingApi api = retrofit.create(PostingApi.class);

        Call<PostingList> call = api.getPostingList("Bearer " + token, offset, limit );

        call.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){
                    PostingList postingList = response.body();

                    count = postingList.count;

                    offset = offset + count;

                    postingArrayList.addAll( postingList.items );

                    adapter.notifyDataSetChanged();

                }else{

                }
            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 네트워크를 통해서 데이터를 받아온다.
        getNetworkData();

    }

    private void getNetworkData() {

        progressBar.setVisibility(View.VISIBLE);

        postingArrayList.clear();

        offset = 0;

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);

        PostingApi api = retrofit.create(PostingApi.class);

        Call<PostingList> call = api.getPostingList("Bearer "+token, offset, limit);

        call.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){
                    PostingList postingList = response.body();

                    count = postingList.count;

                    offset = offset + count;

                    postingArrayList.addAll( postingList.items );

                    adapter = new PostingAdapter(MainActivity.this, postingArrayList);

                    recyclerView.setAdapter(adapter);

                }else{

                }

            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}







