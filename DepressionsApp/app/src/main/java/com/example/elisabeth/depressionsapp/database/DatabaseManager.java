package com.example.elisabeth.depressionsapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.elisabeth.depressionsapp.datamodel.MoodEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elisabeth on 06.01.18.
 */
public class DatabaseManager {

    public static SQLiteHelper sqlh;

    public DatabaseManager () {

    }

    /** call this function on application start  */
    public static int systemSync(Context context) {
        sqlh = new SQLiteHelper(context);
        return 0;
    }

    /* All functions concerning MoodEntry */
    public static MoodEntry getMood(int id) {
        return sqlh.getMood(id);
    }
    public static List<MoodEntry> getAllMoods() {
        return sqlh.getAllMoods();
    }
    public static void addMood(MoodEntry mood) {
        sqlh.addMood(mood);
    }
    //public static long getNumberOfMoodEntries() { return sqlh.numberOfEntries(); }
}
