package com.example.elisabeth.depressionsapp.devices.samsung;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.elisabeth.depressionsapp.R;
import com.example.elisabeth.depressionsapp.devices.WatchActivity;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodNoteActivity extends AppCompatActivity {

    public static final String TAG = "FoodNote";
    public Button GoBack;
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    @BindView(R.id.date_view)
    TextView mDayTv;
    @BindView(R.id.breakfast_calorie) TextView mBreakfastCaloriesTv;
    @BindView(R.id.lunch_calorie) TextView mLunchCaloriesTv;
    @BindView(R.id.dinner_calorie) TextView mDinnerCaloriesTv;
    @BindView(R.id.morning_snack_calorie) TextView mMorningSnackCaloriesTv;
    @BindView(R.id.afternoon_snack_calorie) TextView mAfternoonSnackCaloriesTv;
    @BindView(R.id.evening_snack_calorie) TextView mEveningSnackCaloriesTv;
    @BindView(R.id.total_calorie) TextView mTotalCaloriesTv;

    private HealthDataStore mStore;
    private FoodDataHelper mDataHelper;
    private long mDayStartTime;
    private boolean mIsStoreConnected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_note);
        GoBack = (Button)findViewById(R.id.buttonback);
        GoBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodNoteActivity.this, WatchActivity.class);
                startActivity(intent);

            }
        });





        ButterKnife.bind(this);

        // Get the current time and show it
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        mDayStartTime = calendar.getTimeInMillis();

        mDayTv.setText(getFormattedTime());

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
        mDataHelper = new FoodDataHelper(mStore);
    }

    @Override
    public void onDestroy() {
        HealthDataObserver.removeObserver(mStore, mObserver);
        mStore.disconnectService();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
    }

    @OnClick(R.id.move_before)
    void onClickBeforeButton() {
        mDayStartTime -= ONE_DAY;
        mDayTv.setText(getFormattedTime());
        mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
    }

    @OnClick(R.id.move_next)
    void onClickNextButton() {
        mDayStartTime += ONE_DAY;
        mDayTv.setText(getFormattedTime());
        mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
    }

    @OnClick(R.id.add_icon_breakfast)
    void onClickBreakfastButton() {
        launchActivity(ChooseFoodActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_BREAKFAST);
    }

    @OnClick(R.id.breakfast_layout)
    void onClickBreakfastLayout() {
        launchActivity(MealStoreActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_BREAKFAST);
    }

    @OnClick(R.id.add_icon_lunch)
    void onClickLunchButton() {
        launchActivity(ChooseFoodActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_LUNCH);
    }

    @OnClick(R.id.lunch_layout)
    void onClickLunchLayout() {
        launchActivity(MealStoreActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_LUNCH);
    }

    @OnClick(R.id.add_icon_dinner)
    void onClickDinnerButton() {
        launchActivity(ChooseFoodActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_DINNER);
    }

    @OnClick(R.id.dinner_layout)
    void onClickDinnerLayout() {
        launchActivity(MealStoreActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_DINNER);
    }

    @OnClick(R.id.add_icon_morning_snack)
    void onClickMorningSnackButton() {
        launchActivity(ChooseFoodActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_MORNING_SNACK);
    }

    @OnClick(R.id.morning_snack_layout)
    void onClickMorningSnackLayout() {
        launchActivity(MealStoreActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_MORNING_SNACK);
    }

    @OnClick(R.id.add_icon_afternoon_snack)
    void onClickAfternoonSnackButton() {
        launchActivity(ChooseFoodActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_AFTERNOON_SNACK);
    }

    @OnClick(R.id.afternoon_snack_layout)
    void onClickAfternoonSnackLayout() {
        launchActivity(MealStoreActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_AFTERNOON_SNACK);
    }

    @OnClick(R.id.add_icon_evening_snack)
    void onClickEveningSnackButton() {
        launchActivity(ChooseFoodActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_EVENING_SNACK);
    }

    @OnClick(R.id.evening_snack_layout)
    void onClickEveningSnackLayout() {
        launchActivity(MealStoreActivity.class, HealthConstants.FoodIntake.MEAL_TYPE_EVENING_SNACK);
    }

    private String getFormattedTime() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy (E)", Locale.GERMANY);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(mDayStartTime);
    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {
        @Override
        public void onConnected() {
            Log.d(TAG, "onConnected");
            mIsStoreConnected = true;
            if (isPermissionAcquired()) {
                mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
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
            if (HealthConstants.FoodIntake.HEALTH_DATA_TYPE.equals(dataTypeName)) {
                mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
            }
        }
    };

    private final HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult> mPermissionListener =
            new HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult>() {

                @Override
                public void onResult(HealthPermissionManager.PermissionResult result) {
                    Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = result.getResultMap();
                    // Show a permission alarm and initializes the calories if
                    // permissions are not acquired
                    if (resultMap.values().contains(Boolean.FALSE)) {
                        showPermissionAlarmDialog();
                    } else {
                        // Get the calories of Indexed time and display it
                        mDataHelper.readDailyIntakeCalories(mDayStartTime, mListener);
                        // Register an observer to listen changes of the calories
                        HealthDataObserver.addObserver(mStore, HealthConstants.FoodIntake.HEALTH_DATA_TYPE, mObserver);
                    }
                }
            };

    private void showPermissionAlarmDialog() {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(FoodNoteActivity.this);
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
                error.resolve(FoodNoteActivity.this);
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton(R.string.cancel, null);
        }

        alert.show();
    }

    private void showTotalCalories(String breakfast, String lunch, String dinner, String morningSnack,
                                   String afternoonSnack, String eveningSnack, String total) {

        mBreakfastCaloriesTv.setText(breakfast);
        mLunchCaloriesTv.setText(lunch);
        mDinnerCaloriesTv.setText(dinner);
        mMorningSnackCaloriesTv.setText(morningSnack);
        mAfternoonSnackCaloriesTv.setText(afternoonSnack);
        mEveningSnackCaloriesTv.setText(eveningSnack);
        mTotalCaloriesTv.setText(total);
    }


    private FoodDataHelper.OnRetrieveDailyCaloriesListener mListener = new FoodDataHelper.OnRetrieveDailyCaloriesListener() {
        @Override
        public void onRetrieved(float breakfast, float lunch, float dinner, float morningSnack,
                                float afternoonSnack, float eveningSnack) {
            runOnUiThread(() -> {
                float total = breakfast + lunch + dinner + morningSnack + afternoonSnack + eveningSnack;
                showTotalCalories(String.valueOf(breakfast), String.valueOf(lunch),
                        String.valueOf(dinner),
                        String.valueOf(morningSnack), String.valueOf(afternoonSnack),
                        String.valueOf(eveningSnack),
                        String.valueOf(total));
            });
        }

        @Override
        public void onRetrieveFailed() {
            showTotalCalories("", "", "", "", "", "", "");
        }
    };


    // Method for generate Intent for meal type and activities
    private void launchActivity(Class<? extends Activity> mealType, int mealTypeInteger) {
        if (!mIsStoreConnected || !isPermissionAcquired()) {
            showPermissionAlarmDialog();
            return;
        }

        Intent intent = new Intent(getBaseContext(), mealType);
        if (mealType.equals(ChooseFoodActivity.class)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.BUNDLE_KEY_MEAL_TYPE, mealTypeInteger);
        bundle.putLong(AppConstants.BUNDLE_KEY_INTAKE_DAY, mDayStartTime);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean isPermissionAcquired() {
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Check whether the permissions that this application needs are acquired
            Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(generatePermissionKeySet());
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
            pmsManager.requestPermissions(generatePermissionKeySet(), FoodNoteActivity.this)
                    .setResultListener(mPermissionListener);
        } catch (Exception e) {
            Log.e(TAG, "Permission setting fails.", e);
        }
    }

    private Set<HealthPermissionManager.PermissionKey> generatePermissionKeySet() {
        Set<HealthPermissionManager.PermissionKey> pmsKeySet = new HashSet<>();

        // Add the read and write permissions to Permission KeySet
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.FoodIntake.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.FoodIntake.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.WRITE));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.FoodInfo.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.FoodInfo.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.WRITE));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.FoodInfo.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.FoodInfo.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.WRITE));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
        pmsKeySet.add(new HealthPermissionManager.PermissionKey(StepCountReader.STEP_SUMMARY_DATA_TYPE_NAME, HealthPermissionManager.PermissionType.READ));
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