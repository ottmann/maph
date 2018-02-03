package com.example.elisabeth.depressionsapp.devices.samsung;

import com.samsung.android.sdk.healthdata.HealthConstants;

/**
 * Created by AnnaToshiba2 on 15.01.2018.
 */

public class AppConstants {

    static final String BUNDLE_KEY_INTAKE_DAY = "INDEX_TIME";
    static final String BUNDLE_KEY_MEAL_TYPE = "MEAL_TYPE";
    static final long BREAKFAST_TIME = 8 * 60 * 60 * 1000;
    static final long LUNCH_TIME = 12 * 60 * 60 * 1000;
    static final long DINNER_TIME = 18 * 60 * 60 * 1000;
    static final long MORNING_SNACK_TIME = 10 * 60 * 60 * 1000;
    static final long AFTERNOON_SNACK_TIME = 16 * 60 * 60 * 1000;
    static final long EVENING_SNACK_TIME = 22 * 60 * 60 * 1000;

    public static String getMealTypeName(int mealType) {
        String mealTypeName = null;
        switch (mealType) {
            case HealthConstants.FoodIntake.MEAL_TYPE_BREAKFAST:
                mealTypeName = "Breakfast";
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_LUNCH:
                mealTypeName = "Lunch";
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_DINNER:
                mealTypeName = "Dinner";
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_MORNING_SNACK:
                mealTypeName = "Morning Snack";
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_AFTERNOON_SNACK:
                mealTypeName = "Afternoon Snack";
                break;
            case HealthConstants.FoodIntake.MEAL_TYPE_EVENING_SNACK:
                mealTypeName = "Evening Snack";
                break;
            default:
                mealTypeName = "Unknown";
                break;
        }

        return mealTypeName;
    }
}
