package com.mcgill.ecse428.foodme.test;
import android.os.Bundle;

import cucumber.api.android.CucumberInstrumentationCore;
import androidx.test.runner.AndroidJUnitRunner;

public class Instrumentation extends AndroidJUnitRunner {
    private final CucumberInstrumentationCore instrumentationCore = new CucumberInstrumentationCore(this);

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        instrumentationCore.create(arguments);
    }

    @Override
    public void onStart() {
        waitForIdleSync();
        instrumentationCore.start();
    }
}