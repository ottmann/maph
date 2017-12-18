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

import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthConstants.FoodInfo;
import com.samsung.android.sdk.healthdata.HealthConstants.FoodIntake;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.DeleteRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.Filter;
import com.samsung.android.sdk.healthdata.HealthDataResolver.InsertRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthDeviceManager;
import com.samsung.android.sdk.healthdata.HealthResultHolder.BaseResult;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import rx.AsyncEmitter;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class FoodDataHelper {

    private static final String TAG = MainActivity.TAG;

    private static final long ONE_DAY = 24 * 60 * 60 * 1000;
    private static final String ALIAS_SUM_OF_CALORIE = "alias_sum_of_calorie";
    private static final String ALIAS_GROUP_OF_MEAL_TYPE = "alias_group_of_meal_type";

    private final HealthDataStore mStore;

    public FoodDataHelper(@NonNull HealthDataStore store) {
        mStore = store;
    }

    // Return food info uuid
    private Observable<String> insertFoodInfo(String foodName) {

        return Observable.fromAsync(objectAsyncEmitter -> {
            Log.i(TAG, "insertFoodInfo Async, name : " + foodName);
            // Get the FoodInfoTable's key from selected food name to use nutrition informations
            FoodInfoTable.FoodInfo foodInfo = FoodInfoTable.get(foodName);

            HealthData baseData = new HealthData();

            // Fill out the mandatory properties to insert data
            baseData.putString(FoodInfo.PROVIDER_FOOD_ID, foodInfo.providerFoodId);
            baseData.putString(FoodInfo.INFO_PROVIDER, foodInfo.infoProvider);
            baseData.putString(FoodInfo.NAME, foodInfo.name);
            baseData.putFloat(FoodInfo.CALORIE, foodInfo.calorie);
            baseData.putString(FoodInfo.DESCRIPTION, foodInfo.description);
            baseData.putInt(FoodInfo.METRIC_SERVING_AMOUNT, foodInfo.metricServingAmount);
            baseData.putString(FoodInfo.METRIC_SERVING_UNIT, foodInfo.metricServingUnit);
            baseData.putInt(FoodInfo.DEFAULT_NUMBER_OF_SERVING_UNIT, foodInfo.defaultNumberOfServingUnit);
            baseData.putFloat(FoodInfo.TOTAL_FAT, foodInfo.totalFat);
            baseData.putFloat(FoodInfo.SATURATED_FAT, foodInfo.saturatedFat);
            baseData.putFloat(FoodInfo.POLYSATURATED_FAT, foodInfo.polysaturatedFat);
            baseData.putFloat(FoodInfo.MONOSATURATED_FAT, foodInfo.monosaturatedFat);
            baseData.putFloat(FoodInfo.TRANS_FAT, foodInfo.transFat);
            baseData.putFloat(FoodInfo.CARBOHYDRATE, foodInfo.carbohydrate);
            baseData.putFloat(FoodInfo.DIETARY_FIBER, foodInfo.dietaryFiber);
            baseData.putFloat(FoodInfo.SUGAR, foodInfo.sugar);
            baseData.putFloat(FoodInfo.PROTEIN, foodInfo.protein);
            baseData.putFloat(FoodInfo.UNIT_COUNT_PER_CALORIE, foodInfo.unitCountPerCalorie);
            baseData.putFloat(FoodInfo.CHOLESTEROL, foodInfo.cholesterol);
            baseData.putFloat(FoodInfo.SODIUM, foodInfo.soduim);
            baseData.putFloat(FoodInfo.POTASSIUM, foodInfo.potassium);
            baseData.putFloat(FoodInfo.VITAMIN_A, foodInfo.vitaminA);
            baseData.putFloat(FoodInfo.VITAMIN_C, foodInfo.vitaminC);
            baseData.putFloat(FoodInfo.CALCIUM, foodInfo.calcium);
            baseData.putFloat(FoodInfo.IRON, foodInfo.iron);

            // Register the local device with the data if it is not registered
            baseData.setSourceDevice(new HealthDeviceManager(mStore).getLocalDevice().getUuid());

            HealthDataResolver resolver = new HealthDataResolver(mStore, null);
            InsertRequest request = new InsertRequest.Builder().setDataType(FoodInfo.HEALTH_DATA_TYPE).build();
            request.addHealthData(baseData);
            resolver.insert(request).setResultListener(baseResult -> objectAsyncEmitter
                .onNext((baseResult.getStatus() == BaseResult.STATUS_SUCCESSFUL) ? baseData.getUuid() : null));
        },
        AsyncEmitter.BackpressureMode.NONE);
    }

    // Return the result of inserting successfully
    private Observable<Boolean> insertFoodIntake(@NonNull String foodName, float intakeCount, int mealType, long intakeTime,
                                  String uuid) {

        Log.i(TAG, "insertFoodIntake, name : " + foodName + ", uuid : " + uuid);

        if (TextUtils.isEmpty(uuid)) {
            return Observable.just(false);
        }

        HealthData data = new HealthData();
        // Get the FoodInfoTable's key from selected food name to use nutrition informations
        FoodInfoTable.FoodInfo foodInfo = FoodInfoTable.get(foodName);

        // Fill out the mandatory properties to insert data
        data.putFloat(FoodIntake.CALORIE, foodInfo.calorie * intakeCount);
        data.putString(FoodIntake.NAME, foodName);
        data.putFloat(FoodIntake.AMOUNT, intakeCount);
        data.putString(FoodIntake.UNIT, foodInfo.metricServingUnit);
        data.putInt(FoodIntake.MEAL_TYPE, mealType);
        data.putString(FoodIntake.FOOD_INFO_ID, uuid);
        switch (mealType) {
            case HealthConstants.FoodIntake.MEAL_TYPE_BREAKFAST:
                intakeTime += AppConstants.BREAKFAST_TIME;
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_LUNCH:
                intakeTime += AppConstants.LUNCH_TIME;
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_DINNER:
                intakeTime += AppConstants.DINNER_TIME;
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_MORNING_SNACK:
                intakeTime += AppConstants.MORNING_SNACK_TIME;
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_AFTERNOON_SNACK:
                intakeTime += AppConstants.AFTERNOON_SNACK_TIME;
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_EVENING_SNACK:
                intakeTime += AppConstants.EVENING_SNACK_TIME;
                break;
            default:
                break;
        }

        data.putLong(FoodIntake.START_TIME, intakeTime);
        data.putLong(FoodIntake.TIME_OFFSET, TimeZone.getDefault().getOffset(intakeTime));

        // Register the local device with the data if it is not registered
        data.setSourceDevice(new HealthDeviceManager(mStore).getLocalDevice().getUuid());

        HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        InsertRequest request = new InsertRequest.Builder().setDataType(FoodIntake.HEALTH_DATA_TYPE).build();
        request.addHealthData(data);

        return Observable.fromAsync(booleanAsyncEmitter -> {
            resolver.insert(request)
                .setResultListener(baseResult -> Observable.just(baseResult)
                    .map(result -> result.getStatus() == BaseResult.STATUS_SUCCESSFUL)
                    .subscribe(booleanAsyncEmitter::onNext));
        }, AsyncEmitter.BackpressureMode.NONE);
    }

    public void deleteFoodIntake(@NonNull String intakeUuid, @NonNull OnDeletedListener listener) {

        HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Filter filter = Filter.eq(FoodIntake.UUID, intakeUuid);
        HealthDataResolver.DeleteRequest deleteRequest = new DeleteRequest.Builder()
                .setDataType(FoodIntake.HEALTH_DATA_TYPE)
                .setFilter(filter)
                .build();
        try {
            resolver.delete(deleteRequest).setResultListener(baseResult -> listener
                    .onDeleted(baseResult.getStatus() == BaseResult.STATUS_SUCCESSFUL && baseResult.getCount() >= 1));
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Deleting food intake fails.", e);
            listener.onDeleted(false);
        }
    }

    public void readDailyIntakeCalories(long startTime, @NonNull OnRetrieveDailyCaloriesListener listener) {

        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        HealthDataResolver.AggregateRequest request = new HealthDataResolver.AggregateRequest.Builder()
                .setDataType(FoodIntake.HEALTH_DATA_TYPE)
                .addFunction(HealthDataResolver.AggregateRequest.AggregateFunction.SUM, FoodIntake.CALORIE, ALIAS_SUM_OF_CALORIE)
                .setLocalTimeRange(FoodIntake.START_TIME, FoodIntake.TIME_OFFSET, startTime, startTime + ONE_DAY)
                .addGroup(FoodIntake.MEAL_TYPE, ALIAS_GROUP_OF_MEAL_TYPE)
                .build();
        try {
            resolver.aggregate(request).setResultListener(result -> {
                try {
                    SparseArray<Float> calorieMap = new SparseArray<>();

                    for (HealthData data : result) {
                        float calorie = data.getFloat(ALIAS_SUM_OF_CALORIE);
                        int mealType = data.getInt(ALIAS_GROUP_OF_MEAL_TYPE);
                        calorieMap.put(mealType, calorie);
                    }

                    // Show the food intake calories
                    listener.onRetrieved(
                            calorieMap.get(FoodIntake.MEAL_TYPE_BREAKFAST, 0.f),
                            calorieMap.get(FoodIntake.MEAL_TYPE_LUNCH, 0.f),
                            calorieMap.get(FoodIntake.MEAL_TYPE_DINNER, 0.f),
                            calorieMap.get(FoodIntake.MEAL_TYPE_MORNING_SNACK, 0.f),
                            calorieMap.get(FoodIntake.MEAL_TYPE_AFTERNOON_SNACK, 0.f),
                            calorieMap.get(FoodIntake.MEAL_TYPE_EVENING_SNACK, 0.f));

                } finally {
                    result.close();
                }
            });
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Getting daily calories fails.", e);
            listener.onRetrieveFailed();
        }
    }

    public void readDailyIntakeDetails(long startTime, int mealType, @NonNull OnRetrieveMealDetailsListener listener) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        HealthDataResolver.Filter filter = Filter.eq(FoodIntake.MEAL_TYPE, mealType);
        // Read the foodIntake data of specified day and meal type(startTime to end)
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder().setDataType(FoodIntake.HEALTH_DATA_TYPE)
                .setProperties(new String[]{
                    FoodIntake.UUID, FoodIntake.NAME, FoodIntake.CALORIE, FoodIntake.AMOUNT, FoodIntake.PACKAGE_NAME})
                .setLocalTimeRange(FoodIntake.START_TIME, FoodIntake.TIME_OFFSET, startTime, startTime + ONE_DAY)
                .setFilter(filter)
                .build();
        try {
            resolver.read(request).setResultListener(result -> {
                float totalCalories = 0.f;

                try {
                    ArrayList<String> savedUuidList = new ArrayList<>();
                    List<String> foodNameList = new ArrayList<>();

                    for (HealthData data : result) {
                        savedUuidList.add(data.getString(FoodIntake.UUID));
                        // Set the variables to get the food intake details
                        String foodName = data.getString(FoodIntake.NAME);
                        float intakeTimes = data.getFloat(FoodIntake.AMOUNT);
                        float intakeCalories = data.getFloat(FoodIntake.CALORIE);
                        String packageName = data.getString(FoodIntake.PACKAGE_NAME);
                        if (packageName.equals("com.samsung.android.app.foodnote")) {
                            foodNameList.add(foodName + " : " + intakeTimes + " times" + " ("
                                    + intakeCalories + " Cals)");
                        } else {
                            foodNameList.add(foodName + " : " + intakeTimes + " times" + " ("
                                    + intakeCalories + " Cals)"
                                    + " (Not Deletable)");
                        }

                        // Calculate the total calories from food intakes data
                        totalCalories += intakeCalories;
                    }
                    listener.onRetrieved(totalCalories, savedUuidList, foodNameList);

                } finally {
                    result.close();
                }
            });
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "read error!", e);
        }

    }

    public void createFoodData(@NonNull final String foodName, final float intakeCount, final int mealType,
                               final long intakeTime, @NonNull final OnInsertedListener listener) {

        getFoodUuid(foodName).map(uuid -> insertFoodIntake(foodName, intakeCount, mealType, intakeTime, uuid))
            .subscribe(booleanObservable -> booleanObservable
                    .subscribe(listener::onInserted, throwable -> {
                        Log.e(TAG, "Failed to insert data", throwable);
                        listener.onInserted(false);
                    }));
    }

    private Observable<String> getFoodUuid(String foodName) {
        FoodInfoTable.FoodInfo foodInfo = FoodInfoTable.get(foodName);

        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        HealthDataResolver.Filter filter = Filter.and(
                Filter.eq(FoodInfo.NAME, foodName),
                Filter.eq(FoodInfo.PROVIDER_FOOD_ID, foodInfo.providerFoodId));

        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(FoodInfo.HEALTH_DATA_TYPE)
                .setProperties(new String[]{FoodInfo.UUID})
                .setFilter(filter)
                .build();
        return Observable.fromAsync(objectAsyncEmitter -> resolver.read(request)
                .setResultListener(healthDatas -> Observable.from(healthDatas)
                        .take(1)
                        .map(healthData -> healthData.getString(FoodInfo.UUID))
                        .switchIfEmpty(insertFoodInfo(foodName))
                        .subscribe(objectAsyncEmitter::onNext,
                                objectAsyncEmitter::onError)),
                AsyncEmitter.BackpressureMode.NONE);
    }

    @FunctionalInterface
    public interface OnInsertedListener {
        void onInserted(boolean isSuccess);
    }

    @FunctionalInterface
    public interface OnDeletedListener {
        void onDeleted(boolean isSuccess);
    }

    @FunctionalInterface
    public interface OnRetrieveMealDetailsListener {
        void onRetrieved(float calories, List<String> uuidList, List<String> foodNameList);
    }

    public interface OnRetrieveDailyCaloriesListener {
        void onRetrieved(float breakfast, float lunch, float dinner, float morningSnack,
                         float afternoonSnack, float eveningSnack);

        void onRetrieveFailed();
    }
}
