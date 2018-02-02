package com.example.elisabeth.depressionsapp.devices.samsung;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elisabeth.depressionsapp.R;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class ChooseFoodActivity extends AppCompatActivity {

    @BindView(R.id.choose_food_list)
    ListView mNameListView;

    private HealthDataStore mStore;
    private FoodDataHelper mFoodDataHelper;
    private boolean mIsServiceAvailable;
    private int mMealType;
    private long mIntakeDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the intakes day and meal type from Android Intent
        mIntakeDay = getIntent().getExtras().getLong(AppConstants.BUNDLE_KEY_INTAKE_DAY);
        mMealType = getIntent().getExtras().getInt(AppConstants.BUNDLE_KEY_MEAL_TYPE);

        setTitle(getResources().getString(R.string.title_choose_food_activity, AppConstants.getMealTypeName(mMealType)));
        setContentView(R.layout.activity_choose_food);
        ButterKnife.bind(this);

        mStore = new HealthDataStore(this, mConnectionListener);
        mStore.connectService();
        mFoodDataHelper = new FoodDataHelper(mStore);

        // Load the FoodList from FoodInfoTable class
        List<String> foodNameArray = new ArrayList<>();
        foodNameArray.addAll(FoodInfoTable.keySet());
        Collections.sort(foodNameArray);

        ArrayAdapter<String> nameArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodNameArray);
        mNameListView.setAdapter(nameArrayAdapter);
    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {
        @Override
        public void onConnected() {
            Log.d(FoodNoteActivity.TAG, "onConnected");
            mIsServiceAvailable = true;
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(FoodNoteActivity.TAG, "onConnectionFailed");
        }

        @Override
        public void onDisconnected() {
            Log.d(FoodNoteActivity.TAG, "onDisconnected");
            mIsServiceAvailable = false;
        }
    };

    @OnItemClick(R.id.choose_food_list)
    void onItemClickNameListView(View view) {

        if (!mIsServiceAvailable) {
            Toast.makeText(this, "Health data service not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        final String selectedFoodType = ((TextView) view).getText().toString();
        // Show a Dialog to set intakes times, after tap the food from food list
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // Set EditText of the dialog
        final EditText takeCountInput = new EditText(this);
        dialogBuilder.setTitle(R.string.title_dialog_add_meal);
        dialogBuilder.setMessage(getString(R.string.msg_dialog_add_meal, selectedFoodType,
                FoodInfoTable.get(selectedFoodType).calorie));
        dialogBuilder.setView(takeCountInput);
        takeCountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        dialogBuilder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            // Take the String value (intakes count) from EditText
            String takeCount = takeCountInput.getText().toString();
            // Check for null or 0 value
            if (!takeCount.isEmpty() && !(".".equals(takeCount)) && (0.f != Float.valueOf(takeCount))) {
                // Represent 00:00:00 as Local time
                long localIntakeDay = mIntakeDay - TimeZone.getDefault().getOffset(mIntakeDay);
                mFoodDataHelper.createFoodData(selectedFoodType, Float.valueOf(takeCount), mMealType, localIntakeDay, isSuccess -> {
                    if (isSuccess) {
                        Toast.makeText(ChooseFoodActivity.this, "Food data insert success.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChooseFoodActivity.this, MealStoreActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(AppConstants.BUNDLE_KEY_MEAL_TYPE, mMealType);
                        bundle.putLong(AppConstants.BUNDLE_KEY_INTAKE_DAY, mIntakeDay);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ChooseFoodActivity.this, "Food data insert failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialogBuilder.show();
    }
}
