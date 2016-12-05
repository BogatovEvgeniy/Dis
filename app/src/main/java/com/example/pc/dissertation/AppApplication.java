package com.example.pc.dissertation;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.pc.dissertation.db.DBHelperImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by PC on 30.11.2016.
 */

public class AppApplication extends Application{

    private static  SQLiteOpenHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
        getDB();
    }

    private void getDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/" +DBHelperImpl.DATABASE_NAME;
                String backupDBPath = "backupname.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
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

