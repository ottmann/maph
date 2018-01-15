package com.example.elisabeth.depressionsapp.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.elisabeth.depressionsapp.R;

/**
 * Created by elisabeth on 05.01.18.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new VisualisationsDataFragment();
        } else if (position == 1){
            return new VisualisationsResultFragment();
        }
        return null;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.visualisationsActivity_data);
            case 1:
                return mContext.getString(R.string.visualisationsActivity_results);
            default:
                return null;
        }
    }
}