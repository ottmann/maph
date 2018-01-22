package com.example.elisabeth.depressionsapp.devices;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.Object;

import java.util.HashMap;

import com.example.elisabeth.depressionsapp.R;
import com.example.elisabeth.depressionsapp.datamodel.SensorEntry;
import com.example.elisabeth.depressionsapp.services.BluetoothConnectionManager;
import com.example.elisabeth.depressionsapp.services.BluetoothConnectionService;

import org.w3c.dom.Text;

public class ArduinoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothConnectionManager.BLEService.mBluetoothManager=mBluetoothManager;
        BluetoothConnectionManager.BLEService.initialize();
    }

    BluetoothManager mBluetoothManager;

    boolean isBLEConnected=false;

    public void TryToConnect(View view)
    {

        try
        {
            BluetoothConnectionManager.StartConnection();
            final TextView ConnectionStateTextView = (TextView) findViewById(R.id.textView25);
            ConnectionStateTextView.setText("Verbunden mit Deppenbox");
        }
        catch(Exception ex)
        {
            final TextView ConnectionStateTextView = (TextView) findViewById(R.id.textView25);
            ConnectionStateTextView.setText("Nicht Verbunden");
        }
    }



}