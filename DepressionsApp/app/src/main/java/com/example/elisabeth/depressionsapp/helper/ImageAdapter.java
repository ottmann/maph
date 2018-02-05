package com.example.elisabeth.depressionsapp.helper;

/**
 * Created by elisabeth on 28.11.17.
 */
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.elisabeth.depressionsapp.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // Create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        // if it's not recycled, initialize some attributes
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            // Center crop image
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        // Set images into ImageView
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // References to our images in res > drawable
    public Integer[] mThumbIds = {  R.drawable.happy,
                                    R.drawable.aggressive,
                                    R.drawable.annoyed,
                                    R.drawable.content,
                                    R.drawable.crazy,
                                    R.drawable.emotionless,
                                    R.drawable.frustrated,
                                    R.drawable.sad,
                                    R.drawable.tired
    };
}
