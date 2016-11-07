package com.bytebuilding.affairmanager.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

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
    }
}
