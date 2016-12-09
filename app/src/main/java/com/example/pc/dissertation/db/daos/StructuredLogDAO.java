package com.example.pc.dissertation.db.daos;

import android.content.ContentValues;
import android.database.Cursor;
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

    public static Cursor findProcessEvents(String searchValue, String startItem, String endItem) {
        List<Pair> startEndItemList = new LinkedList<>();

        // Get sets of first and las items
        String queryFirstItem = "SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE " + searchValue + "='" + startItem + "'";
        Cursor firstItemIndexCursor = AppApplication.getWritableDBInstance().rawQuery(
                queryFirstItem, null);

        String queryLastItem = "SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE " + searchValue + "='" + endItem + "'";
        Cursor secondItemIndexCursor = AppApplication.getWritableDBInstance().rawQuery(
                queryLastItem, null);


        int startItemID = -1;
        int firstItemTimestamp = 0;
        int endItemID = -1;
        int secondItemTimestamp = 0;

        while (firstItemIndexCursor.moveToNext() && secondItemIndexCursor.moveToNext()) {
            // Get start item pos
            int columnIndex = firstItemIndexCursor.getColumnIndex(EventsTable._ID);
            startItemID = firstItemIndexCursor.getInt(columnIndex);
            int columnIndex1 = firstItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP);
            firstItemTimestamp = firstItemIndexCursor.getInt(columnIndex1);

            // Get end item pos
            endItemID = secondItemIndexCursor.getInt(
                    secondItemIndexCursor.getColumnIndex(EventsTable._ID));
            secondItemTimestamp = secondItemIndexCursor.getInt(
                    secondItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));
            while (secondItemTimestamp < firstItemTimestamp) {
                endItemID = secondItemIndexCursor.getInt(
                        secondItemIndexCursor.getColumnIndex(EventsTable._ID));
                secondItemTimestamp = secondItemIndexCursor.getInt(
                        secondItemIndexCursor.getColumnIndex(EventsTable.TIMESTAMP));

                if (secondItemIndexCursor.isLast()) {
                    break;
                }
            }
            startEndItemList.add(new Pair(startItemID, endItemID));
        }

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

        return AppApplication.getWritableDBInstance().rawQuery(eventSetQuery.toString(), null);
    }

    public static void delete() {
        AppApplication.getWritableDBInstance().execSQL(StructuredLogTable.getDeleteStatement());
    }
}
