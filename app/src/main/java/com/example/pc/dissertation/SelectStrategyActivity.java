package com.example.pc.dissertation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by PC on 05.11.2016.
 */
public class SelectStrategyActivity extends Activity{

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
    }
}
