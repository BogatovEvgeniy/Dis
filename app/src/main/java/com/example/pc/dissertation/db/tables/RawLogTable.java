package com.example.pc.dissertation.db.tables;

import com.example.pc.dissertation.AppApplication;

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
      if (isExists()){
          AppApplication.getWritableDBInstance().execSQL(getDeleteStatement());
      }
        StringBuffer sb = new StringBuffer();
        sb.append(  "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY,");
        sb.append(ELEMENT_PREFFIX + 0 + " TEXT");
        for (int i = 1; i < rawLog.get(0).size(); i++){
            sb.append(", " + ELEMENT_PREFFIX + i + " TEXT");
        }
        sb.append(")");
        COLUMNS_COUNT = rawLog.get(0).size();

        return sb.toString();
    }

    private boolean isExists() {
        try {
            return RawLodTableDAO.readAllRows().getCount() > 0;
        } catch (Exception e){
            return false;
        }
    }


    @Override
    public String getDeleteStatement() {
        return  "DROP TABLE " + TABLE_NAME;
    }
}
