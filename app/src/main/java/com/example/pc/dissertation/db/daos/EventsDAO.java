package com.example.pc.dissertation.db.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pc.dissertation.AppApplication;
import com.example.pc.dissertation.db.tables.EventsTable;

import java.util.List;

import static com.example.pc.dissertation.db.daos.Utils.insertValue;

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

    // TODO could be replaced with some builder to prevent bugs and organize code in a better way
    public static void insert(String user, String userRole, String object, long timestamp) {
        SQLiteDatabase writableDBInstance = AppApplication.getWritableDBInstance();
        ContentValues contentValues = new ContentValues();
        insertValue(contentValues,EventsTable.USER, user);
        insertValue(contentValues,EventsTable.USER_ROLE, userRole);
        insertValue(contentValues,EventsTable.OBJECT, object);
        insertValue(contentValues,EventsTable.TIMESTAMP, timestamp);
        writableDBInstance.insert(EventsTable.TABLE_NAME, TABLE_COLUMS_LIST, contentValues);
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

    public static Cursor getAllRows() {
        return AppApplication.getWritableDBInstance().rawQuery("SELECT * FROM " + EventsTable.TABLE_NAME  + " ORDER BY " + TIMESTAMP , null);
    }

    public static Cursor getAllRows(String columnName, boolean groupItems) {
        StringBuilder sqlQueryBuilder = new StringBuilder();
        sqlQueryBuilder.append("SELECT " + columnName + " FROM " + EventsTable.TABLE_NAME);

        if (groupItems){
            sqlQueryBuilder.append(" GROUP BY " + columnName);
        }

        sqlQueryBuilder.append(" ORDER BY " + columnName);
        return AppApplication.getWritableDBInstance().rawQuery(
                sqlQueryBuilder.toString(), null);
    }

    public static Cursor getRowsBetween(Object first, Object second) {
        String query = "SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE " + EventsTable._ID + " BETWEEN " + first + " AND " + second;
        return AppApplication.getReadableDBInstance().rawQuery(query, null);
    }
}
