package com.example.elisabeth.depressionsapp.services;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;

import com.example.elisabeth.depressionsapp.R;
import com.example.elisabeth.depressionsapp.datamodel.SensorEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.lang.Thread.sleep;


/**
 * Created by Miriam on 20.01.2018.
 */

public class BluetoothConnectionManager {

    private final static String TAG = BluetoothConnectionManager.class.getSimpleName();

    public static SensorEntry SensorValues = new SensorEntry(0, 0,0, "");

    public static String ADRESS_DEPPBOX = "C8:FD:19:87:CD:71";

    private static HashMap<String, String> attributes = new HashMap();
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String HM_10_CONF = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static String HM_RX_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";

    public static BluetoothGattCharacteristic characteristic=null;


    static {
        // Sample Services.
        attributes.put("0000ffe0-0000-1000-8000-00805f9b34fb", "HM 10 Serial");
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put(HM_RX_TX, "RX/TX data");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

    public static BluetoothConnectionService BLEService = new BluetoothConnectionService();

    public static void StartConnection() {
        try {

            if (BLEService.connect(ADRESS_DEPPBOX)) {
                BLEService.mBluetoothGatt.discoverServices();
                List<BluetoothGattService> services =BLEService.getSupportedGattServices();

                if(services.size()!=0)
                {
                    UUID UUIDService=UUID.fromString(HM_10_CONF);
                    //BluetoothGattCharacteristic characteristic=null;
                    for (BluetoothGattService service : services)
                    {
                        if(service.getUuid().toString().equals(HM_10_CONF))
                        {
                            characteristic=service.getCharacteristic(UUID.fromString(HM_RX_TX));
                        }
                    }
                    if(characteristic!=null)
                    {
                        BLEService.setCharacteristicNotification(characteristic,true);

                    }
                }

            }
            else
            {

            }

        }
        catch (Exception ex)
        {
            throw ex;
        }


    }

    //Erzeugt Zufallswerte für die Sensordaten: Temperatur und Helligkeit
    public static void GenerateRandomSensorValues()
    {
        final int StartTemperatur =17;
        final int EndTemperatur =25;

        Random randomizer = new Random();
        randomizer.setSeed(System.currentTimeMillis()/1000);

        double randomdoublevalue =randomizer.nextDouble();
        int randomtemperatur = (int)((EndTemperatur-StartTemperatur+1)*randomdoublevalue+StartTemperatur);
        int randomlight = randomizer.nextInt(250);
        int airquality = 1;

        setSensorValues(randomtemperatur,airquality,randomlight);
    }

    private static void setSensorValues(int temperatur,int airquality,int lightvalue )
    {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        SensorValues = new SensorEntry(temperatur,airquality,lightvalue,ts);
    }

    public static SensorEntry getSensorValues()
    {
        GenerateRandomSensorValues();
        return SensorValues;
    }
}
