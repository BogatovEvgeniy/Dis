package com.example.pc.dissertation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pc.dissertation.db.daos.EventsDAO;

import java.util.LinkedList;
import java.util.List;

public class Method2Step1Activity extends Activity {

    public static final String EXTRAS_SEARCH_VAL = "searchVal";
    public static final String EXTRAS_START_ITEM = "startItem";
    public static final String EXTRAS_END_ITEM = "endItem";
    private Spinner selectSearchStructElement;
    private BPLog.LogStructElem selectedStructElem;
    private Spinner startItemSpinner;
    private Spinner endItemSpinner;
    private String startItem;
    private String endItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.method2activity_step1);
        selectSearchStructElement = (Spinner) findViewById(R.id.selectSearchStructElement);
        setUpSpinner();
        startItemSpinner = (Spinner) findViewById(R.id.startItemSpinner);
        setUpStartItemSpinner();
        endItemSpinner = (Spinner) findViewById(R.id.endItemSpinner);
        setUpEndItemSpinner();
        findViewById(R.id.startBuildStructLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Method2Step1Activity.this, Method2Step2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString(EXTRAS_SEARCH_VAL, selectedStructElem.name());
                bundle.putString(EXTRAS_START_ITEM, startItem);
                bundle.putString(EXTRAS_END_ITEM, endItem);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void setUpSpinner() {
        BPLog.LogStructElem[] logStructElems = BPLog.LogStructElem.values();
        String[] values = new String[logStructElems.length];
        for (int i = 0; i < logStructElems.length; i++) {
            values[i] = logStructElems[i].toString();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, values);
        selectSearchStructElement.setAdapter(arrayAdapter);
        selectSearchStructElement.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                            long id) {
                        selectedStructElem = BPLog.LogStructElem.values()[position];
                        if (!selectedStructElem.equals(BPLog.LogStructElem.CUSTOM)) {
                            updateElementSelectionSpinners();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    private void updateElementSelectionSpinners() {
        List<String> selectItemsSet = getColumnItemsFromDB(selectedStructElem.name());
        startItemSpinner.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectItemsSet));
        endItemSpinner.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectItemsSet));
        startItemSpinner.invalidate();
        endItemSpinner.invalidate();
    }

    private List<String> getColumnItemsFromDB(String name) {
        Cursor cursor = EventsDAO.getAllRows(name, true);
        List<String> values = new LinkedList<>();
        if (cursor.moveToFirst()) {
            do {
                values.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return values;
    }

    private void setUpStartItemSpinner() {
        String[] values = new String[]{};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, values);
        startItemSpinner.setAdapter(arrayAdapter);
        startItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startItem = ((TextView) view).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpEndItemSpinner() {
        String[] values = new String[]{};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, values);
        startItemSpinner.setAdapter(arrayAdapter);
        startItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endItem = ((TextView) view).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
