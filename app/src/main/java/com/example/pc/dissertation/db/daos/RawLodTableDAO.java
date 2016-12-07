package com.example.pc.dissertation.db.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.pc.dissertation.AppApplication;
import com.example.pc.dissertation.db.tables.RawLogTable;

import java.util.List;

public class RawLodTableDAO {
    public static void insert(List<String> row) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < RawLogTable.COLUMNS_COUNT; i++) {
            contentValues.put(RawLogTable.ELEMENT_PREFFIX + i, row.get(i));
        }
        AppApplication.getWritableDBInstance().insert(RawLogTable.TABLE_NAME, null, contentValues);
    }

    public static Cursor readAllRows() {
        return AppApplication.getWritableDBInstance().rawQuery("SELECT * FROM " + RawLogTable.TABLE_NAME, null);
    }

    private static void printLineInLog(Cursor cursor, String[] columnNames) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i =  0; i < columnNames.length; i++) {
            stringBuffer.append("COL NAME:" + i + " has value:" + cursor.getString(i));
            stringBuffer.append(" | ");
        }
        Log.d("DB SCREEN", stringBuffer.toString());
    }
}
