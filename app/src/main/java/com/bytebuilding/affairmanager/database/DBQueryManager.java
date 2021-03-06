package com.bytebuilding.affairmanager.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bytebuilding.affairmanager.model.Affair;

import java.util.ArrayList;
import java.util.List;

public class DBQueryManager {

    private SQLiteDatabase database;

    public DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public List<Affair> getAffairs(String selection, String[] selectionArgs, String orderBy) {
        List<Affair> affairList = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, selection, selectionArgs, null,
                null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(DBHelper.COLOUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DBHelper
                        .COLOUMN_DESCRIPTION));
                long date = cursor.getLong(cursor.getColumnIndex(DBHelper.COLOUMN_DATE));
                long time = cursor.getLong(cursor.getColumnIndex(DBHelper.COLOUMN_TIME));
                int priority = cursor.getInt(cursor.getColumnIndex(DBHelper.COLOUMN_PRIORITY));
                String object = cursor.getString(cursor.getColumnIndex(DBHelper.COLOUMN_OBJECT));
                String type = cursor.getString(cursor.getColumnIndex(DBHelper.COLOUMN_TYPE));
                String place = cursor.getString(cursor.getColumnIndex(DBHelper.COLOUMN_PLACE));
                long repeateTimestamp = cursor.getLong(cursor.getColumnIndex(DBHelper
                        .COLOUMN_REPEATE_TIMESTAMP));
                long timestamp = cursor.getLong(cursor.getColumnIndex(DBHelper.COLOUMN_TIMESTAMP));
                int status = cursor.getInt(cursor.getColumnIndex(DBHelper.COLOUMN_STATUS));

                Affair affair = new Affair(title, description, date, time, priority,
                        object, type, place, repeateTimestamp, timestamp, status);
                affairList.add(affair);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return affairList;
    }

    public Affair getAffairByTimestamp(long timestamp) {
        Affair affair = null;

        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, DBHelper.SELECTION_BY_TIMESTAMP,
                new String[]{Long.toString(timestamp)}, null, null, null);

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.COLOUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(DBHelper
                    .COLOUMN_DESCRIPTION));
            long date = cursor.getLong(cursor.getColumnIndex(DBHelper.COLOUMN_DATE));
            long time = cursor.getLong(cursor.getColumnIndex(DBHelper.COLOUMN_TIME));
            int priority = cursor.getInt(cursor.getColumnIndex(DBHelper.COLOUMN_PRIORITY));
            String object = cursor.getString(cursor.getColumnIndex(DBHelper.COLOUMN_OBJECT));
            String type = cursor.getString(cursor.getColumnIndex(DBHelper.COLOUMN_TYPE));
            String place = cursor.getString(cursor.getColumnIndex(DBHelper.COLOUMN_PLACE));
            long repeateTimestamp = cursor.getLong(cursor.getColumnIndex(DBHelper
                    .COLOUMN_REPEATE_TIMESTAMP));
            int status = cursor.getInt(cursor.getColumnIndex(DBHelper.COLOUMN_STATUS));

            affair = new Affair(title, description, date, time, priority,
                    object, type, place, repeateTimestamp, timestamp, status);
        }
        cursor.close();

        return affair;
    }

}
