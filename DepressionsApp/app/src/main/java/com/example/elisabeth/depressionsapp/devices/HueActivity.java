package com.example.elisabeth.depressionsapp.devices;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elisabeth.depressionsapp.R;
import com.example.elisabeth.depressionsapp.services.MoodLightManager;

import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;


/**
 * Created by jottmann on 17.12.17.
 */

public class HueActivity extends AppCompatActivity {
    private int lightIndex;
    private MoodLightManager lightManager;

    private SeekBar brightnessBar;
    private SeekBar temperatureBar;
    private int temperatureMin;
    private int temperatureMax;
    private Switch onOffButton;
    private Switch autoBrightnessButton;
    private Button connectBridgeButton;
    private TextView connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue);

        this.lightIndex = 0;
        this.lightManager = MoodLightManager.getInstance();

        //https://developers.meethue.com/documentation/core-concepts

        this.temperatureMin = 153;
        this.temperatureMax = 500;

        boolean isOn = false;
        int brightness = 0;
        int temperature = 0;

        if(this.lightManager.isConnected()){
            brightness = lightManager.getBrightness(this.lightIndex);
            temperature = lightManager.getTemperature(this.lightIndex) - temperatureMin;
            PHBridge bridge = lightManager.getBridge();
            List<PHLight> allLights = bridge.getResourceCache().getAllLights();
            PHLight light = allLights.get(lightIndex);
            isOn = light.getLastKnownLightState().isOn();
        }

        brightnessBar = (SeekBar) findViewById(R.id.brightnessBar);
        brightnessBar.setProgress(brightness);
        brightnessBar.setEnabled(this.lightManager.isConnected());
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

        temperatureBar = (SeekBar) findViewById(R.id.temperatureBar);
        //ToDo: android:min="153"
        temperatureBar.setProgress(temperature);
        temperatureBar.setEnabled(this.lightManager.isConnected());
        temperatureBar.setMax(this.temperatureMax-this.temperatureMin);
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
                setTemperature(seekBar.getProgress()+temperatureMin);
            }
        });

        onOffButton = (Switch) findViewById(R.id.onOffSwitch);
        onOffButton.setEnabled(this.lightManager.isConnected());
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

        autoBrightnessButton = (Switch) findViewById(R.id.autoBrightnessSwitch);
        autoBrightnessButton.setChecked(lightManager.getAutoBrightness());
        autoBrightnessButton.setEnabled(this.lightManager.isConnected());
        autoBrightnessButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MoodLightManager lightManager = MoodLightManager.getInstance();
                lightManager.setAutoBrightness(isChecked);
            }
        });

        connectBridgeButton = (Button)findViewById(R.id.connectBridgeButton);
        connectBridgeButton.setEnabled(!this.lightManager.isConnected());
        connectBridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean succeess = connectToBridge();
                connectBridgeButton.setEnabled(!succeess);
            }
        });

        connected = (TextView) findViewById(R.id.connection);
        updateConnectionStateText();
    }

    public void updateConnectionStateText(){
        if(this.lightManager.connect()) {
            connected.setTextColor(Color.GREEN);
            connected.setText("Lampe ist verbunden!");
        }else{
            connected.setTextColor(Color.RED);
            connected.setText("Lampe ist nicht verbunden!");
        }
    }

    public boolean connectToBridge(){
        boolean isConnected = this.lightManager.connect();
        updateConnectionStateText();
        if(isConnected){
            brightnessBar.setEnabled(isConnected);
            brightnessBar.setProgress(this.lightManager.getBrightness(lightIndex));
            temperatureBar.setEnabled(isConnected);
            temperatureBar.setProgress(this.lightManager.getTemperature(lightIndex));
            onOffButton.setEnabled(isConnected);
            onOffButton.setChecked(this.lightManager.isOn());
            autoBrightnessButton.setEnabled(isConnected);
            Toast.makeText(this, "Es wurde eine Verbindung zur Bridge hergestellt.", Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(this, "Es konnte keine Verbindung hergestellt werden.", Toast.LENGTH_LONG).show();
        return isConnected;
    }

    public void setBrightness(int brightness) {
        lightManager.updateBrightness(brightness, lightIndex);
        lightStateUpdateNotification();
    }

    public void setTemperature(int temperature){
        lightManager.updateTemperature(temperature, lightIndex);
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
