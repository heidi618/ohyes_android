package com.example.mywifi_demo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mywifi_demo.R;

public class physicInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physic_info);
        Button btn_sensor = (Button) findViewById(R.id.btn_input_sensor);
        Button btn_cert = (Button) findViewById(R.id.btn_input_certificate);
        btn_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(physicInfoActivity.this,inputSeonsorActivity.class);
                startActivity(intent);
            }
        });
        btn_cert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(physicInfoActivity.this, inputCertificate.class);
                startActivity(intent);
            }
        });

    }

}