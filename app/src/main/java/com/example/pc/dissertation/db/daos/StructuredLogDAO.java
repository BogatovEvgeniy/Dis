package com.example.pc.dissertation.db.daos;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.pc.dissertation.AppApplication;
import com.example.pc.dissertation.db.tables.EventsTable;
import com.example.pc.dissertation.db.tables.StructuredLogTable;
import com.example.pc.dissertation.models.BusinessProcess;
import com.example.pc.dissertation.models.Event;

import static com.example.pc.dissertation.db.daos.Utils.insertValue;

public class StructuredLogDAO {
    static long processCounter = 0L;

    public static void create() {
        AppApplication.getWritableDBInstance().execSQL(StructuredLogTable.getCreateStatement());
    }

    public static void insertProcess(BusinessProcess businessProcess) {
        processCounter++;
        for (Event event : businessProcess.getEvents()) {
            ContentValues contentValues = new ContentValues();
            insertValue(contentValues, StructuredLogTable.PROCESS, String.valueOf(processCounter));
            insertValue(contentValues, StructuredLogTable.USER, event.getUserName());
            insertValue(contentValues, StructuredLogTable.USER_ROLE, event.getUserRole());
            insertValue(contentValues, StructuredLogTable.OBJECT, event.getObject());
            insertValue(contentValues, StructuredLogTable.TIMESTAMP, event.getTimestamp());
            insertValue(contentValues, StructuredLogTable.STATUS, event.getStatus());
            AppApplication.getWritableDBInstance().insert(StructuredLogTable.TABLE_NAME, null,
                    contentValues);
        }
    }

    public static Cursor findProcessEvents(String searchValue, String startItem, String endItem) {

        // Get sets of first and las items
        String queryFirstItem = "SELECT * FROM" + StructuredLogTable.TABLE_NAME + " WHERE " + searchValue + " IS " + startItem;
        Cursor firstItemIndexCursor = AppApplication.getWritableDBInstance().rawQuery(
                queryFirstItem, null);

        String queryLastItem = "SELECT * FROM" + StructuredLogTable.TABLE_NAME + " WHERE " + searchValue + " IS " + endItem;
        Cursor secondItemIndexCursor = AppApplication.getWritableDBInstance().rawQuery(
                queryLastItem, null);

        // Get start item pos
        firstItemIndexCursor.moveToFirst();
        long startItemID = firstItemIndexCursor.getLong(
                firstItemIndexCursor.getColumnIndex(StructuredLogTable._ID));
        long firstItemTimestamp = firstItemIndexCursor.getLong(
                firstItemIndexCursor.getColumnIndex(StructuredLogTable.TIMESTAMP));
        secondItemIndexCursor.moveToFirst();

        // Get end item pos
        long endItemID = firstItemIndexCursor.getLong(
                secondItemIndexCursor.getColumnIndex(StructuredLogTable._ID));
        long secondItemTimestamp = firstItemIndexCursor.getLong(
                secondItemIndexCursor.getColumnIndex(StructuredLogTable.TIMESTAMP));
        while (secondItemTimestamp < firstItemTimestamp) {
            endItemID = firstItemIndexCursor.getLong(
                    firstItemIndexCursor.getColumnIndex(StructuredLogTable._ID));
            secondItemTimestamp = firstItemIndexCursor.getLong(
                    firstItemIndexCursor.getColumnIndex(StructuredLogTable.TIMESTAMP));

            if (secondItemIndexCursor.isLast()) {
                break;
            }
        }
        String eventSetQuery = "SELECT * FROM " + StructuredLogTable.TABLE_NAME + " WHERE " + EventsTable._ID + " BETWEEN " + startItemID + " AND " + endItemID;
        return AppApplication.getWritableDBInstance().rawQuery(eventSetQuery, null);
    }
}
