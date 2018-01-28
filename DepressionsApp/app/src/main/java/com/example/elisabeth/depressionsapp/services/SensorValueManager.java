package com.example.elisabeth.depressionsapp.services;

import com.example.elisabeth.depressionsapp.interfaces.SensorValueListener;
import com.example.elisabeth.depressionsapp.interfaces.SensorValuesChangedListener;

/**
 * Created by Miriam on 28.01.2018.
 */

public class SensorValueManager implements SensorValueListener {

    public String recieveData(String Data)
    {

        String[] result = Data.split(";");
        if(result.length>=2)
        {
            int temperatur = Integer.parseInt(result[0]);
            int airquality = Integer.parseInt(result[1]);
            int lightvalue =Integer.parseInt(result[2]);

            BluetoothConnectionManager.setSensorValues(temperatur,airquality,lightvalue);

            for (SensorValuesChangedListener listener:BluetoothConnectionManager.SVChangedListener) {
                listener.UpdateValuesInActivity();
            }
        }
        return Data;
    }
}
