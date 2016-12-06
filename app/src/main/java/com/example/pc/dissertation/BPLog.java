package com.example.pc.dissertation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BPLog {
    private static BPLog INSTANCE;

    private String filePath;
    private LogParsingListener logParsingListener;
    private Map<LogStructElem, List<String>> seKnowledgeMap;
    private Map<LogStructElem, List<String>> log;
    private List<List<String>> rawLog;

    private BPLog() {
    }

    private BPLog(String filePath) {
        this.filePath = filePath;
    }

    public static BPLog getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("The instance was not initiated. Call BPLog.init(String filePath) before");
        }
        return INSTANCE;
    }

    public Map<LogStructElem, List<String>> getKnowledgeMap() {
        return seKnowledgeMap;
    }

    public List<String> getElementKnowledge(LogStructElem logStructElem) {
        return seKnowledgeMap.get(logStructElem);
    }

    public Map<LogStructElem, List<String>> getLog() {
        return log;
    }

    public void addKnowledgeOfElement(LogStructElem logStructElem, String... knowledgeArr) {
        if (knowledgeArr.length == 0) {
            return;
        }

        List<String> elementKnowledge = this.seKnowledgeMap.get(logStructElem);
        List<String> knowledgeToAdd = new ArrayList<>();
        for (int i = 0; i < knowledgeArr.length; i++) {
            if (elementKnowledge.contains(knowledgeArr[i])) {
                continue;
            }
            knowledgeToAdd.add(knowledgeArr[i]);
        }
        elementKnowledge.addAll(knowledgeToAdd);
        this.seKnowledgeMap.put(logStructElem, elementKnowledge);
    }

    public void setRawLog(List<List<String>> rawLog) {
        this.rawLog = rawLog;
    }

    public static BPLog init(String path) {
        if (INSTANCE == null) {
            INSTANCE = new BPLog(path);
        } else {
            throw new IllegalStateException("The instance was initiated before.");
        }
        return INSTANCE;
    }

    public static class LogStructureBuilder {
        List<List<String>> rawLog = new ArrayList<>();
        List<String> currentLine = new ArrayList<>();
        private int elementsCountInRow;

        public void addElement(String element) {

            Log.d(BPLog.class.getName(), "Add element:" + element + " in line:" + (rawLog.size() + 1));
            currentLine.add(element);
        }

        public void addLine() {
            if (elementsCountInRow == 0) {
                elementsCountInRow = currentLine.size();
            } else {
                if (currentLine.size() != elementsCountInRow) {
                    throw new IllegalArgumentException("New line size doesn't equals to previous lines");
                }
            }
            Log.d(BPLog.class.getName(), "Add line:" + (rawLog.size() + 1) + " with elements:" + currentLine.toString());
            rawLog.add(currentLine);
            currentLine = new ArrayList<>();
        }

        public List<List<String>> build() {
            return rawLog;
        }
    }


    public String getFilePath() {
        return filePath;
    }

    public enum LogStructElem {
        CUSTOM, PROCESS, ACTIVITY, EVENT, USER, USER_ROLE, OBJECT, TIMESTAMP, STATUS;

        @Override
        public String toString() {
            return name();
        }
    }
}
