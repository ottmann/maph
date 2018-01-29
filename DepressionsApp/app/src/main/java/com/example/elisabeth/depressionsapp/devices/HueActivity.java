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
import com.example.elisabeth.depressionsapp.services.MoodLightManager;
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
    private int lightIndex;
    private MoodLightManager lightManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue);

        this.lightIndex = 1;
         lightManager = MoodLightManager.getInstance();

        int brightness = lightManager.getBrightnes(0);
        //https://developers.meethue.com/documentation/core-concepts
        int temperature = 153;
        boolean isOn = true;


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
        lightManager.updateBrightness(brightness, lightIndex);
        lightStateUpdateNotification();
    }

    public void setTemperature(int temperature){
        lightManager.updateTemeprature(temperature, lightIndex);
        lightStateUpdateNotification();
    }

    public void lightStateUpdateNotification(){
        Toast.makeText(this, "Die Einstellungen für deine Lampe wurden aktualisiert.", Toast.LENGTH_LONG).show();
    }

    public void turnLightOn(){
        PHBridge bridge = lightManager.getBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        PHLight light = allLights.get(lightIndex);

        PHLightState lightState = new PHLightState();
        lightState.setOn(true);
        bridge.updateLightState(light, lightState, lightManager.getLightListener());
    }

    public void turnLightOff(){
        PHBridge bridge = lightManager.getBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        PHLight light = allLights.get(lightIndex);

        PHLightState lightState = new PHLightState();
        lightState.setOn(false);

        bridge.updateLightState(light, lightState, lightManager.getLightListener());
    }

}
