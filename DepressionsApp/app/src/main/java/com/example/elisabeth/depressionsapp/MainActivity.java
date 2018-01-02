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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RatingBar;

import com.example.elisabeth.depressionsapp.devices.AlexaActivity;
import com.example.elisabeth.depressionsapp.devices.ArduinoActivity;
import com.example.elisabeth.depressionsapp.devices.HueActivity;
import com.example.elisabeth.depressionsapp.devices.WatchActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnChartGestureListener,
        OnChartValueSelectedListener {

    LineChart mChart;

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
        //fillChart();

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);
    }

    /** Populates the chart on the main page with data */
    /** private void fillChart() {
        mChart = (LineChart) findViewById(R.id.chart_line);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(false);

        // add data
        setData();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        Description description = new Description();
        //description.setText("Demo Line Chart");
        //mChart.setDescription(description);
        //mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setLabelCount(1);
        leftAxis.setAxisMaxValue(6);
        leftAxis.setAxisMinValue(0);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.setVisibleXRangeMaximum(10);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

        //  dont forget to refresh the drawing
        mChart.invalidate();
    }

    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("5");
        xVals.add("4");
        xVals.add("3");
        xVals.add("2");
        xVals.add("1");
        xVals.add("0");

        return xVals;
    }

    private ArrayList<Entry> setYAxisValues(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(0, 5));
        yVals.add(new Entry(1, 0));
        yVals.add(new Entry(2, 1));
        yVals.add(new Entry(3, 3));
        yVals.add(new Entry(4, 2));

        return yVals;
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "Mood test results");

        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mChart.setData(data);

    } */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Three dots menu in action bar
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
            System.out.println("Clicking Hue Activity");
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

    @Override
    public void onChartGestureStart(MotionEvent me,
                                    ChartTouchListener.ChartGesture
                                            lastPerformedGesture) {

        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me,
                                  ChartTouchListener.ChartGesture
                                          lastPerformedGesture) {

        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            // or highlightTouch(null) for callback to onNothingSelected(...)
            mChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2,
                             float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: "
                + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        //Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleXIndex()
        //        + ", high: " + mChart.getHighestVisibleXIndex());

        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin()
                + ", xmax: " + mChart.getXChartMax()
                + ", ymin: " + mChart.getYChartMin()
                + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }


    public void fillGraphView() {

        GraphView graph = (GraphView) findViewById(R.id.graph);

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
}
