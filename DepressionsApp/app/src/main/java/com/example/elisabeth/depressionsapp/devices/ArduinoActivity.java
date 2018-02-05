package com.example.elisabeth.depressionsapp.devices;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.elisabeth.depressionsapp.R;
import com.example.elisabeth.depressionsapp.database.SQLiteHelper;
import com.example.elisabeth.depressionsapp.datamodel.SensorEntry;
import com.example.elisabeth.depressionsapp.services.BluetoothConnectionManager;
import com.example.elisabeth.depressionsapp.interfaces.SensorValuesChangedListener;
import com.example.elisabeth.depressionsapp.services.BluetoothConnectionService;

import org.w3c.dom.Text;


/**
 * Created by miriam on 17.12.17.
 */

public class ArduinoActivity extends AppCompatActivity implements SensorValuesChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);

        try {
            if(BluetoothConnectionManager.BLEService==null)
                BluetoothConnectionManager.BLEService= new BluetoothConnectionService();

            if (BluetoothConnectionManager.BLEService.mBluetoothManager == null) {
                mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                BluetoothConnectionManager.BLEService.mBluetoothManager = mBluetoothManager;
                BluetoothConnectionManager.BLEService.initialize();

            }
            BluetoothConnectionManager.AddListenerToSensorValuesChanged(this);
            isBLEConnected = BluetoothConnectionManager.isConnected;

            DataBaseConnection = new SQLiteHelper(this);

            SwitchAcitivityConnectedDisconnected();
        }
        catch (Exception ex)
        {
            String err = ex.getMessage();
        }

        /*mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothConnectionManager = new BluetoothConnectionManager();
        mBluetoothConnectionManager.AddListenerToSensorValuesChanged(this);
        mBluetoothConnectionManager.BLEService.mBluetoothManager=mBluetoothManager;
        mBluetoothConnectionManager.BLEService.initialize();*/
    }


    BluetoothManager mBluetoothManager;
    private SQLiteHelper DataBaseConnection;

    boolean isBLEConnected=false;

    public void TryToConnect(View view)
    {
        ConnectOrDisconnect();
    }

    private void ConnectOrDisconnect()
    {
        try {
            if (!isBLEConnected)
            {
                isBLEConnected=BluetoothConnectionManager.StartConnection();
                BluetoothConnectionManager.isConnected=isBLEConnected;
            }
            else if(isBLEConnected)
            {
                BluetoothConnectionManager.StopConnection();
                isBLEConnected = false;
                BluetoothConnectionManager.isConnected=false;
            }
        }
        catch(Exception ex)
        {
        }
        SwitchAcitivityConnectedDisconnected();
    }

    private void SwitchAcitivityConnectedDisconnected()
    {
        try {
            if (isBLEConnected)
            {
                final TextView ConnectionStateTextView = (TextView) findViewById(R.id.textView25);
                ConnectionStateTextView.setText("Verbunden mit Deppenbox");

                final TextView ButtonConnection = (Button) findViewById(R.id.BLEConnectionButton);
                ButtonConnection.setText("Verbindung trennen");


            }
            else if(!isBLEConnected)
            {

                final TextView ConnectionStateTextView = (TextView) findViewById(R.id.textView25);
                ConnectionStateTextView.setText("Verbunden -");

                final TextView ButtonConnection = (Button) findViewById(R.id.BLEConnectionButton);
                ButtonConnection.setText("Verbindung herstellen");

            }
        }
        catch(Exception ex)
        {
            final TextView ConnectionStateTextView = (TextView) findViewById(R.id.textView25);
            ConnectionStateTextView.setText("konnte Nicht Verbunden werden");
        }
    }

    public void UpdateValuesInActivity()
    {
        try {

            Handler refresh = new Handler(Looper.getMainLooper());
            refresh.post(new Runnable() {
                public void run()
                {
                    SensorEntry SensorValues = BluetoothConnectionManager.getSensorValues();
                    DataBaseConnection.addSensorEntry(SensorValues);


                    final TextView TemperaturTextView = (TextView) findViewById(R.id.TemperaturTextView);
                    TemperaturTextView.setText(Double.toString(SensorValues.temperature)+"°C");

                    final TextView AirQualityTextView = (TextView) findViewById(R.id.textView33);
                    if(SensorValues.airquality==-1)
                        AirQualityTextView.setText("100%");
                    else if(SensorValues.airquality==0)
                        AirQualityTextView.setText("0%");
                    else if(SensorValues.airquality==1)
                        AirQualityTextView.setText("40%");
                    else if(SensorValues.airquality==2)
                        AirQualityTextView.setText("80%");
                    else if(SensorValues.airquality==3)
                        AirQualityTextView.setText("100%");
                    else
                        AirQualityTextView.setText("Fehler bei der Messung");

                    final TextView LightValueTextView = (TextView) findViewById(R.id.LichtValueTextView);
                    LightValueTextView.setText(Integer.toString(SensorValues.lightvalue));

                }
            });
        }
        catch(Exception ex)
        {
            String fehler =ex.getMessage();
        }
    }


}

