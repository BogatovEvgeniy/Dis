package com.example.pc.dissertation.db.tables;

public abstract class DBTable {
    abstract String getCreateStatement();
    abstract String getDeleteStatement();
}
