package com.example.elisabeth.depressionsapp.devices.samsung;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elisabeth.depressionsapp.R;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
public class MealStoreActivity extends Activity {

    @BindView(R.id.saved_food_list)
    ListView mCachedFoodListView;
    @BindView(R.id.total_calorie)
    TextView mCalories;

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
        setContentView(R.layout.activity_meal_store);
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
            Log.d(FoodNoteActivity.TAG, "onConnected");

            // Get the cached food intake data
            mFoodDataHelper.readDailyIntakeDetails(mIntakeDay, mMealType, mListener);
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(FoodNoteActivity.TAG, "onConnectionFailed");
        }

        @Override
        public void onDisconnected() {
            Log.d(FoodNoteActivity.TAG, "onDisconnected");
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
