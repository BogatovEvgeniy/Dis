package com.example.pc.dissertation;

import android.database.Cursor;
import android.util.Log;

public class Utils {

    public static final String TAG = Utils.class.getName();

    public static void printCursorInLog(Cursor cursor) {
        StringBuilder stringBuilder = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                stringBuilder.append((cursor.getPosition() + 1) + ". ");
                for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
                    stringBuilder.append(cursor.getColumnName(columnIndex) + ":" + cursor.getString(columnIndex) + " ");
                }
                Log.d(TAG, stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.length());
            } while (cursor.moveToNext());
        }
    }
}
