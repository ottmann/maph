package com.example.elisabeth.depressionsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by elisabeth on 04.12.17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DepressionsApp.db";
    public static final String USER_TABLE_NAME = "User";
    public static final String USER_COLUMN_NAME = "Name";
    public static final String MOOD_TABLE_NAME = "Mood";
    public static final String MOOD_COLUMN_USER = "UserId";
    public static final String MOOD_COLUMN_TIMESTAMP = "Timestamp";
    public static final String MOOD_COLUMN_SCORE = "Score";
    private HashMap hp;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS" + USER_TABLE_NAME + "(" +
                        "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Name VARCHAR, " +
                        "Timestamp DATETIME)"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS" + MOOD_TABLE_NAME + "(" +
                        "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "UserId INTEGER, " +
                        "Score INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS" + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + MOOD_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact (String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.insert("contacts", null, contentValues);
        return true;
    }

    public Cursor getUserData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from User where id=" + id + "", null );
        return res;
    }

    public Cursor getMoodData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Mood where id=" + id + "", null );
        return res;
    }

    public int numberOfUserRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_TABLE_NAME);
        return numRows;
    }

    public int numberOfMoodRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MOOD_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(USER_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
