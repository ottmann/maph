package com.example.elisabeth.depressionsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmclockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmclock);

        Button btn = (Button) findViewById(R.id.buttonSetAlarm);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toastText = "Du hast einen Alarm für " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute() + " Uhr gestellt";
                Toast.makeText(AlarmclockActivity.this, toastText, Toast.LENGTH_LONG).show();
            }
        });
    }
}
