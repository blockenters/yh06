package com.block.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectContact();
//                openWebPage("https://naver.com");
//                composeSMS("010-2222-3333");
//                composeEmail(new String[]{"abc@naver.com", "bbb@gmail.com"}, "테스트 메일");
                shareText("안녕하세요~~");
            }
        });
    }

    // 연락처 선택하는 액티비티 뛰우기!
    void selectContact(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivity(intent);
    }

    // 웹브라우저 액티비티를 실행시키는 함수
    void openWebPage(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    // SMS 작성하는 액티비티를 실행시키는 함수
    void composeSMS(String phone){
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    // 이메일 작성하는 액티비티를 실행시키는 함수
    void composeEmail(String[] address, String subject){
        Uri uri = Uri.parse("mailto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(uri);
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(intent);
    }

    // 공유버튼 눌러서, 문자열을 공유할 수 있도록 하는 함수
    void shareText(String text){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, "선택하세요");
        startActivity(shareIntent);
    }

}






