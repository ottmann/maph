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

package com.samsung.android.app.smartwatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthConstants.FoodInfo;
import com.samsung.android.sdk.healthdata.HealthConstants.FoodIntake;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionResult;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SmartWatchActivity extends Activity {

    public static final String TAG = "Fitness tracker";


    public Button GoToFoodNote;
    public Button GoToStepCounter;
    private HealthDataStore mStore;
    private FoodDataHelper mDataHelper;

    private boolean mIsStoreConnected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_watch);
        GoToFoodNote = (Button)findViewById(R.id.buttonFoodNote);
        GoToStepCounter = (Button)findViewById(R.id.buttonStepCounter);
        GoToFoodNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartWatchActivity.this, FoodNoteActivity.class);
                startActivity(intent);

            }
        });

        GoToStepCounter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               Intent intent = new Intent(SmartWatchActivity.this, StepCounterActivity.class);
                startActivity(intent);

            }
        });

        try {
            HealthDataService healthDataService = new HealthDataService();
            healthDataService.initialize(this);
        } catch (Exception e) {
            Log.e(TAG, "Init fail", e);
        }
        // Create a HealthDataStore instance and set its listener
        mStore = new HealthDataStore(this, mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();

    }

    @Override
    public void onDestroy() {

        mStore.disconnectService();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
    }


    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {
        @Override
        public void onConnected() {
            Log.d(TAG, "onConnected");
            mIsStoreConnected = true;
            if (isPermissionAcquired()) {
                //mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
            } else {
                requestPermission();
            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(TAG, "onConnectionFailed");
            showConnectionFailureDialog(error);
        }

        @Override
        public void onDisconnected() {
            Log.d(TAG, "onDisconnected");
            mIsStoreConnected = false;
        }
    };

    private final HealthDataObserver mObserver = new HealthDataObserver(null) {
        @Override
        public void onChange(String dataTypeName) {
            Log.d(TAG, "onChange");
            if (FoodIntake.HEALTH_DATA_TYPE.equals(dataTypeName)) {
              //  mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
            }
        }
    };

    private final HealthResultHolder.ResultListener<PermissionResult> mPermissionListener =
            new HealthResultHolder.ResultListener<PermissionResult>() {

                @Override
                public void onResult(PermissionResult result) {
                    Map<PermissionKey, Boolean> resultMap = result.getResultMap();
                    // Show a permission alarm and initializes the calories if
                    // permissions are not acquired
                    if (resultMap.values().contains(Boolean.FALSE)) {
                        showPermissionAlarmDialog();
                    } else {
                        // Get the calories of Indexed time and display it
                       // mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
                        // Register an observer to listen changes of the calories
                        HealthDataObserver.addObserver(mStore, FoodIntake.HEALTH_DATA_TYPE, mObserver);
                    }
                }
            };

    private void showPermissionAlarmDialog() {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(SmartWatchActivity.this);
        alert.setTitle(R.string.notice)
                .setMessage(R.string.msg_perm_acquired)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showConnectionFailureDialog(final HealthConnectionErrorResult error) {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if (error.hasResolution()) {
            switch (error.getErrorCode()) {
                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                    alert.setMessage(R.string.msg_req_install);
                    break;
                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                    alert.setMessage(R.string.msg_req_upgrade);
                    break;
                case HealthConnectionErrorResult.PLATFORM_DISABLED:
                    alert.setMessage(R.string.msg_req_enable);
                    break;
                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                    alert.setMessage(R.string.msg_req_agree);
                    break;
                default:
                    alert.setMessage(R.string.msg_req_available);
                    break;
            }
        } else {
            alert.setMessage(R.string.msg_conn_not_available);
        }

        alert.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (error.hasResolution()) {
                error.resolve(SmartWatchActivity.this);
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton(R.string.cancel, null);
        }

        alert.show();
    }



    private boolean isPermissionAcquired() {
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Check whether the permissions that this application needs are acquired
            Map<PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(generatePermissionKeySet());
            return !resultMap.values().contains(Boolean.FALSE);
        } catch (Exception e) {
            Log.e(TAG, "Permission request fails.", e);
        }
        return false;
    }

    private void requestPermission() {
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Show user permission UI for allowing user to change options
            pmsManager.requestPermissions(generatePermissionKeySet(), SmartWatchActivity.this)
                    .setResultListener(mPermissionListener);
        } catch (Exception e) {
            Log.e(TAG, "Permission setting fails.", e);
        }
    }

    private Set<PermissionKey> generatePermissionKeySet() {
        Set<PermissionKey> pmsKeySet = new HashSet<>();

        // Add the read and write permissions to Permission KeySet
        pmsKeySet.add(new PermissionKey(FoodIntake.HEALTH_DATA_TYPE, PermissionType.READ));
        pmsKeySet.add(new PermissionKey(FoodIntake.HEALTH_DATA_TYPE, PermissionType.WRITE));
        pmsKeySet.add(new PermissionKey(FoodInfo.HEALTH_DATA_TYPE, PermissionType.READ));
        pmsKeySet.add(new PermissionKey(FoodInfo.HEALTH_DATA_TYPE, PermissionType.WRITE));
        pmsKeySet.add(new PermissionKey(FoodInfo.HEALTH_DATA_TYPE, PermissionType.READ));
        pmsKeySet.add(new PermissionKey(FoodInfo.HEALTH_DATA_TYPE, PermissionType.WRITE));
        pmsKeySet.add(new PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, PermissionType.READ));
        pmsKeySet.add(new PermissionKey(StepCountReader.STEP_SUMMARY_DATA_TYPE_NAME, PermissionType.READ));
        return pmsKeySet;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        if (item.getItemId() == R.id.connect) {
            requestPermission();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
