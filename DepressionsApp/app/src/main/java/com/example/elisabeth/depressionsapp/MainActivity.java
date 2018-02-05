package com.example.elisabeth.depressionsapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.regions.Regions;
import com.example.elisabeth.depressionsapp.database.DatabaseManager;
import com.example.elisabeth.depressionsapp.datamodel.MoodEntry;
import com.example.elisabeth.depressionsapp.datamodel.SensorEntry;
import com.example.elisabeth.depressionsapp.devices.AlexaActivity;
import com.example.elisabeth.depressionsapp.devices.ArduinoActivity;
import com.example.elisabeth.depressionsapp.devices.HueActivity;
import com.example.elisabeth.depressionsapp.devices.WatchActivity;

import com.example.elisabeth.depressionsapp.interfaces.SensorValuesChangedListener;
import com.example.elisabeth.depressionsapp.services.MoodLightManager;
import com.example.elisabeth.depressionsapp.services.BluetoothConnectionManager;
import com.example.elisabeth.depressionsapp.services.WifiConnectionManager;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorValuesChangedListener {
    private int mInterval = 5000;
    private Handler mHandler;
    private MoodLightManager lightManager;
    public boolean IS_CONNECTED_TO_HOME_WIFI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseManager.systemSync(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        String wifi = WifiConnectionManager.getCurrentSsid(getApplicationContext());
        System.out.println("WIFI: " + wifi);

        if (wifi != null) {
            //Set our home wifi as eduroam/uni network
            if (wifi.equals("eduroam")) {
                IS_CONNECTED_TO_HOME_WIFI = true;
            }
        }

        List<MoodEntry> moodList = DatabaseManager.getAllMoods();
        System.out.println("MOODLIST: " + moodList);

        fillGraphView();

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);

        initializeAwsCredentials();

        UpdateValuesInActivity();
        BluetoothConnectionManager.AddListenerToSensorValuesChanged(this);
        
        lightManager = MoodLightManager.getInstance();
        mHandler = new Handler();
        startSensoreUpdateTask();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopSensoreUpdateTask();
        lightManager.destroy();
    }

    //credentials needed for AWS to work
    private void initializeAwsCredentials() {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:827567bb-aa00-4cfe-8bc9-61c06b0fc123", // Identity pool ID
                Regions.US_EAST_1 // Region
        );
        AWSMobileClient.getInstance().setCredentialsProvider(credentialsProvider);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Add nav items to side nav */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mood) {
            // Change to set mood activity
            Intent i = new Intent(getBaseContext(), MoodActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_visualizations) {
            // Change to get visualisations activity
            Intent i = new Intent(getBaseContext(), VisualisationsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_alarmclock) {
            // Change to get visualisations activity
            Intent i = new Intent(getBaseContext(), AlarmclockActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_music) {
            // Change to get visualisations activity
            Intent i = new Intent(getBaseContext(), MusicActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_settings) {
            // Change to settings activity
            Intent i = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.devices_watch) {
            // Change to watch settings activity
            Intent i = new Intent(getBaseContext(), WatchActivity.class);
            startActivity(i);
        } else if (id == R.id.devices_alexa) {
            // Change to alexa settings activity
            Intent i = new Intent(getBaseContext(), AlexaActivity.class);
            startActivity(i);
        } else if (id == R.id.devices_hue) {
            // Change to hue activity
            Intent i = new Intent(getBaseContext(), HueActivity.class);
            startActivity(i);
        } else if (id == R.id.devices_arduino) {
            // Change to arduino settings activity
            Intent i = new Intent(getBaseContext(), ArduinoActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void fillGraphView() {

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getLegendRenderer().setVisible(true);

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 3),
                new DataPoint(d3, 2),
                new DataPoint(d4, 5)
        });

        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);
        series.setTitle(getString(R.string.visualisationsActivity_moodLegend));
        graph.addSeries(series);

    // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(MainActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

    // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d4.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

    // as we use dates as labels, the human rounding to nice readable numbers
    // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    //Reciever for new Sensor Values should Update the Values of MainActivity
    public void UpdateValuesInActivity()
    {
        try {

            Handler refresh = new Handler(Looper.getMainLooper());
            refresh.post(new Runnable() {
                public void run()
                {
                    SensorEntry SensorValues = BluetoothConnectionManager.getSensorValues();
                    TextView TemperaturTextView = (TextView) findViewById(R.id.textView9);
                    TemperaturTextView.setText(Double.toString(SensorValues.temperature)+"°C");

                    final TextView AirQualityTextView = (TextView) findViewById(R.id.textView8);
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
                }
            });
        }
        catch(Exception ex)
        {
            String fehler =ex.getMessage();
        }
    }

    private void startSensoreUpdateTask(){
        mUpdateSensoreValues.run();
    }

    private void stopSensoreUpdateTask(){
        mHandler.removeCallbacks(mUpdateSensoreValues);
    }

    Runnable mUpdateSensoreValues = new Runnable() {

        private SensorEntry sensorEntry = new SensorEntry();
        @Override
        public void run() {
            try {
                MoodLightManager lightManager = MoodLightManager.getInstance();

                if(lightManager.getAutoBrightness()) {
                    SensorEntry newSensoreEntry = BluetoothConnectionManager.getSensorValues();

                    //Make no sense. light brightness should change,
                    if (newSensoreEntry.getLightValue() < sensorEntry.getLightValue() * 0.8 ||
                            newSensoreEntry.getLightValue() > sensorEntry.getLightValue() * 1.2) {
                        sensorEntry = newSensoreEntry;
                        lightManager.updateLightMode(sensorEntry.getLightValue());
                    }
                }
            } finally {
                mHandler.postDelayed(mUpdateSensoreValues, mInterval);
            }
        }
    };
}
