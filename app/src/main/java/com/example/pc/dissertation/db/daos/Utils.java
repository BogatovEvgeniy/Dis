package com.example.pc.dissertation.db.daos;

import android.content.ContentValues;

import com.example.pc.dissertation.AppApplication;

public class Utils {

    public static void insertValue(ContentValues contentValues, String column, String value) {
        if (value == null) {
            contentValues.putNull(column);
        } else {
            contentValues.put(column, value);
        }
    }

    public static void insertValue(ContentValues contentValues, String column, long value) {
            contentValues.put(column, value);

    }
    public static boolean checkTableExistance(String tableName){
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
        int rowsFound = AppApplication.getWritableDBInstance().rawQuery(query, null).getCount();
        return rowsFound > 0;
    }
}
