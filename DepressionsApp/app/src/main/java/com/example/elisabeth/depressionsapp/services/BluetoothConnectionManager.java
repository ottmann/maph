package com.example.elisabeth.depressionsapp.services;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.example.elisabeth.depressionsapp.database.SQLiteHelper;
import com.example.elisabeth.depressionsapp.datamodel.SensorEntry;
import com.example.elisabeth.depressionsapp.interfaces.SensorValueListener;
import com.example.elisabeth.depressionsapp.interfaces.SensorValuesChangedListener;



import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


/**
 * Created by Miriam on 20.01.2018.
 */

public class BluetoothConnectionManager {

    private final static String TAG = BluetoothConnectionManager.class.getSimpleName();

    public static SensorEntry SensorValues = new SensorEntry(18.0, -1,0, "");

    public static boolean isConnected=false;

    public static List<SensorValuesChangedListener>  SVChangedListener =new ArrayList<SensorValuesChangedListener>();

    private static SensorValueManager SVManager = new SensorValueManager();

    public static BluetoothConnectionService BLEService;//=new BluetoothConnectionService();

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

    public static void AddListenerToSensorValuesChanged(SensorValuesChangedListener listener)
    {
        SVChangedListener.add(listener);
    }

    public static  boolean StartConnection() {
        try {

            if (BLEService.connect(ADRESS_DEPPBOX)) {
                if(BLEService.mBluetoothGatt.discoverServices());
                {

                    boolean isDeviceDiscoverd=false;
                    BLEService.isDiscovering=true;
                    while(!isDeviceDiscoverd)
                    {
                        //Log.i(TAG, "found :" + BLEService.mBluetoothGatt.getServices().size() + " Services");

                        if(BLEService.mBluetoothGatt.getServices().size()>0)
                        {

                            Log.i(TAG, "found :" + BLEService.mBluetoothGatt.getServices().size() + " Services");
                            isDeviceDiscoverd=true;
                            BLEService.isDiscovering=false;
                        }
                        else
                        {
                            sleep(5);
                        }
                    }
                }

                List<BluetoothGattService> services =BLEService.getSupportedGattServices();

                if(services.size()!=0)
                {
                    UUID UUIDService=UUID.fromString(HM_10_CONF);
                    //BluetoothGattCharacteristic characteristic=null;
                    for (BluetoothGattService service : services)
                    {
                        if(service.getUuid().toString().equals(HM_10_CONF))
                        {
                            BLEService.setValueChangedListener(SVManager);
                            characteristic=service.getCharacteristic(UUID.fromString(HM_RX_TX));

                        }
                    }
                    if(characteristic!=null)
                    {
                        BLEService.setCharacteristicNotification(characteristic,true); //start Receving Data
                        return true;
                    }
                }

            }
            else
            {
                return false;
            }

        }
        catch(InterruptedException ex)
        {

        }
        catch (Exception ex)
        {
            throw ex;
        }

        return false;
    }

    public static boolean StopConnection()
    {

        BLEService.mBluetoothGatt.disconnect();
        BLEService.mBluetoothGatt.close();
        BLEService.mBluetoothGatt=null;
        //BLEService.disconnect();
        return  true;
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

    public static void setSensorValues(int temperatur,int airquality,int lightvalue )
    {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        SensorValues = new SensorEntry(temperatur,airquality,lightvalue,ts);

        Log.i(TAG, "SensorValues set to : T=" +SensorValues.temperature+" A="+SensorValues.airquality+" L="+SensorValues.lightvalue);
    }

    public static SensorEntry getSensorValues()
    {
        //GenerateRandomSensorValues();
        return SensorValues;
    }


}

