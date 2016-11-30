package com.example.pc.dissertation;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pc.dissertation.db.DBHelperImpl;

/**
 * Created by PC on 30.11.2016.
 */

public class AppApplication extends Application{

    private static  SQLiteOpenHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
    }

    private void initDB() {
        dbHelper = new DBHelperImpl(getApplicationContext());
        dbHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getWritableDBInstance(){
        return dbHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDBInstance(){
        return dbHelper.getReadableDatabase();
    }
}

