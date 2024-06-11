package com.block.simpleimage;

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
import com.block.simpleimage.adapter.PostingAdapter;
import com.block.simpleimage.model.Posting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<Posting> postingArrayList = new ArrayList<>();
    PostingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // 1. 큐를 만든다.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // 2. request 만든다.
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://block1-image-test.s3.ap-northeast-2.amazonaws.com/photos.json",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for(int i = 0; i < dataArray.length(); i++){
                                JSONObject data = dataArray.getJSONObject(i);
                                int id = data.getInt("id");
                                int albumId = data.getInt("albumId");
                                String title = data.getString("title");
                                String url = data.getString("url");
                                String thumbnailUrl = data.getString("thumbnailUrl");

                                Posting posting = new Posting(id, albumId, title, url, thumbnailUrl);

                                postingArrayList.add(posting);

                            }

                            adapter = new PostingAdapter(MainActivity.this, postingArrayList);

                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,
                                    "파싱 에러",
                                    Toast.LENGTH_SHORT).show();
                            Log.i("EMPLOYER MAIN", e.toString());
                            return;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(MainActivity.this,
                                "네트워크 통신 에러",
                                Toast.LENGTH_SHORT).show();
                        Log.i("EMPLOYER MAIN", error.toString());
                    }
                }
        );
        // 3. 네트워크로 API 호출
        queue.add(request);

    }
}





