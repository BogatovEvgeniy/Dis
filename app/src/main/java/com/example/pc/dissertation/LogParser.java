package com.example.pc.dissertation;

/**
 * Created by PC on 26.11.2016.
 */
public abstract class LogParser {

    protected LogParsingListener logParsingListener;
    protected BPLog log;
    protected String elementSeparator;
    protected String lineSeparator;

    public LogParser(LogParsingListener logParsingListener) {
        this.logParsingListener = logParsingListener;
    }

    void startParsing(BPLog log, String elementSeparator, String lineSeparator) {
        this.log = log;
        this.elementSeparator = elementSeparator;
        this.lineSeparator = lineSeparator;

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
        logParsingListener.onValidationFinish();
    }

    protected abstract void parseLog() throws Exception;

}
