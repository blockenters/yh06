package com.block.employer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    ProgressBar progressBar;

    // 리사이클러뷰는 관련된 멤버변수 2개 더 작성해야 한다.
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AddActivity 를 띄운다.
            }
        });

        // 네트워크로부터 데이터를 받아온다.
        // Volley 라이브러리를 이용한 네트워크 통신

        // 1. request queue 를 만든다.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // 2. request(요청) 를 만든다.
        // 데이터 타입은, 응답의 json 형식을 보고 결정한다.
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://block1-image-test.s3.ap-northeast-2.amazonaws.com/employees.json",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 프로그레스바를 유저의 눈에서 사라지게 하는 코드.
                        progressBar.setVisibility(View.GONE);

                        Log.i("EMPLOYER MAIN", response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 프로그레스바를 유저의 눈에서 사라지게 하는 코드.
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(MainActivity.this,
                                "네트워크 통신 에러",
                                Toast.LENGTH_SHORT).show();
                        Log.i("EMPLOYER MAIN", error.toString());
                    }
                }
        );

        // 3. 네트워크로 보낸다.
        queue.add(request);
    }
}




