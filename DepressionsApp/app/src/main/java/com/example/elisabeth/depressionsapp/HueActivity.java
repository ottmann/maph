package com.example.elisabeth.depressionsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;


/**
 * Created by jottmann on 17.12.17.
 */

public class HueActivity extends AppCompatActivity {
    private PHHueSDK phHueSDK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phHueSDK = PHHueSDK.create();

        PHAccessPoint accessPoint = new PHAccessPoint();
        accessPoint.setIpAddress("192.168.178.24");
        accessPoint.setUsername("KkYEUbiyRlFZeyuyampMyLHk0oOSdvR807wi5QYw");
        phHueSDK.connect(accessPoint);
        setContentView(R.layout.activity_hue);

        Log.i("DepressionsApp",  "Is access point connected? " + phHueSDK.isAccessPointConnected(accessPoint));

        PHBridge bridge = phHueSDK.getSelectedBridge();

        int brightness = 254;
        int saturation = 0;
        if(bridge == null){
            Toast.makeText(this, "Es konnte keine Verbindung zur Bridge hergestellt werden.", Toast.LENGTH_LONG).show();
        }else{
            List<PHLight> allLights = bridge.getResourceCache().getAllLights();
            PHLight light = allLights.get(0);

            PHLightState lightState = light.getLastKnownLightState();
            Log.i("DepressionsApp", "getBrightness(): " + lightState.getBrightness().toString());
            Log.i("DepressionsApp", "getSaturation(): " + lightState.getSaturation().toString());
            Log.i("DepressionsApp", "getColorMode(): " + lightState.getColorMode().toString());
        }
        SeekBar brightnessBar =  findViewById(R.id.brightnessBar);
        brightnessBar.setProgress(brightness);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                return;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setBrightness(seekBar.getProgress());
            }
        });

        SeekBar temperatureBar = findViewById(R.id.temperatureBar);
        temperatureBar.setProgress(saturation);
        temperatureBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                return;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setTemperature(seekBar.getProgress());
            }
        });
    }

    public void setBrightness(int brightness){
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if(bridge == null){
            Toast.makeText(this, "Es konnte keine Verbindung zur Bridge hergestellt werden.", Toast.LENGTH_LONG).show();
            return;
        }

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        PHLight light = allLights.get(0);

        PHLightState lightState = phHueSDK.getCurrentLightState();
        lightState.setBrightness(brightness);
        bridge.updateLightState(light, lightState);

    }

    public void setTemperature(int temperature){
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if(bridge == null){
            Toast.makeText(this, "Es konnte keine Verbindung zur Bridge hergestellt werden.", Toast.LENGTH_LONG).show();
            return;
        }

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        PHLight light = allLights.get(0);

        PHLightState lightState = phHueSDK.getCurrentLightState();
        lightState.setSaturation(temperature);
        bridge.updateLightState(light, lightState);
    }


    @Override
    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }

            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }
}
