package com.example.pc.dissertation.db.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.example.pc.dissertation.AppApplication;
import com.example.pc.dissertation.db.tables.EventsTable;
import com.example.pc.dissertation.db.tables.StructuredLogTable;
import com.example.pc.dissertation.models.BusinessProcess;
import com.example.pc.dissertation.models.Event;

import java.util.LinkedList;
import java.util.List;

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

    public static List<Pair> findProcessEvents(String searchValue, String startItem, String endItem) {
        List<Pair> startEndItemList = new LinkedList<>();
        Cursor firstItemsCursor = getItemsCursor(searchValue, startItem);
        Cursor secondItemsCursor = getItemsCursor(searchValue, endItem);
        buildStartEndProcessItemsList(startEndItemList, firstItemsCursor, secondItemsCursor);
        return startEndItemList;
    }

    private static Cursor getItemsCursor(String searchColumn, String searchItem) {
        String queryLastItem = "SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE " + searchColumn + "='" + searchItem + "'";
        return AppApplication.getWritableDBInstance().rawQuery(queryLastItem, null);
    }

    private static void buildStartEndProcessItemsList(List<Pair> startEndItemList,
            Cursor firstItemIndexCursor, Cursor secondItemIndexCursor) {
        long startItemID = -1;
        long firstItemTimestamp = 0;
        long endItemID = -1;
        long secondItemTimestamp = 0;

        while (firstItemIndexCursor.moveToNext() && secondItemIndexCursor.moveToNext()) {
            // Get start item pos
            startItemID = firstItemIndexCursor.getLong(firstItemIndexCursor.getColumnIndex(EventsTable._ID));
            firstItemTimestamp = firstItemIndexCursor.getLong(firstItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));
            while (firstItemTimestamp < secondItemTimestamp) {
                startItemID = firstItemIndexCursor.getLong(firstItemIndexCursor.getColumnIndex(EventsTable._ID));
                firstItemTimestamp = firstItemIndexCursor.getLong(firstItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));
                if (secondItemIndexCursor.isLast()) break;
            }

            // Get end item pos
            endItemID = secondItemIndexCursor.getLong(secondItemIndexCursor.getColumnIndex(EventsTable._ID));
            secondItemTimestamp = secondItemIndexCursor.getLong(secondItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));
            while (secondItemTimestamp < firstItemTimestamp) {
                endItemID = secondItemIndexCursor.getLong(secondItemIndexCursor.getColumnIndex(EventsTable._ID));
                secondItemTimestamp = secondItemIndexCursor.getLong(secondItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));

                if (secondItemIndexCursor.isLast()) break;
            }
            startEndItemList.add(new Pair(startItemID, endItemID));
        }
    }

    @NonNull
    private static StringBuilder buildSelectProcessQuery(List<Pair> startEndItemList) {
        StringBuilder eventSetQuery = new StringBuilder();
        eventSetQuery.append("SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE ");
        if (startEndItemList.size() > 0) {
            eventSetQuery.append(" ( ");
            eventSetQuery.append(EventsTable._ID);
            eventSetQuery.append(
                    " BETWEEN '" + startEndItemList.get(0).first + "' AND '" + startEndItemList
                            .get(0).second + "')");
        }
        for (int i = 1; i < startEndItemList.size(); i++) {
            eventSetQuery.append(" OR ");
            eventSetQuery.append(" ( ");
            eventSetQuery.append(EventsTable._ID);
            eventSetQuery.append(
                    " BETWEEN '" + startEndItemList.get(i).first + "' AND '" + startEndItemList
                            .get(i).second + "')");
        }
        return eventSetQuery;
    }

    public static void delete() {
        AppApplication.getWritableDBInstance().execSQL(StructuredLogTable.getDeleteStatement());
    }

    public static Cursor getAllRows() {
        String selectQuery = "SELECT * FROM " + StructuredLogTable.TABLE_NAME;
        return AppApplication.getReadableDBInstance().rawQuery(selectQuery, null);
    }
}
