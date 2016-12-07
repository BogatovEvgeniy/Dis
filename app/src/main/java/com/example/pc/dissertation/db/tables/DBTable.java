package com.example.pc.dissertation.db.tables;

public abstract class DBTable {
    static final String TEXT_TYPE = " TEXT";
    static final String COMMA_SEP = ",";

    abstract String getCreateStatement();
    abstract String getDeleteStatement();
}
