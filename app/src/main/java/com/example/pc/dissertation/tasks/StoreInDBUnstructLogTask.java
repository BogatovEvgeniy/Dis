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
                // TODO It's terrible but I have no time to make right decision is it should be builder, string arr, structure or smthg. else
                int userIndex = orderOfInsertionValues[0];
                int userRoleIndex = orderOfInsertionValues[1];
                int objectIndex = orderOfInsertionValues[2];
                int timestumpIndex = orderOfInsertionValues[3];
                EventsDAO.insert(
                        userIndex == -1 ? null : cursor.getString(userIndex),
                        userRoleIndex == -1 ? null : cursor.getString(userRoleIndex),
                        objectIndex == -1 ? null : cursor.getString(objectIndex),
                        timestumpIndex == -1 ? null : new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(timestumpIndex)).getTime());
            } while (cursor.moveToNext());
            Log.d(TAG, "The insertion was madden " + i + " times");
        }
    }
}
