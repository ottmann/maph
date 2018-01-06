package com.example.elisabeth.depressionsapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

/**
 * Created by elisabeth on 04.12.17.
 */
public class WifiConnectionManager extends AppCompatActivity {
    private WifiManager mainWifi;
    //mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    private  String ssid;

    public WifiConnectionManager() {

    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return null;
        }

        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }

        return ssid;
    }

    private void setSsid(String newSsid) {
        ssid = newSsid;
    }

    private String getSsid() {
        return ssid;
    }
}
