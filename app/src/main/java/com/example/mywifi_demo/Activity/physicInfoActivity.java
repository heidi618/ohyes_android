package com.example.mywifi_demo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mywifi_demo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class physicInfoActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    TextView tvStepDetector;
    private  int mStepDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physic_info);
        Button btn_sensor = (Button) findViewById(R.id.btn_input_sensor);
        Button btn_cert = (Button) findViewById(R.id.btn_input_certificate);


        btn_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(physicInfoActivity.this,inputSeonsorActivity.class);
                startActivity(i);
            }
        });

        btn_cert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(physicInfoActivity.this, inputCertificate.class);
                startActivity(intent);
            }
        });

        //stepdecetor
        tvStepDetector=(TextView)findViewById(R.id.txt_step);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepDetectorSensor==null){
            Toast.makeText(this,"No Step Detect Seonsor",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,stepDetectorSensor,SensorManager.SENSOR_DELAY_UI);

    }
    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
            if(event.values[0]==1.0f){
                mStepDetector++;
                tvStepDetector.setText("걸음수 : "+String.valueOf(mStepDetector));
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}