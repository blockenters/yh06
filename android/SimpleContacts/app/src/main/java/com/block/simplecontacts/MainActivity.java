package com.block.simplecontacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import com.block.simplecontacts.adapter.ContactAdapter;
import com.block.simplecontacts.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;

    // 리사이클러뷰는 항상 같이 사용하는 멤버변수들이 있다.
    // 첫번째로, 여러 데이터를 처리해야 하니까, 어레이 리스트가 있어야 한다.
    // 두번째로, 메모리의 데이터를 화면에 보이게 하는 어댑터가 있어야 한다.
    RecyclerView recyclerView;
    ArrayList<Contact> contactArrayList = new ArrayList<>();
    ContactAdapter adapter;


    // AddActivity 로 부터 데이터를 받아야 하므로 액티비티 리절트 런쳐를 만든다.
    public ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            Log.i("LIFE MAIN", "onActivityResult 실행");
                            // 내가 실행한 액티비티로 부터, 데이터를 받아오는 부분

                            if(o.getResultCode() == 1000){
                                Contact contact = (Contact) o.getData().getSerializableExtra("contact");
                                contactArrayList.add(contact);
                                // 메모리의 데이터가 바뀌면
                                // 리사이클러뷰의 화면에도 데이터를 표시하도록
                                // 어댑터의 함수를 이용해야 한다.
                                adapter.notifyDataSetChanged();
                            } else if(o.getResultCode() == 1001){
                                Contact contact = (Contact) o.getData().getSerializableExtra("contact");
                                int index = o.getData().getIntExtra("index", 0);
                                contactArrayList.set( index , contact );

                                adapter.notifyDataSetChanged();
                            }

                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        // 리사이클러뷰는 생성할때 함께 작성하는 코드가 있다.
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        // 버튼 누르면, 연락처 생성 액티비티로 이동
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddActivity.class);

                launcher.launch(intent);

            }
        });

        // 어댑터를 만들고
        adapter = new ContactAdapter(MainActivity.this, contactArrayList);
        // 리사이클러뷰에 적용시킨다.
        recyclerView.setAdapter(adapter);

    }
}


