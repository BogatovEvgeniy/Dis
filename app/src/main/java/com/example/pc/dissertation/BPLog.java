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


    public String getFilePath() {
        return filePath;
    }

    private enum LogStructElem {
        CUSTOM, PROCESS, ACTIVITY, EVENT, USER, USER_ROLE, OBJECT, TIMESTAMP, STATUS;
    }
}
