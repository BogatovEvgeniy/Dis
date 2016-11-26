package com.example.pc.dissertation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by PC on 05.11.2016.
 */
public class Method1Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_GET_LOG_FILE = 0x0001;
    private static final int REQUEST_STORAGE_PERM_CODE = 0x0002;
    private static final java.lang.String GET_FILE_THREAD_NAME = "GET_FILE_BY_URI";
    private FrameLayout logFrame;
    private BPLog log;
    private LogParser logParser = new DefaultLogParser(new LogParsingListenerImpl(this));
    private LinearLayout progressLayout;
    private String elementSeparator;
    private String lineSeparator;
    private TextView elementSeparatorEt;
    private TextView lineSeparatorEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploading_parce_data_layout);
        findViewById(R.id.button_next).setOnClickListener(onNextBtnClick());
        findViewById(R.id.button_parse).setOnClickListener(onParseBtnClick());
        findViewById(R.id.get_file_button).setOnClickListener(onGetFileBtnClick());
        elementSeparatorEt = ((TextView)findViewById(R.id.elementSeparatorSymb));
        lineSeparatorEt = ((TextView)findViewById(R.id.lineSeparatorSymb));
        progressLayout = (LinearLayout) findViewById(R.id.progress);
        logFrame = (FrameLayout) findViewById(R.id.logFrame);
    }

    private View.OnClickListener onParseBtnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementSeparator = elementSeparatorEt.getText().toString();
                lineSeparator = lineSeparatorEt.getText().toString();
                if (elementSeparator != null && lineSeparator != null) {
                    logParser.startParsing(log, elementSeparator, lineSeparator);
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener onNextBtnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Method1Activity.this, SelectStrategyActivity.class));
            }
        };
    }

    @NonNull
    private View.OnClickListener onGetFileBtnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionsGranted()) {
                    getFile();
                } else {
                    ActivityCompat.requestPermissions(Method1Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERM_CODE);
                }
            }
        };
    }

    private boolean isPermissionsGranted() {
        int permissionState = ContextCompat.checkSelfPermission(Method1Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void getFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("file/*");
        startActivityForResult(intent, REQUEST_CODE_GET_LOG_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERM_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFile();
            } else {
                Log.d(getPackageName(), "User doesn't grant permissions");
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GET_LOG_FILE
                && resultCode == RESULT_OK && data != null) {
            if (data.getExtras() != null) {
                Log.d(getPackageName(), data.getExtras().toString());
            } else {
                File file = new File(data.getData().getPath());
                log = new BPLog(file);
            }
        }
    }

    private static class LogParsingListenerImpl implements LogParsingListener {
        private final WeakReference<Method1Activity> weakRef;

        public LogParsingListenerImpl(Method1Activity method1Activity) {
            weakRef = new WeakReference<Method1Activity>(method1Activity);
        }

        @Override
        public void onValidationStart() {
            defaultProcessingForCallback("Processing start", true);
        }

        @Override
        public void onValidationFinish() {
            defaultProcessingForCallback("Processing done", false);
        }

        @Override
        public void onValidationError(Exception e) {
            defaultProcessingForCallback("Processing error", false);
        }

        private void defaultProcessingForCallback(String logMessage, boolean showProgress) {
            Method1Activity method1Activity = weakRef.get();
            if (method1Activity == null) {
                return;
            }

            if (showProgress) {
                method1Activity.startProgress();
            } else {
                method1Activity.stopProgress();
            }

            Toast.makeText(method1Activity, logMessage, Toast.LENGTH_LONG);
            Log.d(method1Activity.getPackageName(), logMessage);
        }
    }

    private void stopProgress() {
        if (progressLayout.getVisibility() == View.VISIBLE) {
            progressLayout.setVisibility(View.GONE);
        }
    }

    private void startProgress() {
        if (progressLayout.getVisibility() == View.GONE) {
            progressLayout.setVisibility(View.VISIBLE);
        }
    }
}
