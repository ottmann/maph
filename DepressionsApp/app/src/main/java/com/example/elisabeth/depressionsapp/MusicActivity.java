package com.example.elisabeth.depressionsapp;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
    }

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //if (mBluetoothAdapter == null) {
        // Device does not support Bluetooth
    //}
}
