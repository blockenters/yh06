package com.block.memo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.block.memo.api.MemoApi;
import com.block.memo.api.NetworkClient;
import com.block.memo.config.Config;
import com.block.memo.model.Memo;
import com.block.memo.model.Res;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddActivity extends AppCompatActivity {

    EditText editTitle;
    Button btnDate;
    Button btnTime;
    EditText editContent;
    Button btnSave;

    String date = "";
    String time = "";

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

                                String strHour = "";
                                if(hourOfDay < 10){
                                    strHour = "0" + hourOfDay;
                                }else {
                                    strHour = "" + hourOfDay;
                                }

                                String strMin;
                                if(minute < 10){
                                    strMin = "0" + minute;
                                }else{
                                    strMin = "" + minute;
                                }

                                time = strHour + ":" + strMin;

                                btnTime.setText(time);

                            }
                        },
                        current.get(Calendar.HOUR_OF_DAY),
                        current.get(Calendar.MINUTE),
                        true
                );
                dialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                // 필수항목 입력했는지 체크
                if(title.isEmpty() || content.isEmpty() || date.isEmpty() || time.isEmpty()){
                    Snackbar.make(btnSave,
                            "필수항목입니다. 모두 입력하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;

                }

                showProgress();

                // 메모생성 API 실행!
                Retrofit retrofit = NetworkClient.getRetrofitClient(AddActivity.this);
                MemoApi api = retrofit.create(MemoApi.class);

                SharedPreferences sp = getSharedPreferences(Config.SP_NAME, MODE_PRIVATE);

                String token = sp.getString("token", "");

                // 글로벌 서비스에 맞게, UTC로 변환시켜서 서버로 보내야 한다.
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sf.setTimeZone(TimeZone.getDefault());
                df.setTimeZone(TimeZone.getTimeZone("UTC"));

                String utcTime = "";
                try {
                    Date datetime = sf.parse(date+" "+time);
                    utcTime = df.format(datetime);

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                Memo memo = new Memo(title, utcTime, content );

                Call<Res> call = api.addMemo("Bearer "+token, memo);

                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {

                        dismissProgress();

                        if(response.isSuccessful()){

                            finish();

                        }else if(response.code() == 400){

                        }else if(response.code() == 500){

                        }else {

                        }

                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable throwable) {
                        dismissProgress();
                    }
                });

            }
        });
    }

    Dialog dialog;
    void showProgress(){
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(this));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void dismissProgress(){
        dialog.dismiss();
    }

}




