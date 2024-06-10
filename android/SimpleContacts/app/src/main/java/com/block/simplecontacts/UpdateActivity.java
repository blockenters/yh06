package com.block.simplecontacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.simplecontacts.model.Contact;
import com.google.android.material.snackbar.Snackbar;

public class UpdateActivity extends AppCompatActivity {

    EditText editName;
    EditText editPhone;
    Button btnSave;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // 받아오는 데이터가 있다.
        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        index = getIntent().getIntExtra("index", 0);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);

        editName.setText(contact.name);
        editPhone.setText(contact.phone);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 유저가 입력한 내용을 모두 가져온다.
                String name = editName.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();

                // 2. 비어있는 항목이 있는치 체크한다.
                if(name.isEmpty() || phone.isEmpty()){
                    Snackbar.make(btnSave,
                            "필수항목 모두 입력하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // 3. 데이터를 메인 액티비티로 보낸다.
                Contact contact = new Contact(name, phone);
                Intent intent = new Intent();
                intent.putExtra("contact", contact);
                intent.putExtra("index", index);
                setResult(1001, intent);

                // 4. 이 액티비티를 종료한다.
                finish();
            }
        });

    }
}


