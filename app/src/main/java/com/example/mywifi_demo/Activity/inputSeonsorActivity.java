package com.example.mywifi_demo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mywifi_demo.R;

public class inputSeonsorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_seonsor);
        Button btn_connect = (Button) findViewById(R.id.btn_connect);
        Button btn_save = (Button)findViewById(R.id.btn_save);
        Spinner spinner = (Spinner)findViewById(R.id.spin_sensor);
        final TextView txt_sensor=(TextView)findViewById(R.id.txt_sensor);
        TextView txt_sensing=(TextView)findViewById(R.id.txt_sensing);

        // Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_sensor.setText("입력할 센서 : " + parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}