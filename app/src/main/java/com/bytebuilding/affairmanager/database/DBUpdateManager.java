package com.bytebuilding.affairmanager.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.bytebuilding.affairmanager.model.Affair;

/**
 * Created by Alexey on 07.11.2016.
 */

public class DBUpdateManager {

    private SQLiteDatabase database;

    public DBUpdateManager(SQLiteDatabase database) {
        this.database = database;
    }

    private void update(String coloumn, long key, String value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(coloumn, value);
        database.update(DBHelper.TABLE_NAME, contentValues, DBHelper.COLOUMN_TIMESTAMP + " = " + key,
                null);
    }

    private void update(String coloumn, long key, long value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(coloumn, value);
        database.update(DBHelper.TABLE_NAME, contentValues, DBHelper.COLOUMN_TIMESTAMP + " = " + key,
                null);
    }

    private void update(String coloumn, long key, int value) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(coloumn, value);
        database.update(DBHelper.TABLE_NAME, contentValues, DBHelper.COLOUMN_TIMESTAMP + " = " + key,
                null);
    }

    public void title(long timestamp, String title) {
        update(DBHelper.COLOUMN_TITLE, timestamp, title);
    }

    public void description(long timestamp, String description) {
        update(DBHelper.COLOUMN_DESCRIPTION, timestamp, description);
    }

    public void date(long timestamp, long date) {
        update(DBHelper.COLOUMN_DATE, timestamp, date);
    }

    public void time(long timestamp, long time) {
        update(DBHelper.COLOUMN_TIME, timestamp, time);
    }

    public void priority(long timestamp, int priority) {
        update(DBHelper.COLOUMN_PRIORITY, timestamp, priority);
    }

    public void object(long timestamp, String object) {
        update(DBHelper.COLOUMN_OBJECT, timestamp, object);
    }

    public void type(long timestamp, String type) {
        update(DBHelper.COLOUMN_TYPE, timestamp, type);
    }

    public void place(long timestamp, String place) {
        update(DBHelper.COLOUMN_PLACE, timestamp, place);
    }

    public void repeateTimestamp(long timestamp, long repeateTimestamp) {
        update(DBHelper.COLOUMN_REPEATE_TIMESTAMP, timestamp, repeateTimestamp);
    }

    public void timestamp(long timestamp, long newTimestamp) {
        update(DBHelper.COLOUMN_TIMESTAMP, timestamp, newTimestamp);
    }

    public void status(long timestamp, int status) {
        update(DBHelper.COLOUMN_STATUS, timestamp, status);
    }

    public void affair(Affair affair) {
        title(affair.getTimestamp(), affair.getTitle());
        description(affair.getTimestamp(), affair.getDescription());
        date(affair.getTimestamp(), affair.getDate());
        time(affair.getTimestamp(), affair.getTime());
        priority(affair.getTimestamp(), affair.getPriority());
        object(affair.getTimestamp(), affair.getObject());
        type(affair.getTimestamp(), affair.getType());
        place(affair.getTimestamp(), affair.getPlace());
        repeateTimestamp(affair.getTimestamp(), affair.getRepeatTimestamp());
        timestamp(affair.getTimestamp(), affair.getTimestamp());
        status(affair.getTimestamp(), affair.getStatus());
    }
}
