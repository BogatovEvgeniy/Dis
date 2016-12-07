package com.example.pc.dissertation.db.daos;

import android.content.ContentValues;

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
}
