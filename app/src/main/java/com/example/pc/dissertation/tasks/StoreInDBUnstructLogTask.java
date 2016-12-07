package com.example.pc.dissertation.tasks;

import android.database.Cursor;
import android.util.Log;

import com.example.pc.dissertation.Utils;
import com.example.pc.dissertation.db.daos.EventsDAO;
import com.example.pc.dissertation.db.tables.EventsTable;
import com.example.pc.dissertation.db.daos.RawLodTableDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreInDBUnstructLogTask implements Runnable {
    public static final String TAG = StoreInDBUnstructLogTask.class.getName();
    private HashMap strucElementIndexMap;

    public StoreInDBUnstructLogTask(HashMap strucElementIndexMap) {
        this.strucElementIndexMap = strucElementIndexMap;
    }

    @Override
    public void run() {
        Cursor cursor = RawLodTableDAO.readAllRows();
        Utils.printCursorInLog(EventsDAO.getAllRows());
        List<String> tableCols = EventsTable.getAllColumnsName();
        int[] orderOfInsertionValues = getInsertionArr(tableCols);

        try {
            insertRowIntoEventTable(cursor, orderOfInsertionValues);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Print result
        Utils.printCursorInLog(EventsDAO.getAllRows());
    }

    private int[] getInsertionArr(List<String> tableCols) {
        int[] orderOfInsertionValues = new int[EventsTable.getAllColumnsName().size()];
        for (int tableColIndex = 0; tableColIndex < tableCols.size(); tableColIndex++) {
            int foundedPos = getColumnNumForElement(strucElementIndexMap, tableCols.get(tableColIndex));
            orderOfInsertionValues[tableColIndex] = foundedPos;
        }
        return orderOfInsertionValues;
    }

    private int getColumnNumForElement(Map<String, Integer> structuredRow, String columnName) {
        for (String element : structuredRow.keySet()) {
            if (columnName.equalsIgnoreCase(element)) {
                return structuredRow.get(element);
            }
        }
        return -1;
    }

    private void insertRowIntoEventTable(Cursor cursor, int[] orderOfInsertionValues) throws ParseException {
        if (cursor.moveToFirst()) {
            int i = 0;
            do {
                i++;
                // TODO It's terrible but I have no timi to make right decision is it should be builder, string arr, structure or smthg. else
                int processIndex = orderOfInsertionValues[0];
                int userIndex = orderOfInsertionValues[1];
                int userRoleIndex = orderOfInsertionValues[2];
                int objectIndex = orderOfInsertionValues[3];
                int timestumpIndex = orderOfInsertionValues[4];
                int statusIndex = orderOfInsertionValues[5];
                EventsDAO.insert(processIndex == -1 ? null : cursor.getString(processIndex),
                        userIndex == -1 ? null : cursor.getString(userIndex),
                        userRoleIndex == -1 ? null : cursor.getString(userRoleIndex),
                        objectIndex == -1 ? null : cursor.getString(objectIndex),
                        new SimpleDateFormat("MM/dd/yyyy").parse(cursor.getString(timestumpIndex)).getTime(),
                        statusIndex == -1 ? null : cursor.getString(statusIndex));
            } while (cursor.moveToNext());
            Log.d(TAG, "The insertion was madden " + i + " times");
        }
    }
}
