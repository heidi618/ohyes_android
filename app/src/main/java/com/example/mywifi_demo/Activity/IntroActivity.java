package com.example.mywifi_demo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.os.Bundle;

import com.example.mywifi_demo.IntroThread;
import com.example.mywifi_demo.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        IntroThread introThread = new IntroThread(handler);
        introThread.start();
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    };
}