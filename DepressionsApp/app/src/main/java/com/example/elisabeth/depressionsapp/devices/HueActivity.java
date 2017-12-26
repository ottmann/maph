package com.example.elisabeth.depressionsapp.devices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.elisabeth.depressionsapp.R;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLight.PHLightColorMode;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;


/**
 * Created by jottmann on 17.12.17.
 */

public class HueActivity extends AppCompatActivity {
    private PHHueSDK phHueSDK;
    private PHAccessPoint accessPoint;
    private int lightIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue);

        this.lightIndex = 1;
        phHueSDK = PHHueSDK.create();

        accessPoint = new PHAccessPoint();
        accessPoint.setIpAddress("192.168.178.24");
        accessPoint.setUsername("iGctBGnxvNgaXshsdksmfdLulRNFc8C711naVV13");
        phHueSDK.connect(accessPoint);

        //ToDo Validate if the timeout is to short.
        //Wait until bridge is connected with the app
        int timeout_count = 10 * (int)Math.pow(10, 15);
        for(int i = 0; i < timeout_count && !phHueSDK.isAccessPointConnected(accessPoint); i++){}
        Log.i("DepressionsApp", "Is access point connected? " + phHueSDK.isAccessPointConnected(accessPoint));

        PHBridge bridge= phHueSDK.getSelectedBridge();

        int brightness = 0;
        //https://developers.meethue.com/documentation/core-concepts
        int temperature = 153;
        boolean isOn = false;

        if (bridge == null) {
            Toast.makeText(this, "Es konnte keine Verbindung zur Bridge hergestellt werden.", Toast.LENGTH_LONG).show();
            return;
        }

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        PHLight light = allLights.get(lightIndex);

        PHLightState lightState = light.getLastKnownLightState();
        brightness =  lightState.getBrightness();
        temperature = lightState.getCt();
        isOn = lightState.isOn();

        SeekBar brightnessBar = (SeekBar) findViewById(R.id.brightnessBar);
        brightnessBar.setProgress(brightness);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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

        SeekBar temperatureBar = (SeekBar) findViewById(R.id.temperatureBar);
        //ToDo: android:min="153"
        temperatureBar.setProgress(temperature);
        temperatureBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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

        Switch onOffButton = (Switch) findViewById(R.id.onOffSwitch);
        onOffButton.setChecked(isOn);
        onOffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnLightOn();
                }else{
                    turnLightOff();
                }
            }
        });
    }

    public void setBrightness(int brightness) {
        Log.i("DepressionsApp","Set Brightness: " + brightness);
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge == null) {
            Toast.makeText(this, "Es konnte keine Verbindung zur Bridge hergestellt werden.", Toast.LENGTH_LONG).show();
            return;
        }

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Log.i("DepressionsApp", "Number of found hue lights: " + allLights.size());
        if (allLights.size() == 0){
            Toast.makeText(this, "Es wurden keine Hue Lampen gefunden.", Toast.LENGTH_LONG).show();
            return;
        }
        PHLight light = allLights.get(lightIndex);
        PHLightState lightState = new PHLightState();
        lightState.setBrightness(brightness);

        bridge.updateLightState(light, lightState, lightListener);
        lightStateUpdateNotification();
    }

    public void setTemperature(int temperature){
        Log.i("DepressionsApp","Set Temperature: " + temperature);
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if(bridge == null){
            Toast.makeText(this, "Es konnte keine Verbindung zur Bridge hergestellt werden.", Toast.LENGTH_LONG).show();
            return;
        }

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Log.i("DepressionsApp", "Number of found hue lights: " + allLights.size());
        if (allLights.size() == 0){
            Toast.makeText(this, "Es wurden keine Hue Lampen gefunden.", Toast.LENGTH_LONG).show();
            return;
        }

        PHLight light = allLights.get(lightIndex);
        PHLightState lightState = new PHLightState();
        lightState.setCt(temperature);
        bridge.updateLightState(light, lightState, lightListener);
        lightStateUpdateNotification();
    }

    public void lightStateUpdateNotification(){
        Toast.makeText(this, "Die Einstellungen für deine Lampe wurden aktualisiert.", Toast.LENGTH_LONG).show();
    }

    public void turnLightOn(){
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        PHLight light = allLights.get(lightIndex);

        PHLightState lightState = new PHLightState();
        lightState.setOn(true);
        bridge.updateLightState(light, lightState, lightListener);
    }

    public void turnLightOff(){
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        PHLight light = allLights.get(lightIndex);

        PHLightState lightState = new PHLightState();
        lightState.setOn(false);
        bridge.updateLightState(light, lightState, lightListener);
    }

    PHLightListener lightListener = new PHLightListener() {
        @Override
        public void onReceivingLightDetails(PHLight phLight) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> list) {}

        @Override
        public void onSearchComplete() {}

        @Override
        public void onSuccess() {}

        @Override
        public void onError(int i, String s) {
            Log.e("DepressionsApp", "UpdateLightStateError: " + s);
        }

        @Override
        public void onStateUpdate(Map<String, String> map, List<PHHueError> list) {
            lightStateUpdateNotification();
        }
    };

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
