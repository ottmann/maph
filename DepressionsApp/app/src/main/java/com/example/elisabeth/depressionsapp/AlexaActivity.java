package com.example.elisabeth.depressionsapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.elisabeth.depressionsapp.model.DeviceItem;

import java.util.ArrayList;
import java.util.Set;

public class AlexaActivity extends AppCompatActivity {

    public static int REQUEST_BLUETOOTH = 1;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alexa);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        ArrayList<DeviceItem> deviceItemList = new ArrayList<DeviceItem>();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        System.out.println("paired: " + pairedDevices);

        TextView t = (TextView)findViewById(R.id.textView16);

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                DeviceItem newDevice= new DeviceItem(device.getName(),device.getAddress(),"false");
                deviceItemList.add(newDevice);
                System.out.println("paired name: " + device.getName());
                System.out.println("paired name: " + device.getAddress());
                t.setText("1: paired device name: " + device.getName() + " \n paired device address: " + device.getAddress());
            }
        }
    }
}
