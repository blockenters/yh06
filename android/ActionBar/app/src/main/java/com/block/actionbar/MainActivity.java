package com.block.actionbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.block.actionbar.adapter.PostingAdapter;
import com.block.actionbar.model.Posting;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    FloatingActionButton fab;

    // todo 리사이클러뷰는 한번에 설정.
    RecyclerView recyclerView;
    ArrayList<Posting> postingArrayList = new ArrayList<>();
    PostingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);
        // 리사이클러뷰는 한번에 설정
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 설정이 끝나면, 네트워크 통해서 데이터를 받아오고, 화면에 표시한다.
        // 1. request queue 를 만든다.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // 2. request 를 만든다. => 응답 데이터의 JSON 형식을 먼저 확인한다.
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://block1-image-test.s3.ap-northeast-2.amazonaws.com/posting.json",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // parsing
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            // 하나씩 꺼내서 메모리에 객체 생성하고,
                            // 여러개니까 어레이리스트에 저장.
                            // 모든 데이터를 전부 저장하고 나면, 화면에 표시한다.

                            for(int i = 0; i < dataArray.length(); i++){
                               JSONObject data = dataArray.getJSONObject(i);
                               int id = data.getInt("id");
                               int userId = data.getInt("userId");
                               String title = data.getString("title");
                               String body = data.getString("body");
                               Posting posting = new Posting(id, userId, title, body);
                               postingArrayList.add(posting);
                            }

                            adapter = new PostingAdapter(MainActivity.this, postingArrayList);
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,
                                    "파싱 에러",
                                    Toast.LENGTH_SHORT).show();
                            Log.i("ACTION MAIN", e.toString());
                            return;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                "네트워크 통신 에러",
                                Toast.LENGTH_SHORT).show();
                        Log.i("EMPLOYER MAIN", error.toString());
                    }
                }
        );

        // 3. 만든 request 를 queue 에 넣어준다.
        queue.add(request);

    }
}





