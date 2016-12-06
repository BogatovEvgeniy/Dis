package com.example.pc.dissertation;

public interface LogParsingListener {
    void onValidationStart();
    void onValidationFinish();
    void onValidationError(Exception e);
}
