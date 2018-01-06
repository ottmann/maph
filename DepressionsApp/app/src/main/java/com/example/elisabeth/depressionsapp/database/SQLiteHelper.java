package com.example.elisabeth.depressionsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.elisabeth.depressionsapp.datamodel.MoodEntry;
import com.example.elisabeth.depressionsapp.datamodel.SensorEntry;
import com.example.elisabeth.depressionsapp.datamodel.WatchEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisabeth on 04.12.17.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = SQLiteHelper.class.getSimpleName();

    public static final String DB_NAME = "DepressionsApp.db";
    public static final int DB_VERSION = 1;

    private static final String TABLE_MOOD = "MoodEntry";
    private static final String TABLE_SMARTWATCH= "Smartwatch";
    private static final String TABLE_SENSORS = "Sensors";

    private static final String ID = "id";
    private static final String SCORE = "score";
    private static final String TIMESTAMP = "timestamp";

    private static final String TEMPERATURE = "temperature";
    private static final String AIRQUALITY = "airquality";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
        Log.d(LOG_TAG, "has created Database: " + getDatabaseName());
        //Used to create a new SQLite DB
        //initSQLiteTables(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            initSQLiteTables(db);
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Failed to create table: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMARTWATCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSORS);
        db.execSQL("vacuum");

        // create new tables
        onCreate(db);
    }

    public void initSQLiteTables(SQLiteDatabase db) {

        Log.d(LOG_TAG, "Deleting tables ...");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMARTWATCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENSORS);

        Log.d(LOG_TAG, "Creating tables ... ");

        final String CREATE_TABLE_MOOD = "CREATE TABLE " + TABLE_MOOD + " ("
                + " ID INTEGER PRIMARY KEY,"
                + " score INTEGER,"
                + " timestamp TEXT);";
        //+ " DATETIME DEFAULT CURRENT_TIMESTAMP)";

        //TODO: fill Smartwatch
        final String CREATE_TABLE_SMARTWATCH = "CREATE TABLE " + TABLE_SMARTWATCH + " ("
                + " ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " timestamp TEXT);";

        final String CREATE_TABLE_SENSORS = "CREATE TABLE " + TABLE_SENSORS + " ("
                + " ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " temperature DOUBLE,"
                + " airquality DOUBLE,"
                + " timestamp TEXT);";

        db.execSQL(CREATE_TABLE_MOOD);
        db.execSQL(CREATE_TABLE_SMARTWATCH);
        db.execSQL(CREATE_TABLE_SENSORS);

        //Log.d(LOG_TAG, "Filling tables ... ");
        //Create testdata
        createTestData();
    }

    // Add new mood to database
    public void addMood(MoodEntry m) {
        SQLiteDatabase db = this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_MOOD);

        ContentValues values = new ContentValues();
        values.put("ID", numRows+1);
        values.put("score", m.getScore());;
        values.put("timestamp", m.getTimestamp());

        // Inserting Row
        db.insert(TABLE_MOOD, null, values);
        db.close(); // Closing database connection
    }

    // Add new watch entry to database
    public void addWatchEntry(WatchEntry w) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("ID", w.getId());
        //values.put("value1", w.getValue1());
        //values.put("value2", w.getValue2());

        // Inserting Row
        db.insert(TABLE_SMARTWATCH, null, values);
        db.close(); // Closing database connection
    }

    // Add new sensor entry to database
    public void addSensorEntry(SensorEntry s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("ID", a.getId());
        values.put("temperature", s.getTemperature());
        values.put("airquality", s.getAirquality());
        values.put("timestamp", s.getTimestamp());

        // Inserting Row
        db.insert(TABLE_SENSORS, null, values);
        db.close(); // Closing database connection
    }


    /** GET FROM DATABASE */

    // Get a single mood
    public MoodEntry getMood(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOOD,
                new String[] { ID, SCORE, TIMESTAMP},
                ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MoodEntry m = new MoodEntry(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)),
                cursor.getString(2));

        db.close(); // Closing database connection
        return m;
    }

    // Get all moods
    public List<MoodEntry> getAllMoods() {
        List<MoodEntry> moodList = new ArrayList<MoodEntry>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOOD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_MOOD);
        int i= 1;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MoodEntry m = new MoodEntry();
                m.setId(i);
                //s.setId(Integer.parseInt(cursor.getString(0)));
                m.setScore(Integer.parseInt(cursor.getString(1)));
                m.setTimestamp(cursor.getString(2));

                moodList.add(m);
                i++;
            } while (cursor.moveToNext());
        }
        db.close(); // Closing database connection
        cursor.close();
        return moodList;
    }

    //TODO: get all watch entries
    public List<WatchEntry> getAllWatchEntries() {
        List<WatchEntry> watchEntryList = new ArrayList<WatchEntry>();
        String selectQuery = "SELECT  * FROM " + TABLE_SMARTWATCH;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_SMARTWATCH);
        int i= 1;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WatchEntry w = new WatchEntry();
                //s.setId(i);
                w.setId(Integer.parseInt(cursor.getString(0)));
                //TODO: add other values
                //w.setTimestamp(cursor.getString(1));

                watchEntryList.add(w);
                i++;
            } while (cursor.moveToNext());
        }
        db.close(); // Closing database connection
        cursor.close();
        return watchEntryList;
    }

    // Get all sensor entries
    public List<SensorEntry> getAllSensorEntries() {
        List<SensorEntry> sensorEntryList = new ArrayList<SensorEntry>();
        String selectQuery = "SELECT  * FROM " + TABLE_SENSORS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_SENSORS);
        int i= 1;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SensorEntry s = new SensorEntry();
                //s.setId(i);
                s.setId(Integer.parseInt(cursor.getString(0)));
                s.setTemperature(Double.parseDouble(cursor.getString(1)));
                s.setAirquality(Double.parseDouble(cursor.getString(2)));
                s.setTimestamp(cursor.getString(3));

                sensorEntryList.add(s);
                i++;
            } while (cursor.moveToNext());
        }
        db.close(); // Closing database connection
        cursor.close();
        return sensorEntryList;
    }

    /** Creates an authentic set of test data to test the app without database connection. */
    public void createTestData() {

        MoodEntry m1 = new MoodEntry(1, 4, "2018-01-01 12:00:00");
        addMood(m1);
        MoodEntry m2 = new MoodEntry(2, 1, "2018-01-02 12:00:00");
        addMood(m2);
        MoodEntry m3 = new MoodEntry(3, 3, "2018-01-03 12:00:00");
        addMood(m3);
        MoodEntry m4 = new MoodEntry(4, 2, "2018-01-04 12:00:00");
        addMood(m4);
        MoodEntry m5 = new MoodEntry(5, 5, "2018-01-05 12:00:00");
        addMood(m5);
        MoodEntry m6 = new MoodEntry(6, 2, "2018-01-06 12:00:00");
        addMood(m6);
        MoodEntry m7 = new MoodEntry(7, 1, "2018-01-07 12:00:00");
        addMood(m7);
        MoodEntry m8 = new MoodEntry(8, 3, "2018-01-08 12:00:00");
        addMood(m8);


        SensorEntry s1 = new SensorEntry(1, 18.0, 85.0, "2018-01-01 10:00:00" );
        addSensorEntry(s1);
        SensorEntry s2 = new SensorEntry(2, 17.0, 85.0, "2018-01-02 10:00:00" );
        addSensorEntry(s2);
        SensorEntry s3 = new SensorEntry(3, 21.0, 85.0, "2018-01-03 10:00:00" );
        addSensorEntry(s3);
    }
}
