package com.example.elisabeth.depressionsapp;

/**
 * Created by elisabeth on 28.11.17.
 */
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 9;
        //return mThumbIds.length;
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
    public Integer[] mThumbIds = {  R.drawable.ic_alarm_black_24dp,
                                    R.drawable.ic_assessment_black_24dp,
                                    R.drawable.ic_devices_other_black_24dp,
                                    R.drawable.ic_done_black_24dp,
                                    R.drawable.ic_face_black_24dp,
                                    R.drawable.ic_info_black_24dp,
                                    R.drawable.ic_lightbulb_outline_black_24dp,
                                    R.drawable.ic_menu_camera,
                                    R.drawable.ic_menu_gallery,
                                    R.drawable.ic_menu_manage,
                                    R.drawable.ic_menu_send,
                                    R.drawable.ic_menu_share,
                                    R.drawable.ic_menu_slideshow,
                                    R.drawable.ic_nfc_black_24dp,
                                    R.drawable.ic_notifications_black_24dp,
                                    R.drawable.ic_settings_black_24dp,
                                    R.drawable.ic_speaker_black_24dp,
                                    R.drawable.ic_sync_black_24dp,
                                    R.drawable.ic_timeline_black_24dp,
                                    R.drawable.ic_watch_black_24dp};
}
