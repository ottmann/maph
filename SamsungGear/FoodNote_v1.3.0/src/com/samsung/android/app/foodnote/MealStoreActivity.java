/**
 * Copyright (C) 2014 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */

package com.samsung.android.app.foodnote;

import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

public class MealStoreActivity extends Activity {

    @BindView(R.id.saved_food_list) ListView mCachedFoodListView;
    @BindView(R.id.total_calorie) TextView mCalories;

    private HealthDataStore mStore;
    private FoodDataHelper mFoodDataHelper;
    private List<String> mFoodNameArray = new ArrayList<>();
    private ArrayAdapter<String> mNameArrayAdapter;
    private ArrayList<String> mIntakeUuidArray = new ArrayList<>();
    private int mMealType;
    private long mIntakeDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intake day and meal type from Android Intent
        mIntakeDay = getIntent().getExtras().getLong(AppConstants.BUNDLE_KEY_INTAKE_DAY);
        mMealType = getIntent().getExtras().getInt(AppConstants.BUNDLE_KEY_MEAL_TYPE);

        setTitle(AppConstants.getMealTypeName(mMealType));
        setContentView(R.layout.meal_store);
        ButterKnife.bind(this);

        mStore = new HealthDataStore(this, mConnectionListener);
        mStore.connectService();
        mFoodDataHelper = new FoodDataHelper(mStore);

        mNameArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, mFoodNameArray);

        mCachedFoodListView.setAdapter(mNameArrayAdapter);
        mCachedFoodListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {
        @Override
        public void onConnected() {
            Log.d(MainActivity.TAG, "onConnected");

            // Get the cached food intake data
            mFoodDataHelper.readDailyIntakeDetails(mIntakeDay, mMealType, mListener);
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(MainActivity.TAG, "onConnectionFailed");
        }

        @Override
        public void onDisconnected() {
            Log.d(MainActivity.TAG, "onDisconnected");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.meal_store_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        if (item.getItemId() == R.id.delete) {
            int id = mCachedFoodListView.getCheckedItemPosition();
            if (id != ListView.INVALID_POSITION) {
                // Get the foodintake UUID from mFoodNameArray
                String selectedUuid = mIntakeUuidArray.get(id);
                // Delete the selected food by the foodintake UUID
                mFoodDataHelper.deleteFoodIntake(selectedUuid, isSuccess -> {
                    if (isSuccess) {
                        // Read the food intake data after deletion
                        mFoodDataHelper.readDailyIntakeDetails(mIntakeDay, mMealType, mListener);
                    } else {
                        Toast.makeText(MealStoreActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                });
                mCachedFoodListView.clearChoices();
            }
        }

        return true;
    }

    private FoodDataHelper.OnRetrieveMealDetailsListener mListener = (calories, uuidList, foodNameList) -> {
        mIntakeUuidArray.clear();
        mIntakeUuidArray.addAll(uuidList);

        mFoodNameArray.clear();
        mFoodNameArray.addAll(foodNameList);

        mNameArrayAdapter.notifyDataSetChanged();

        mCalories.setText(String.valueOf(calories));
    };
}
