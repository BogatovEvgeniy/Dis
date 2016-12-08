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
        String queryFirstItem = "SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE " + searchValue + "='" + startItem + "'";
        Cursor firstItemIndexCursor = AppApplication.getWritableDBInstance().rawQuery(
                queryFirstItem, null);

        String queryLastItem = "SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE " + searchValue + "='" + endItem + "'";
        Cursor secondItemIndexCursor = AppApplication.getWritableDBInstance().rawQuery(
                queryLastItem, null);

        // Get start item pos
        long startItemID = 0;
        long firstItemTimestamp =0;
        if (firstItemIndexCursor.moveToFirst()) {
            startItemID = firstItemIndexCursor.getLong(firstItemIndexCursor.getColumnIndex(
                    EventsTable._ID));
            firstItemTimestamp = firstItemIndexCursor.getLong(firstItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));
            secondItemIndexCursor.moveToFirst();
        }
        // Get end item pos
        long endItemID = 0;
        long secondItemTimestamp = 0;
        if (secondItemIndexCursor.moveToFirst()) {
            endItemID = secondItemIndexCursor.getLong(secondItemIndexCursor.getColumnIndex(EventsTable._ID));
            secondItemTimestamp = secondItemIndexCursor.getLong(secondItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));
            while (secondItemTimestamp < firstItemTimestamp) {
                endItemID = secondItemIndexCursor.getLong(secondItemIndexCursor.getColumnIndex(EventsTable._ID));
                secondItemTimestamp = secondItemIndexCursor.getLong(secondItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));

                if (secondItemIndexCursor.isLast()) {
                    break;
                }
            }
        }

        String eventSetQuery = "SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE " + EventsTable._ID + " BETWEEN '" + startItemID + "' AND '" + endItemID + "'";
        return AppApplication.getWritableDBInstance().rawQuery(eventSetQuery, null);
    }

    public static void delete() {
        AppApplication.getWritableDBInstance().execSQL(StructuredLogTable.getDeleteStatement());
    }
}
