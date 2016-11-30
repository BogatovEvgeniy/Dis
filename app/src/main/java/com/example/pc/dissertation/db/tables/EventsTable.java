package com.example.pc.dissertation.db.tables;

/**
 * Created by PC on 30.11.2016.
 */

public class EventsTable{
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

    public static String getCreateStatement() {
        return  "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                PROCESS + TEXT_TYPE + COMMA_SEP +
                USER + TEXT_TYPE + COMMA_SEP +
                USER_ROLE + TEXT_TYPE + COMMA_SEP +
                OBJECT + TEXT_TYPE + COMMA_SEP +
                TIMESTAMP + TEXT_TYPE + COMMA_SEP +
                STATUS + TEXT_TYPE + " )";
    }

    public static String getDeleteStatement() {
        return  "DROP TABLE " + TABLE_NAME;
    }
}
