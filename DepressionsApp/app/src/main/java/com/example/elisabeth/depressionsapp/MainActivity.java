package com.example.elisabeth.depressionsapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.elisabeth.depressionsapp.devices.AlexaActivity;
import com.example.elisabeth.depressionsapp.devices.ArduinoActivity;
import com.example.elisabeth.depressionsapp.devices.WatchActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fillGraphView();
    }

    /** Populates the graph view on the main page with data */
    private void fillGraphView() {
        //New graph view
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getLegendRenderer().setVisible(true);

        //Mood graph
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series.setTitle("Mood");
        series.setColor(Color.RED);
        graph.addSeries(series);

        //Sleep path
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 5),
                new DataPoint(1, 2),
                new DataPoint(2, 4),
                new DataPoint(3, 0),
                new DataPoint(4, 1)
        });
        series2.setTitle("Sleep");
        series2.setColor(Color.BLUE);
        graph.addSeries(series2);

        //xx path
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 7),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        series3.setTitle("XY");
        series3.setColor(Color.GREEN);
        graph.addSeries(series3);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
}
