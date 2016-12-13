package com.example.pc.dissertation.db.tables;

import java.util.Arrays;
import java.util.List;

import static com.example.pc.dissertation.db.tables.DBTable.COMMA_SEP;
import static com.example.pc.dissertation.db.tables.DBTable.TEXT_TYPE;

public class StructuredLogTable{

    public static final String TABLE_NAME = "STRUCTURED_LOG_TABLE";
    public static String _ID = "_id";
    public static String PROCESS = "process";
    public static String ACTIVITY = "activity";
    public static String USER = "user";
    public static String USER_ROLE = "user_role";
    public static String RESOURCE = "resource";
    public static String TIMESTAMP = "timestamp";
    public static String STATUS = "status";
    private static List<String> ALL_COLUMNS_NAMES = Arrays.asList(PROCESS, ACTIVITY, USER, USER_ROLE,
            RESOURCE, TIMESTAMP, STATUS);

    public static String getCreateStatement() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                PROCESS + TEXT_TYPE + COMMA_SEP +
                ACTIVITY + TEXT_TYPE + COMMA_SEP +
                USER + TEXT_TYPE + COMMA_SEP +
                USER_ROLE + TEXT_TYPE + COMMA_SEP +
                RESOURCE + TEXT_TYPE + COMMA_SEP +
                TIMESTAMP + TEXT_TYPE + COMMA_SEP +
                STATUS + TEXT_TYPE + " )";
    }

    public static String getDeleteStatement() {
        return "DROP TABLE " + TABLE_NAME;
    }

    public static List<String> getAllColumnsNames() {
        return ALL_COLUMNS_NAMES;
    }
}
