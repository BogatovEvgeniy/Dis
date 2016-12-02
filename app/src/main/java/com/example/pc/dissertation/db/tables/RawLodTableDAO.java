package com.example.pc.dissertation.db.tables;

import android.content.ContentValues;

import com.example.pc.dissertation.AppApplication;

import java.util.List;

/**
 * Created by PC on 02.12.2016.
 */

public class RawLodTableDAO {
    public static void insert(List<String> row) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < RawLogTable.COLUMNS_COUNT; i++) {
            contentValues.put(RawLogTable.ELEMENT_PREFFIX + 1, row.get(i));
        }
        AppApplication.getWritableDBInstance().insert(RawLogTable.TABLE_NAME, null, contentValues);
    }
}
