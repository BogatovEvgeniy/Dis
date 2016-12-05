package com.example.pc.dissertation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pc.dissertation.db.tables.EventsDAO;
import com.example.pc.dissertation.db.tables.EventsTable;
import com.example.pc.dissertation.db.tables.RawLodTableDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 05.11.2016.
 */
public class SelectStrategyActivity extends Activity {

    public static final String TAG = SelectStrategyActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strategy_activity);
        View.OnClickListener defaultListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectStrategyActivity.this, StrategySetUpActivity.class));
            }
        };
        findViewById(R.id.object_analyze).setOnClickListener(defaultListener);
        findViewById(R.id.user_analyze).setOnClickListener(defaultListener);
        findViewById(R.id.object_users_analyze).setOnClickListener(defaultListener);

        if (getIntent().getExtras() != null) {
            final HashMap serializable = (HashMap) (getIntent().getExtras().getSerializable(Method1Activity.ELEMENTS_COLUMN_MAP));
            if (serializable != null) {
                Log.d(SelectStrategyActivity.class.getName(), serializable.toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Cursor cursor = RawLodTableDAO.readAllRows();
                        Utils.printCursorInLog(EventsDAO.getAllRows());
                        List<String> tableCols = EventsTable.getAllCollumnsName();
                        int[] orderOfInsertionValues = getInsertionArr(tableCols);
                        insertRowIntoEventTable(cursor, orderOfInsertionValues);
                        // Print result
                        Utils.printCursorInLog(EventsDAO.getAllRows());
                    }

                    private int[] getInsertionArr(List<String> tableCols) {
                        int[] orderOfInsertionValues = new int[EventsTable.getAllCollumnsName().size()];
                        for (int tableColIndex = 0; tableColIndex < tableCols.size(); tableColIndex++) {
                            int foundedPos = getColumnNumForElement(serializable, tableCols.get(tableColIndex));
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

                    private void insertRowIntoEventTable(Cursor cursor, int[] orderOfInsertionValues) {
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
                                EventsDAO.insert(
                                        processIndex == -1 ? null : cursor.getString(processIndex),
                                        userIndex == -1 ? null : cursor.getString(userIndex),
                                        userRoleIndex == -1 ? null : cursor.getString(userRoleIndex),
                                        objectIndex == -1 ? null : cursor.getString(objectIndex),
                                        timestumpIndex == -1 ? null : cursor.getString(timestumpIndex),
                                        statusIndex == -1 ? null : cursor.getString(statusIndex));
                            } while (cursor.moveToNext());
                            Log.d(TAG, "The insertion was madden " + i + " times");
                        }
                    }
                }).start();
            }
        }
    }
}
