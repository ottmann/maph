package com.example.elisabeth.depressionsapp.devices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.elisabeth.depressionsapp.R;
import com.example.elisabeth.depressionsapp.database.DynamoDB;
import com.example.elisabeth.depressionsapp.datamodel.DeviceItem;
import com.example.elisabeth.depressionsapp.datamodel.DynamoDbEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AlexaActivity extends AppCompatActivity {

    public static int REQUEST_BLUETOOTH = 1;

    private TextView testResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alexa);

        testResult = (TextView)findViewById(R.id.alexa_result_description);

        updateBluetoothStatus();
    }

    private void updateBluetoothStatus() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            System.out.println("paired: " + pairedDevices);

            TextView t = (TextView)findViewById(R.id.textView16);

            ArrayList<DeviceItem> deviceItemList = new ArrayList<DeviceItem>();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    DeviceItem newDevice= new DeviceItem(device.getName(),device.getAddress(),"false");
                    deviceItemList.add(newDevice);
                    System.out.println("paired name: " + device.getName());
                    System.out.println("paired name: " + device.getAddress());
                    t.setText("1: paired device name: " + device.getName() + " \n paired device address: " + device.getAddress());
                }
            }
        } else {
            Log.i("AlexaActivity", "No bluetooth found");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTestResult();
    }

    private void updateTestResult() {
        Runnable runnable = new Runnable() {
            public void run() {
                DynamoDB db = new DynamoDB();
                List<DynamoDbEntry> entries = db.getEntries();

                if (entries.isEmpty()) {
                    testResult.setText("Keine Testergebnisse vorhanden");
                    return;
                }

                final DynamoDbEntry first = entries.get(0);

                runOnUiThread(new Runnable() {
                    public void run() {
                        if (first.getScore() / first.getTimesDone() > 13) {
                            testResult.setText("Dein Ergebnis spricht für ein gutes Wohlbefinden.");
                        } else {
                            testResult.setText("Eine behandlungsbedürftige Überlastung, ein Burnout oder eine Depression können bei deinem Ergebnis nicht sicher ausgeschlossen werden. Dies ist aber noch keine Diagnose, hierfür solltest du eine gezielte Diagnostik durchführen lassen.");
                        }
                    }
                });
            }
        };
        Thread dynamoScan = new Thread(runnable);
        dynamoScan.start();
    }
}