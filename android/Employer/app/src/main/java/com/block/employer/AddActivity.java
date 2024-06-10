package com.block.employer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.employer.model.Employer;
import com.google.android.material.snackbar.Snackbar;

public class AddActivity extends AppCompatActivity {

    EditText editName;
    EditText editSalary;
    EditText editAge;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editName = findViewById(R.id.editName);
        editSalary = findViewById(R.id.editSalary);
        editAge = findViewById(R.id.editAge);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String strSalary = editSalary.getText().toString().trim();
                String strAge = editAge.getText().toString().trim();

                if(name.isEmpty() || strSalary.isEmpty() || strAge.isEmpty()){
                    Snackbar.make(btnSave,
                            "필수항목 모두 입력하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                int salary = Integer.parseInt(strSalary);
                int age = Integer.parseInt(strAge);

                Employer employer = new Employer(1000, name, salary, age);

                Intent intent = new Intent();
                intent.putExtra("employer", employer);
                setResult(1000, intent);

                Toast.makeText(AddActivity.this,
                        "저장되었습니다...",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}




