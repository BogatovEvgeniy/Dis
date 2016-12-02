package com.example.pc.dissertation.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.pc.dissertation.AppApplication;

import java.util.List;

/**
 * Created by PC on 02.12.2016.
 */

public class RawLodTableDAO {
    public static void insert(List<String> row) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < RawLogTable.COLUMNS_COUNT; i++) {
            contentValues.put(RawLogTable.ELEMENT_PREFFIX + i, row.get(i));
        }
        AppApplication.getWritableDBInstance().insert(RawLogTable.TABLE_NAME, null, contentValues);
    }

    public static int readAll() {
        Cursor cursor = AppApplication.getWritableDBInstance().rawQuery("SELECT * FROM " + RawLogTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            do {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i =  0; i < columnNames.length; i++) {
                    stringBuffer.append("COL NAME:" + i + " has value:" + cursor.getString(i));
                    stringBuffer.append(" | ");
                }
                Log.d("DB SCREEN", stringBuffer.toString());
            } while (cursor.moveToNext());
        }
        return cursor.getCount();
    }
}
