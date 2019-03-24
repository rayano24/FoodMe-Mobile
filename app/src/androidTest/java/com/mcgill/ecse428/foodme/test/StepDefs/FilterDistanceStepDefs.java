package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class FilterDistanceStepDefs {

    public static void when(Solo solo, int distance) {
        CucumberActionSteps.filterByDistance(solo, distance);
    }

    //TODO
    public static boolean then(Solo solo, int distance) { return solo.waitForText(""); }
}
