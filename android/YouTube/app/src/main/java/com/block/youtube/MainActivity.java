package com.block.youtube;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.block.youtube.adapter.VideoAdapter;
import com.block.youtube.config.Config;
import com.block.youtube.model.Video;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    ImageView imgSearch;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<Video> videoArrayList = new ArrayList<>();
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
        // 스크롤 처리를 위한 코드
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 맨 마지막 데이터가 화면에 보이게 되면,
                // 네트워크 통해서 데이터를 추가로 가져오도록 한다.
                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if( lastPosition + 1 == totalCount  ){
                    // 네트워크로부터 데이터를 추가로 받아온다.
                    addNetworkData();
                }
            }
        });

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

    // 추가로 20개씩을 더 호출할때 사용하는 함수.
    private void addNetworkData() {
        progressBar.setVisibility(View.VISIBLE);

        // 유튜브 API 를 호출해서, 데이터를 받아온다.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url = "https://www.googleapis.com/youtube/v3/search?key=" + Config.YOUTUBE_KEY +
                "&part=snippet&maxResults=20&order=date&type=video&q=" + keyword + "&pageToken=" + nextPageToken;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("YOUTUBE MAIN", response.toString());

                        progressBar.setVisibility(View.GONE);

                        try {

                            nextPageToken = response.getString("nextPageToken");

                            JSONArray items = response.getJSONArray("items");
                            for(int i = 0; i < items.length(); i++){
                                JSONObject data = items.getJSONObject(i);
                                JSONObject id = data.getJSONObject("id");
                                String videoId = id.getString("videoId");
                                JSONObject snippet = data.getJSONObject("snippet");
                                String title = snippet.getString("title");
                                String description = snippet.getString("description");
                                JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                                JSONObject medium = thumbnails.getJSONObject("medium");
                                String thumbUrl = medium.getString("url");
                                JSONObject high = thumbnails.getJSONObject("high");
                                String url = high.getString("url");

                                Video video = new Video(videoId, title, description, thumbUrl, url);

                                videoArrayList.add(video);

                            }

                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            // todo 유저한테 알려주고, 로그도 찍고 리턴.
                            return;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // todo 유저한테 알려주고, 로그도 찍고 리턴.
                        Log.i("YOUTUBE MAIN", error.toString());

                        progressBar.setVisibility(View.GONE);
                    }
                }
        );

        queue.add(request);


    }

    // 처음 데이터 20개 가져올때 호출하는 함수.
    private void getNetworkData() {

        progressBar.setVisibility(View.VISIBLE);

        videoArrayList.clear();

        // 유튜브 API 를 호출해서, 데이터를 받아온다.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url = "https://www.googleapis.com/youtube/v3/search?key=" + Config.YOUTUBE_KEY +
                "&part=snippet&maxResults=20&order=date&type=video&q=" + keyword;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("YOUTUBE MAIN", response.toString());

                        progressBar.setVisibility(View.GONE);

                        try {

                            nextPageToken = response.getString("nextPageToken");

                            JSONArray items = response.getJSONArray("items");
                            for(int i = 0; i < items.length(); i++){
                                JSONObject data = items.getJSONObject(i);
                                JSONObject id = data.getJSONObject("id");
                                String videoId = id.getString("videoId");
                                JSONObject snippet = data.getJSONObject("snippet");
                                String title = snippet.getString("title");
                                String description = snippet.getString("description");
                                JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                                JSONObject medium = thumbnails.getJSONObject("medium");
                                String thumbUrl = medium.getString("url");
                                JSONObject high = thumbnails.getJSONObject("high");
                                String url = high.getString("url");

                                Video video = new Video(videoId, title, description, thumbUrl, url);

                                videoArrayList.add(video);

                            }

                            adapter = new VideoAdapter(MainActivity.this, videoArrayList);
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            // todo 유저한테 알려주고, 로그도 찍고 리턴.
                            return;
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // todo 유저한테 알려주고, 로그도 찍고 리턴.
                        Log.i("YOUTUBE MAIN", error.toString());

                        progressBar.setVisibility(View.GONE);
                    }
                }
        );

        queue.add(request);
    }
}




