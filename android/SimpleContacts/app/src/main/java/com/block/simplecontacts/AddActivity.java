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

public class AddActivity extends AppCompatActivity {

    EditText editName;
    EditText editPhone;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();

                if(name.isEmpty() || phone.isEmpty()){
                    Snackbar.make(btnSave,
                            "필수 항목입니다. 모두 입력하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Contact contact = new Contact(name, phone);

                // 현재 나를 실행시킨 액티비티에 데이터를 돌려주는 코드.
                Intent intent = new Intent();
                intent.putExtra("contact" , contact);
                setResult(1000, intent);
                finish();

            }
        });

    }
}




