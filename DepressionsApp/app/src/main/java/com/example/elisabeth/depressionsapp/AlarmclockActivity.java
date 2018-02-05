package com.example.elisabeth.depressionsapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.elisabeth.depressionsapp.services.AlarmReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by elisabeth on 28.11.17.
 */

public class AlarmclockActivity extends AppCompatActivity {

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    Button button;
    TextView alarmTv;
    TextView resultTv;
    EditText intervalTv;
    int interval;
    String formattedTime;

    String recommendation1, recommendation2, recommendation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmclock);

        button = (Button) findViewById(R.id.buttonSetAlarmTime);
        alarmTv = (TextView) findViewById(R.id.textView50);
        resultTv = (TextView) findViewById(R.id.textView49);
        intervalTv = (EditText) findViewById(R.id.editText3);

        final Date time = Calendar.getInstance().getTime();
        alarmTv.setText(time.getHours()+ ":" +time.getMinutes());

        giveSleepTimeRecommendation();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                Date currentTime = Calendar.getInstance().getTime();
                TimePickerDialog Tp = new TimePickerDialog(AlarmclockActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        alarmTv.setText(hour+ ":" +minute);

                        if (!TextUtils.isEmpty(intervalTv.getText())) {
                            interval = Integer.parseInt(intervalTv.getText().toString());
                        } else {
                            interval = 0;
                        }

                        giveSleepTimeRecommendation(hour, minute, interval);

                    }
                }, currentTime.getHours(), currentTime.getMinutes(), true);
                Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Tp.show();
            }
        });

        intervalTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    giveSleepTimeRecommendation();
                }
                return false;
            }
        });
    }

    public void giveSleepTimeRecommendation() {
        String timeSet = (String) alarmTv.getText();
        String[] split = timeSet.split(":");
        String hourString = split[0];
        String minuteString = split[1];

        int hour = Integer.parseInt(hourString);
        int minute = Integer.parseInt(minuteString);

        if (!TextUtils.isEmpty(intervalTv.getText())) {
            interval = Integer.parseInt(intervalTv.getText().toString());

        } else {
            interval = 0;
        }
        giveSleepTimeRecommendation(hour, minute, interval);
    }

    public void giveSleepTimeRecommendation(int chosenHour, int chosenMinute, int interval) {

        //wake-up time in 4 sleep cycles
        recommendation1 = "8:00";
        //wake-up time in 5 sleep cycles
        recommendation2 = "9:30";
        //wake-up time in 6 sleep cycles
        recommendation3 = "11:00";

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();

        //set calender to time provided by user
        calendar.set(Calendar.HOUR_OF_DAY, chosenHour);
        calendar.set(Calendar.MINUTE, chosenMinute);
        //add the time the user needs to fall asleep
        calendar.add(Calendar.MINUTE, interval);

        //add 4 sleep cycles
        calendar.add(Calendar.MINUTE, 4*90);
        formattedTime = dateFormat.format(calendar.getTime());
        recommendation1 = formattedTime;

        //add the 5th sleep cycle
        calendar.add(Calendar.MINUTE, 90);
        formattedTime = dateFormat.format(calendar.getTime());
        recommendation2 = formattedTime;

        //add the 6th sleep cycle
        calendar.add(Calendar.MINUTE, 90);
        formattedTime = dateFormat.format(calendar.getTime());
        recommendation3 = formattedTime;

        //set tv to display the 3 recommended wake-up times
        resultTv.setText(recommendation1 + ", " + recommendation2 + " " + getString(R.string.alarmclockActivity_or) + " " + recommendation3);
    }

    //previously used to set an alarm.
    //TODO: add in 'set alarm' functionality again
    public void OnToggleClicked(View view)
    {
        long time;
        if (((ToggleButton) view).isChecked())
        {
            Toast.makeText(AlarmclockActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent intent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
            if(System.currentTimeMillis()>time)
            {
                if (calendar.AM_PM == 0)
                    time = time + (1000*60*60*12);
                else
                    time = time + (1000*60*60*24);
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
        }
        else
        {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(AlarmclockActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
        }
    }
}

