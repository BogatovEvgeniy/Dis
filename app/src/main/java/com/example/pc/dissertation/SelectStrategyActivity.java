package com.example.pc.dissertation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pc.dissertation.tasks.StoreInDBUnstructLogTask;

import java.util.HashMap;

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
            final HashMap strucElementIndexMap = (HashMap) (getIntent().getExtras().getSerializable(Method1Activity.ELEMENTS_COLUMN_MAP));
            if (strucElementIndexMap != null) {
                Log.d(SelectStrategyActivity.class.getName(), strucElementIndexMap.toString());
                new Thread(new StoreInDBUnstructLogTask(strucElementIndexMap)).start();
            }
        }
    }
}
