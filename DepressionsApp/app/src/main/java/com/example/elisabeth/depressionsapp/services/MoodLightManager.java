package com.example.elisabeth.depressionsapp.services;

import android.util.Log;
import android.widget.Toast;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;

/**
 * Created by ottmann on 22.01.18.
 */

public class MoodLightManager {
    private final int MAX_BRIGHTNESS_VALUE = 256;
    private PHHueSDK phHueSDK;
    private PHAccessPoint accessPoint;
    private int lightIndex;

    public MoodLightManager(){
        this.lightIndex = 0;
        phHueSDK = PHHueSDK.create();

        accessPoint = new PHAccessPoint();
        accessPoint.setIpAddress("192.168.178.24");
        accessPoint.setUsername("iGctBGnxvNgaXshsdksmfdLulRNFc8C711naVV13");
        this.connect();

    }

    public boolean connect(){
        //ToDo Validate if the timeout is to short.
        //Wait until bridge is connected with the app
        int timeout_count = 10 * (int)Math.pow(10, 25);
        phHueSDK.connect(accessPoint);
        for(int i = 0; i < timeout_count && !phHueSDK.isAccessPointConnected(accessPoint); i++){}
        Log.i("DepressionsApp", "Is access point connected? " + phHueSDK.isAccessPointConnected(accessPoint));
        return phHueSDK.isAccessPointConnected(accessPoint);
    }
    public void updateLightMode(int lightValue){
        Log.d("MoodLightManager", "lightValue: " + lightValue);
        if(!phHueSDK.isAccessPointConnected(accessPoint)) {
            Log.d("MoodlightManager", "not connected");
            connect();
            return;
        }

        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        PHLight light = allLights.get(lightIndex);
        PHLightState lightState = new PHLightState();
        lightState.setBrightness(MAX_BRIGHTNESS_VALUE - lightValue);

        bridge.updateLightState(light, lightState, lightListener);

        Log.d("MoodLightManagaer", lightState.getBrightness().toString());
        bridge.updateLightState(light, lightState, lightListener);
    }

    public void destroy(){
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }
            phHueSDK.disconnect(bridge);
        }
    }

    PHLightListener lightListener = new PHLightListener() {
        @Override
        public void onReceivingLightDetails(PHLight phLight) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> list) {}

        @Override
        public void onSearchComplete() {}

        @Override
        public void onSuccess() {
            //Log.d("LightStateUpdate", "new light mode is defined");
        }

        @Override
        public void onError(int i, String s) {
            Log.e("DepressionsApp", "UpdateLightStateError: " + s);
        }

        @Override
        public void onStateUpdate(Map<String, String> map, List<PHHueError> list) {
            Log.d("LightStateUpdate", "new light state is assumed");
        }
    };
}
