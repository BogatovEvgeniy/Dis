package com.example.pc.dissertation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 26.11.2016.
 */
public class BPLog {
    private String filePath;
    private LogParsingListener logParsingListener;
    private Map<LogStructElem, List<String>> seKnowledgeMap;
    private Map<LogStructElem, List<String>> log;
    private List<List<String>> rawLog;

    public BPLog(String filePath) {
        this.filePath = filePath;
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

    public static class LogStructureBuilder {
        List<List<String>> rawLog = new ArrayList<>();
        List<String> currentLine = new ArrayList<>();
        private int elementsCountInRow;

        void addElement(String element) {
            System.out.println("Add element:" + element + " in line:" + rawLog.size() + 1);
            currentLine.add(element);
        }

        void addLine() {
            if (elementsCountInRow == 0) {
                elementsCountInRow = currentLine.size();
            } else {
                if (currentLine.size() == elementsCountInRow) {
                    throw new IllegalArgumentException("New line size doesn't equals to previous lines");
                }
            }
            currentLine.clear();
            System.out.println("Add line:" + (rawLog.size() + 1) + " with elements:" + currentLine.toString());
            rawLog.add(currentLine);
        }

        List<List<String>> build() {
            return rawLog;
        }
    }


    public String getFilePath() {
        return filePath;
    }

    private enum LogStructElem {
        CUSTOM, PROCESS, ACTIVITY, EVENT, USER, USER_ROLE, OBJECT, TIMESTAMP, STATUS;
    }
}
