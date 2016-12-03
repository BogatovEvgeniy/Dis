package com.example.pc.dissertation.services;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.pc.dissertation.AppApplication;
import com.example.pc.dissertation.BPLog;
import com.example.pc.dissertation.LogParsingListener;
import com.example.pc.dissertation.db.tables.RawLodTableDAO;
import com.example.pc.dissertation.db.tables.RawLogTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by PC on 26.11.2016.
 */
public class DefaultParseService extends ParseService {

    private static final java.lang.String PARSER_THREAD = "parser_thread";

    public DefaultParseService() {
        super(DefaultParseService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void parseLog() throws Exception {
        HandlerThread handlerThread = new HandlerThread(PARSER_THREAD, Thread.NORM_PRIORITY);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                File file = new File(log.getFilePath());
                BPLog.LogStructureBuilder rawStuctBuilder = new BPLog.LogStructureBuilder();
                if (file.exists()) {
                    try {
                        BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        String currentRecord;
                        while ((currentRecord = fis.readLine()) != null) {
                            parseInRecordElements(rawStuctBuilder, currentRecord);
                            rawStuctBuilder.addLine();
                        }
                        List<List<String>> rawLog = rawStuctBuilder.build();
                        DefaultParseService.this.getLog().setRawLog(rawLog);
                        storeRawLogInDB(rawLog);
                        logParsingListener.onValidationFinish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void storeRawLogInDB(List<List<String>> rawLog) {
        AppApplication.getWritableDBInstance().execSQL(RawLogTable.getInstance(rawLog).getCreateStatement());
        for (List<String> row : rawLog) {
            RawLodTableDAO.insert(row);
        }
    }

    private void parseInRecordElements(BPLog.LogStructureBuilder rawStuctBuilder, String parseLine) {
        System.out.println("Parse element");
        int elementOffset = 0;
        int lastFoundElementSeparator = 0;
        while (lastFoundElementSeparator != -1 && lastFoundElementSeparator != parseLine.length() - 1) {
            int newFoundElementSeparatorIndex = parseLine.indexOf(elementSeparator, elementOffset);
            String element;
            if (newFoundElementSeparatorIndex == -1) {
                element = parseLine.substring(elementOffset, parseLine.length() - 1);
            } else {
                element = parseLine.substring(elementOffset, newFoundElementSeparatorIndex);
            }
            rawStuctBuilder.addElement(element);
            elementOffset = newFoundElementSeparatorIndex + 1;
            lastFoundElementSeparator = newFoundElementSeparatorIndex;
        }
    }
}
