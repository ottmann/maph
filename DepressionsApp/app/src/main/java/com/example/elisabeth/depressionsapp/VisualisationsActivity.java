package com.example.elisabeth.depressionsapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;

public class VisualisationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisations);

        fillMoodGraphView();
        fillSleepGraphView();
    }

    /** Populates the mood graph view */
    private void fillMoodGraphView() {
        //New graph view
        GraphView graph = (GraphView) findViewById(R.id.graphVisualisationsMood);
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
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(VisualisationsActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d4.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    /** Populates the mood graph view */
    private void fillSleepGraphView() {
        //New graph view
        GraphView graph = (GraphView) findViewById(R.id.graphVisualisationsSleep);
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
                new DataPoint(d1, 2),
                new DataPoint(d2, 4),
                new DataPoint(d3, 8),
                new DataPoint(d4, 6)
        });

        series.setColor(Color.BLUE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);
        series.setTitle(getString(R.string.visualisationsActivity_sleepLegend));
        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(VisualisationsActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d4.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMinY(2.0);
        graph.getViewport().setMaxY(10.0);
        graph.getViewport().setYAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }
}
