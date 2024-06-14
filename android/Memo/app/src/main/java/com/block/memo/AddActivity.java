package com.block.memo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    EditText editTitle;
    Button btnDate;
    Button btnTime;
    EditText editContent;
    Button btnSave;

    String date = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTitle = findViewById(R.id.editTitle);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 오늘 날짜를 가져오는 코드
                Calendar current = Calendar.getInstance();
                // current.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog dialog = new DatePickerDialog(
                        AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // 로직을 짠다.
                                Log.i("MEMO ADD", "년도 : " + year + " 월 : " + month + "  일 : "+ dayOfMonth);

                                // 월 정보는 +1 을 해줘야 한다.
                                // 월 과 일은, 1~9 사이의 숫자로 설정하면,
                                // 한자리 수로 나오기때문에
                                // 두자리로 맞춰주기 위해서 왼쪽에 0을 붙여야 한다.

                                int realMonth = month + 1;
                                String strMonth = "";
                                if(realMonth < 10){
                                    strMonth = "0" + realMonth;
                                }else {
                                    strMonth = "" + realMonth;
                                }

                                String strDay = "";
                                if( dayOfMonth < 10){
                                    strDay = "0" + dayOfMonth;
                                }else{
                                    strDay = "" + dayOfMonth;
                                }

                                date = year + "-" + strMonth + "-" + strDay;

                                Log.i("MEMO ADD", date);

                                btnDate.setText( date );

                            }
                        },
                        current.get(Calendar.YEAR),
                        current.get(Calendar.MONTH),
                        current.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar current = Calendar.getInstance();
//                current.get(Calendar.MINUTE)

                TimePickerDialog dialog = new TimePickerDialog(
                        AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // 유저가 시간을 선택했을때, 로직 작성.
                            }
                        },
                        current.get(Calendar.HOUR_OF_DAY),
                        current.get(Calendar.MINUTE),
                        true
                );
                dialog.show();
            }
        });

    }
}




