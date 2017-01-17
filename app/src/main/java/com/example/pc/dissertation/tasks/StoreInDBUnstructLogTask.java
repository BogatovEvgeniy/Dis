package com.example.pc.dissertation.tasks;

import android.database.Cursor;
import android.util.Log;

import com.example.pc.dissertation.Utils;
import com.example.pc.dissertation.db.daos.EventsDAO;
import com.example.pc.dissertation.db.daos.RawLodTableDAO;
import com.example.pc.dissertation.db.tables.EventsTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreInDBUnstructLogTask implements Runnable {
    public static final String TAG = StoreInDBUnstructLogTask.class.getName();
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss.SSSZ";
    private HashMap strucElementIndexMap;
    private String dateTimeFormat;

    public StoreInDBUnstructLogTask(HashMap strucElementIndexMap, String dateTimeFormat) {
        this.strucElementIndexMap = strucElementIndexMap;
        this.dateTimeFormat = dateTimeFormat == null ? DATE_TIME_FORMAT : dateTimeFormat;
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
        int i = 0;
        while (cursor.moveToNext()) {
            i++;
            // TODO It's terrible but I have no time to make right decision is it should be builder, string arr, structure or smthg. else
            int activityIndex = orderOfInsertionValues[0];
            int userIndex = orderOfInsertionValues[1];
            int userRoleIndex = orderOfInsertionValues[2];
            int objectIndex = orderOfInsertionValues[3];
            int timestumpIndex = orderOfInsertionValues[4];
            String string = cursor.getString(timestumpIndex);
            Date parse = new SimpleDateFormat(dateTimeFormat).parse(string);
            long time = parse.getTime();
            EventsDAO.insert(
                    activityIndex == -1 ? null : cursor.getString(activityIndex),
                    userIndex == -1 ? null : cursor.getString(userIndex),

                    userRoleIndex == -1 ? null : cursor.getString(userRoleIndex),
                    objectIndex == -1 ? null : cursor.getString(objectIndex),
                    timestumpIndex == -1 ? null : time);
        }
        ;
        Log.d(TAG, "The insertion was madden " + i + " times");
    }
}
