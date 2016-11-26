package com.example.pc.dissertation;

/**
 * Created by PC on 26.11.2016.
 */
public interface LogParsingListener {
    void onValidationStart();
    void onValidationFinish();
    void onValidationError(Exception e);
}
