package com.example.elisabeth.depressionsapp.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.elisabeth.depressionsapp.R;
import com.example.elisabeth.depressionsapp.helper.ImageAdapter;

/**
 * Created by elisabeth on 28.11.17.
 */
public class SingleItemView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.single_item_view);

        // Get position from intent passed from MainActivity.java
        Intent i = getIntent();

        int position = i.getExtras().getInt("id");

        // Open the Image adapter
        ImageAdapter imageAdapter = new ImageAdapter(this);

        // Locate the ImageView in single_item_view.xml
        ImageView imageView = (ImageView) findViewById(R.id.image);

        // Get image and position from ImageAdapter.java and set into ImageView
        imageView.setImageResource(imageAdapter.mThumbIds[position]);
    }
}
