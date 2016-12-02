package com.example.pc.dissertation.db.tables;

import java.util.List;

/**
 * Created by PC on 02.12.2016.
 */

public class RawLogTable extends DBTable {

    public static final String TABLE_NAME = "RowLog";
    private static final String _ID = "_id";
    public static final String ELEMENT_PREFFIX = "ELEMENT_";
    public static int COLUMNS_COUNT;
    private static RawLogTable INSTANCE;
    private final List<List<String>> rawLog;

    private RawLogTable(List<List<String>> rawLog) {
        this.rawLog = rawLog;
    }

    public static RawLogTable getInstance(List<List<String>> rawLog){
        if (INSTANCE == null){
            INSTANCE = new RawLogTable(rawLog);
        }
        return INSTANCE;
    }

    @Override
    public String getCreateStatement() {
        StringBuffer sb = new StringBuffer();
        sb.append(  "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY,");
        sb.append(ELEMENT_PREFFIX + 0 + " TEXT");
        for (int i = 1; i < rawLog.get(0).size(); i++){
            sb.append(", " + ELEMENT_PREFFIX + i + " TEXT");
        }
        COLUMNS_COUNT = rawLog.get(0).size();
        return sb.toString();
    }

    @Override
    public String getDeleteStatement() {
        return  "DROP TABLE " + TABLE_NAME;
    }
}
