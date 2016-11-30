package com.example.pc.dissertation.db.tables;

/**
 * Created by PC on 30.11.2016.
 */
public abstract class DBTable {
    abstract String getCreateStatement();
    abstract String getDeleteStatement();
}
