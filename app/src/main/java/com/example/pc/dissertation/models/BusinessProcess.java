package com.example.pc.dissertation.models;

import android.database.Cursor;

import com.example.pc.dissertation.db.tables.StructuredLogTable;

import java.util.LinkedList;
import java.util.List;

public class BusinessProcess {
    private List<Event> events = new LinkedList<>();

    public static BusinessProcess createFromCursor(Cursor cursor) {
        BusinessProcess businessProcess = new BusinessProcess();
        int activityNameIndex = cursor.getColumnIndex(StructuredLogTable.ACTIVITY_NAME);
        int userIndex = cursor.getColumnIndex(StructuredLogTable.USER);
        int userRoleIndex = cursor.getColumnIndex(StructuredLogTable.USER_ROLE);
        int objectIndex = cursor.getColumnIndex(StructuredLogTable.RESOURCE);
        int timestampIndex = cursor.getColumnIndex(StructuredLogTable.TIMESTAMP);
        int statusIndex = cursor.getColumnIndex(StructuredLogTable.STATUS);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setActivityName(cursor.getString(activityNameIndex));
                event.setUserName(cursor.getString(userIndex));
                event.setUserRole(cursor.getString(userRoleIndex));
                event.setObject(cursor.getString(objectIndex));
                event.setTimestamp(cursor.getString(timestampIndex));
                event.setStatus(cursor.getString(statusIndex));
                businessProcess.addEvent(event);
            } while (cursor.moveToNext());
        }
        return businessProcess;
    }

    private void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }
}
