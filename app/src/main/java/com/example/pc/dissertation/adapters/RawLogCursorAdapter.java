package com.example.pc.dissertation.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pc.dissertation.BPLog;
import com.example.pc.dissertation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 03.12.2016.
 */
public class RawLogCursorAdapter extends CursorAdapter {

    private Map<Integer, String> elementTypeColumnMap = new HashMap<>();

    public RawLogCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout rowLayout = (LinearLayout) layoutInflater.inflate(R.layout.log_row, null);
        ViewHolder viewHolder = new ViewHolder();
        for (int i = 0; i < cursor.getColumnNames().length; i++) {
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.row_element, null);
            rowLayout.addView(linearLayout);
            View dividerLayout = layoutInflater.inflate(R.layout.list_horizontal_devider, null);
            rowLayout.addView(dividerLayout);

            TextView elementTextView = (TextView) linearLayout.findViewById(R.id.elementView);
            Spinner spinnerView = (Spinner) linearLayout.findViewById(R.id.structElement);
            setUpSpinner(context, spinnerView);
            spinnerView.setTag(i);
            viewHolder.elementsViewList.add(elementTextView);
            viewHolder.spinnerViewList.add(spinnerView);
        }

        rowLayout.setTag(viewHolder);
        return rowLayout;
    }

    private Spinner setUpSpinner(Context context, Spinner spinnerView) {
        BPLog.LogStructElem[] logStructElems = BPLog.LogStructElem.values();
        String[] values = new String[logStructElems.length];
        for (int i = 0; i < logStructElems.length; i++) {
            values[i] = logStructElems[i].toString();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, values);
        spinnerView.setAdapter(arrayAdapter);
        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer columnNum = (Integer) view.getTag();
                BPLog.LogStructElem elem = BPLog.LogStructElem.values()[position];
                elementTypeColumnMap.put(columnNum, elem.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return spinnerView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        for (int i = 0; i < viewHolder.elementsViewList.size(); i++) {
            viewHolder.elementsViewList.get(i).setText(cursor.getString(i));
            if (cursor.isFirst()) {
                if (i == 0) {
                    viewHolder.spinnerViewList.get(i).setVisibility(View.GONE);
                } else {
                    viewHolder.spinnerViewList.get(i).setVisibility(View.VISIBLE);
                }
            } else {
                viewHolder.spinnerViewList.get(i).setVisibility(View.GONE);
            }
        }
    }

    Map<Integer,String> getElementColumnMap(){
        return elementTypeColumnMap;
    }

    class ViewHolder {
        List<TextView> elementsViewList = new ArrayList<>();
        List<Spinner> spinnerViewList = new ArrayList<>();
    }
}

