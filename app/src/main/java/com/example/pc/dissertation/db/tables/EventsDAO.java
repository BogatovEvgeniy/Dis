package com.example.pc.dissertation.db.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.pc.dissertation.AppApplication;

import java.util.List;

/**
 * Created by PC on 30.11.2016.
 */

public class EventsDAO {
    private static String TABLE_COLUMS_LIST;
    private static ContentValues VALUES_TO_INSERT;

    public static String TABLE_NAME = "EVENTS";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    //TODO Replace String fields of values on ID fields
    public static String _ID = "_id";
    //    public static String PROCESS_ID = "process_id";
    public static String PROCESS = "process";
    //    public static String USER_ID = "user_id";
    public static String USER = "user";
    //    public static String USER_ROLE_ID = "user_role_id";
    public static String USER_ROLE = "user_role";
    //    public static String OBJECT_ID = "object_id";
    public static String OBJECT = "object";
    public static String TIMESTAMP = "timestamp";
    //    public static String STATUS_ID = "status_id";
    public static String STATUS = "status";

    public static void insert(List<String> logEvent) {
        SQLiteDatabase writableDBInstance = AppApplication.getWritableDBInstance();
        ContentValues content_values = new ContentValues();
        content_values.put(EventsTable._ID, logEvent.get(0));
        content_values.put(EventsTable.PROCESS, logEvent.get(1));
        content_values.put(EventsTable.USER, logEvent.get(2));
        content_values.put(EventsTable.USER_ROLE, logEvent.get(3));
        content_values.put(EventsTable.OBJECT, logEvent.get(4));
        content_values.put(EventsTable.TIMESTAMP, logEvent.get(5));
        content_values.put(EventsTable.STATUS, logEvent.get(6));
        writableDBInstance.insert(EventsTable.TABLE_NAME, TABLE_COLUMS_LIST, content_values);
    }

    public static void select(String rowID) {
        SQLiteDatabase writableDBInstance = AppApplication.getWritableDBInstance();
        ContentValues content_values = new ContentValues();
        content_values.put(EventsTable._ID, rowID);
        writableDBInstance.insert(EventsTable.TABLE_NAME, TABLE_COLUMS_LIST, content_values);
    }

    public static void delete(String rowID) {
        SQLiteDatabase writableDBInstance = AppApplication.getWritableDBInstance();
        ContentValues content_values = new ContentValues();
        content_values.put(EventsTable._ID, rowID);
        writableDBInstance.insert(EventsTable.TABLE_NAME, TABLE_COLUMS_LIST, content_values);
    }
}
