package com.example.pc.dissertation;

import android.os.Handler;
import android.os.HandlerThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by PC on 26.11.2016.
 */
public class DefaultLogParser extends LogParser {

    private static final java.lang.String PARSER_THREAD = "parser_thread";
    public static final String LINE_SEPARATOR = "/n";

    public DefaultLogParser(LogParsingListener logParsingListener) {
        super(logParsingListener);
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
                        String lastReadLine;
                        while ((lastReadLine = fis.readLine()).length() > 0) {
                            int lastFoundLineSeparatorIndex = 0;
                            while(lastFoundLineSeparatorIndex != -1){
                                String parseData = lastReadLine.substring(lastFoundLineSeparatorIndex, lastReadLine.length());
                                lastFoundLineSeparatorIndex = parseInLineElements(rawStuctBuilder, parseData);
                                rawStuctBuilder.addLine();
                            }
                        }
                        DefaultLogParser.this.getLog().setRawLog(rawStuctBuilder.build());
                        logParsingListener.onValidationFinish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private int parseInLineElements(BPLog.LogStructureBuilder rawStuctBuilder, String lastReadLine) {
        int elementOffset = 0;
        int lastFoundElementSymbIndex = 0;
        // TODO suppose that this line separator was used
        int lastFoundLineSeparator = lastReadLine.indexOf(LINE_SEPARATOR);
        while (lastFoundElementSymbIndex < lastFoundLineSeparator){
            int elementSeparatorIndex = lastReadLine.indexOf(elementSeparator, elementOffset);
            rawStuctBuilder.addElement(lastReadLine.substring(elementOffset, elementSeparatorIndex));
            elementOffset = elementSeparatorIndex + 1;
        }

        return lastFoundLineSeparator;
    }
}
