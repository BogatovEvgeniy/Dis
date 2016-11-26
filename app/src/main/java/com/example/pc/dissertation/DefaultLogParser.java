package com.example.pc.dissertation;

import android.os.Handler;
import android.os.HandlerThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PC on 26.11.2016.
 */
public class DefaultLogParser extends LogParser {

    private static final java.lang.String PARSER_THREAD = "parser_thread";

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

                                           if (file.exists()) {
                                               IOException exception = null;
                                               try {
                                                   FileInputStream fis = new FileInputStream(file);
                                                   int bufferLength = 1024;
                                                   byte[] buffer = new byte[bufferLength];
                                                   int offset = 0;
                                                   int readSymbols;
                                                   while ((readSymbols = fis.read(buffer, offset, bufferLength)) > 0) {
                                                                                                              // Search patterns
                                                       Pattern elementPattern = Pattern.compile("[" + elementSeparator + "]");
                                                       Pattern lineEndPattern = Pattern.compile("[" + lineSeparator + "]");

                                                       // Matchers
                                                       Matcher lineMatcher = lineEndPattern.matcher(new String(buffer));
                                                       Matcher elementMatcher = lineEndPattern.matcher(new String(buffer));

                                                       //Search algorithm
                                                       int elementOffsetIndex = 0;
                                                       int lineOffsetIndex = lineMatcher.start();
                                                       int newElementOffsetIndex = 0;
                                                       while ((newElementOffsetIndex = elementMatcher.start(elementOffsetIndex)) < lineOffsetIndex) {

                                                       }

                                                   }

                                               }
                                           }catch(IOException e){
                                               exception = e;
                                               e.printStackTrace();
                                           }finally{
                                               if (exception != null) {
                                                   logParsingListener.onValidationError(exception);
                                               }
                                           }
                                       }
                                   }

        );
    }
}
