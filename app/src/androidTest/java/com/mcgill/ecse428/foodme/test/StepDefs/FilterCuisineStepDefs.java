package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class FilterCuisineStepDefs {

    public static void givenNotLogIn(Solo solo) {
        CucumberActionSteps.accessAppWithoutLogginIn(solo);
    }

    public static void when(Solo solo, String cuisine){
        CucumberActionSteps.filterByCuisine(solo, cuisine);
    }

    //TODO
    public static boolean then(Solo solo, String cuisine){
        return solo.waitForText("");
    }
}
