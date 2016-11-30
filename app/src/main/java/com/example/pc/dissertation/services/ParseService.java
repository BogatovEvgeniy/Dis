package com.example.pc.dissertation.services;

import android.app.IntentService;
import android.content.Intent;

import com.example.pc.dissertation.BPLog;
import com.example.pc.dissertation.LogParsingListener;

/**
 * Created by PC on 26.11.2016.
 */
public abstract class ParseService extends IntentService{

    protected static LogParsingListener logParsingListener;
    protected static BPLog log;
    protected static String elementSeparator;
    protected static String lineSeparator;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ParseService(String name) {
        super(name);
    }

    public static void beforeParse(LogParsingListener logParsingListener, BPLog log, String elementSeparator, String lineSeparator) {
        ParseService.logParsingListener = logParsingListener;
        ParseService.log = log;
        ParseService.elementSeparator = elementSeparator;
        ParseService.lineSeparator = lineSeparator;
    }

    void startParsing() {
        System.out.println("Parser was started. Element symb: " + elementSeparator + " line symb: " + lineSeparator);
        if (logParsingListener == null) {
            return;
        }

        logParsingListener.onValidationStart();
        try {
            parseLog();

        } catch (Exception e) {
            logParsingListener.onValidationError(e);
        }
    }

    protected abstract void parseLog() throws Exception;

    public BPLog getLog() {
        return log;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        startParsing();
    }
}
