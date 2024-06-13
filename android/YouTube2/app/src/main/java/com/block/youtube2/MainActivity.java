package com.block.youtube2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.block.youtube2.adapter.VideoAdapter;
import com.block.youtube2.api.NetworkClient;
import com.block.youtube2.api.VideoApi;
import com.block.youtube2.config.Config;
import com.block.youtube2.model.Item;
import com.block.youtube2.model.VideoList;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    ImageView imgSearch;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<Item> videoArrayList = new ArrayList<>();
    VideoAdapter adapter;


    String keyword;
    String nextPageToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        progressBar.setVisibility(View.GONE);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                keyword = editSearch.getText().toString().trim();

                if(keyword.isEmpty()){
                    Snackbar.make(imgSearch,
                            "검색어를 입력하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // 네크워크로 API 를 호출한다.
                getNetworkData();
            }
        });

    }

    private void getNetworkData() {

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);

        VideoApi api = retrofit.create(VideoApi.class);

        // API 를 만들고
        Call<VideoList> call = api.searchVideo(Config.YOUTUBE_KEY,
                                            "snippet",
                                            20,
                                            "date",
                                            "video",
                                                keyword );
        // 네트워크로 호출한다.
        call.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                // 네트워크 통신 성공
                progressBar.setVisibility(View.GONE);

                // 200 OK 일때 코드.
                if(response.isSuccessful()){
                    // 응답하는 JSON 은 body 에 들어있으므로 body() 함수 호출한다.
                    // 레트로핏 라이브러리는, JSON을 자바의 클래스로 바로 담아준다.
                    // 따라서 파싱할 필요가 없다.
                    VideoList videoList = response.body();

                    nextPageToken = videoList.nextPageToken;

                    videoArrayList.addAll( videoList.items );

                    adapter = new VideoAdapter(MainActivity.this, videoArrayList);

                    recyclerView.setAdapter(adapter);


                }else if(response.code() == 400){
                    // response.code() 는 HTTP 상태코드가 들어있다.

                }else if(response.code() == 500){

                }else {

                }
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable throwable) {
                // 네트워크 통신 실패
                progressBar.setVisibility(View.GONE);
                // 유저한테 알려주고, 로그찍고 리턴.
            }
        });

    }


}