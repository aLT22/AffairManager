package com.bytebuilding.affairmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.bytebuilding.affairmanager.model.Affair;

/**
 * Created by Alexey on 07.11.2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "affair_manager_database";

    public static final String TABLE_NAME = "AFFAIR";

    public static final String COLOUMN_TITLE = "TITLE";
    public static final String COLOUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLOUMN_DATE = "DATE";
    public static final String COLOUMN_TIME = "TIME";
    public static final String COLOUMN_PRIORITY = "PRIORITY";
    public static final String COLOUMN_OBJECT = "OBJECT";
    public static final String COLOUMN_TYPE = "TYPE";
    public static final String COLOUMN_PLACE = "PLACE";
    public static final String COLOUMN_REPEATE_TIMESTAMP = "REPEATE_TIMESTAMP";
    public static final String COLOUMN_TIMESTAMP = "AFFAIR_TIMESTAMP";

    public static final String TABLE_CREATION_SCRIPT = "CREATE TABLE " + TABLE_NAME
            + " (" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLOUMN_TITLE + " TEXT NOT NULL, "
            + COLOUMN_DESCRIPTION + " TEXT, " + COLOUMN_DATE + " LONG, "
            + COLOUMN_TIME + " LONG, " + COLOUMN_PRIORITY + " INTEGER NOT NULL, "
            + COLOUMN_OBJECT + " TEXT NOT NULL, " + COLOUMN_TYPE + " TEXT NOT NULL, "
            + COLOUMN_PLACE + " TEXT, " + COLOUMN_REPEATE_TIMESTAMP + " LONG, "
            + COLOUMN_TIMESTAMP + " LONG);";

    public static final String TABLE_DROP_SCRIPT = "DROP TABLE " + TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATION_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP_SCRIPT);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP_SCRIPT);
        onCreate(db);
    }

    public void saveAffair(Affair affair) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLOUMN_TITLE, affair.getTitle());
        contentValues.put(COLOUMN_DESCRIPTION, affair.getDescription());
        contentValues.put(COLOUMN_DATE, affair.getDate());
        contentValues.put(COLOUMN_TIME, affair.getTime());
        contentValues.put(COLOUMN_PRIORITY, affair.getPriority());
        contentValues.put(COLOUMN_OBJECT, affair.getObject());
        contentValues.put(COLOUMN_TYPE, affair.getType());
        contentValues.put(COLOUMN_PLACE, affair.getPlace());
        contentValues.put(COLOUMN_REPEATE_TIMESTAMP, affair.getRepeatTimestamp());
        contentValues.put(COLOUMN_TIMESTAMP, affair.getTimestamp());

        getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }
}
