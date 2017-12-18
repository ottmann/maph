package com.example.elisabeth.depressionsapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class VisualisationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisations);

        fillGraphView();
    }

    /** Populates the graph view on the main page with data */
    private void fillGraphView() {
        //New graph view
        GraphView graph = (GraphView) findViewById(R.id.graph2);
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
}
