package com.block.employer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.block.employer.adapter.EmployerAdapter;
import com.block.employer.model.Employer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    ProgressBar progressBar;

    // 리사이클러뷰는 관련된 멤버변수 2개 더 작성해야 한다.
    RecyclerView recyclerView;
    ArrayList<Employer> employerArrayList = new ArrayList<>();
    EmployerAdapter adapter;

    public ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            Log.i("LIFE MAIN", "onActivityResult 실행");
                            // 내가 실행한 액티비티로 부터, 데이터를 받아오는 부분

                            if(o.getResultCode() == 1000){
                               Employer employer = (Employer) o.getData().getSerializableExtra("employer");
                               employerArrayList.add(0, employer);
                               adapter.notifyDataSetChanged();
                            }

                        }
                    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBar);
        // 다음 코드는 쌍으로 무조건 같이 작성.
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AddActivity 를 띄운다.
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                launcher.launch(intent);
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

                        try {
                            String status = response.getString("status");
                            if(status.equals("success") == false){
                                Toast.makeText(MainActivity.this,
                                        "네트워크 에러",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray data = response.getJSONArray("data");

                            for(int i = 0; i < data.length(); i++){
                                JSONObject row = data.getJSONObject(i);
                                int id = row.getInt("id");
                                String name = row.getString("employee_name");
                                int salary = row.getInt("employee_salary");
                                int age = row.getInt("employee_age");

                                Employer employer = new Employer(id, name, salary, age);
                                employerArrayList.add(employer);
                            }

                            // 어댑터를 만들고
                            adapter = new EmployerAdapter(MainActivity.this, employerArrayList);
                            // 리사이클러뷰에 어댑터를 적용하면 화면에 나온다.
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,
                                    "파싱 에러",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

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




