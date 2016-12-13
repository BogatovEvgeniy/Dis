package com.example.pc.dissertation.models;

import android.database.Cursor;

import com.example.pc.dissertation.db.tables.EventsTable;
import com.example.pc.dissertation.db.tables.StructuredLogTable;

import java.util.LinkedList;
import java.util.List;

public class BusinessProcess {
    private List<Event> events = new LinkedList<>();

    public static BusinessProcess createFromCursor(Cursor cursor) {
        BusinessProcess businessProcess = new BusinessProcess();
        if (cursor.moveToFirst()){
           int activityNameIndex = cursor.getColumnIndex(EventsTable.ACTIVITY);
           int userIndex = cursor.getColumnIndex(EventsTable.USER);
           int userRoleIndex = cursor.getColumnIndex(EventsTable.USER_ROLE);
           int resourceIndex = cursor.getColumnIndex(EventsTable.RESOURCE);
           int timestampIndex = cursor.getColumnIndex(EventsTable.TIMESTAMP);
           int statusIndex = cursor.getColumnIndex(EventsTable.STATUS);

           do {
               Event event = new Event();
               event.setActivityName(cursor.getString(activityNameIndex));
               event.setUserName(cursor.getString(userIndex));
               event.setUserRole(cursor.getString(userRoleIndex));
               event.setObject(cursor.getString(resourceIndex));
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
