package com.example.pc.dissertation;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.example.pc.dissertation.db.daos.*;
import com.example.pc.dissertation.db.tables.StructuredLogTable;
import com.example.pc.dissertation.models.BusinessProcess;

public class Method2Step2Activity extends Activity implements LoaderManager.LoaderCallbacks {

    private static final int BUILD_STRUCT_LOG = 0x0001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.method2activity_step2);
        getLoaderManager().initLoader(BUILD_STRUCT_LOG, null, this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String searchVal = extras.getString(Method2Step1Activity.EXTRAS_SEARCH_VAL);
            String startItem = extras.getString(Method2Step1Activity.EXTRAS_START_ITEM);
            String endItem = extras.getString(Method2Step1Activity.EXTRAS_END_ITEM);
            createStructLog();
            buildStructuredLog(searchVal, startItem, endItem);
        }
    }

    private void createStructLog() {
        if (com.example.pc.dissertation.db.daos.Utils.checkTableExistance(StructuredLogTable.TABLE_NAME)){
            StructuredLogDAO.delete();
        }
        StructuredLogDAO.create();
    }

    private void buildStructuredLog(String searchVal, String startItem, String endItem) {
        Cursor cursor = StructuredLogDAO.findProcessEvents(searchVal, startItem, endItem);
        BusinessProcess businessProcess = BusinessProcess.createFromCursor(cursor);
        StructuredLogDAO.insertProcess(businessProcess);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
