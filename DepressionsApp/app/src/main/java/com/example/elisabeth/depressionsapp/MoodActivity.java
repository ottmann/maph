package com.example.elisabeth.depressionsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        // Locate GridView in listview_main.xml
        GridView gridview = (GridView) findViewById(R.id.gridView);

        // Set the ImageAdapter into GridView Adapter
        gridview.setAdapter(new ImageAdapter(this));

        // Capture GridView item click
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Show the item position using toast
                Toast.makeText(MoodActivity.this, "Position " + position,
                        Toast.LENGTH_SHORT).show();

                //Change pictures in grid view
                //Record selected image
            }
        });
    }
}
