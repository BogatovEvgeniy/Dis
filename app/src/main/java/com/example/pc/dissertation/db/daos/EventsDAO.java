package com.example.pc.dissertation.db.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pc.dissertation.AppApplication;
import com.example.pc.dissertation.db.tables.EventsTable;

import static com.example.pc.dissertation.db.daos.Utils.insertValue;

public class EventsDAO {

    // TODO could be replaced with some builder to prevent bugs and organize code in a better way
    public static void insert(String actionsName, String user, String userRole, String object, long timestamp) {
        SQLiteDatabase writableDBInstance = AppApplication.getWritableDBInstance();
        ContentValues contentValues = new ContentValues();
        insertValue(contentValues,EventsTable.ACTIVITY, actionsName);
        insertValue(contentValues,EventsTable.USER, user);
        insertValue(contentValues,EventsTable.USER_ROLE, userRole);
        insertValue(contentValues,EventsTable.RESOURCE, object);
        insertValue(contentValues,EventsTable.TIMESTAMP, timestamp);
        writableDBInstance.insert(EventsTable.TABLE_NAME, null, contentValues);
    }

    public static void select(String rowID) {
        SQLiteDatabase writableDBInstance = AppApplication.getWritableDBInstance();
        ContentValues content_values = new ContentValues();
        content_values.put(EventsTable._ID, rowID);
        writableDBInstance.insert(EventsTable.TABLE_NAME, null, content_values);
    }

    public static void delete(String rowID) {
        SQLiteDatabase writableDBInstance = AppApplication.getWritableDBInstance();
        ContentValues content_values = new ContentValues();
        content_values.put(EventsTable._ID, rowID);
        writableDBInstance.insert(EventsTable.TABLE_NAME, null, content_values);
    }

    public static Cursor getAllRows() {
        return AppApplication.getWritableDBInstance().rawQuery("SELECT * FROM " + EventsTable.TABLE_NAME  + " ORDER BY " + EventsTable.TIMESTAMP , null);
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
        String query = "SELECT * FROM " + EventsTable.TABLE_NAME + " WHERE " + EventsTable._ID + " BETWEEN '" + first + "' AND '" + second + "'";
        return AppApplication.getReadableDBInstance().rawQuery(query, null);
    }
}
