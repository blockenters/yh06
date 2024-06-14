package com.block.memo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.memo.model.Memo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UpdateActivity extends AppCompatActivity {

    EditText editTitle;
    Button btnDate;
    Button btnTime;
    EditText editContent;
    Button btnSave;

    String date;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Memo memo = (Memo) getIntent().getSerializableExtra("memo");

        editTitle = findViewById(R.id.editTitle);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        editTitle.setText( memo.getTitle() );

        Log.i("MEMO UPDATE", memo.getDate());

        // UTC 를 Local 로 변환해야 하고,
        // 년월일과 시분초를 분리해서
        // btnDate 와 btnTime 글자 적용한다.

        // 2024-06-15T13:35:00

        // UTC 를 로컬타임으로 변환
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getDefault());

        String localTime = "";
        try {
            Date date = sf.parse( memo.getDate() );
            localTime  = df.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        date = localTime.split(" ")[0];
        time = localTime.split(" ")[1];

        btnDate.setText(date);
        btnTime.setText(time);

        editContent.setText( memo.getContent() );

    }
}



