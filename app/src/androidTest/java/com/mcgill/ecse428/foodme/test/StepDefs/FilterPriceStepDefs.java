package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class FilterPriceStepDefs {

    public static void when(Solo solo, String price){
        CucumberActionSteps.filterByPrice(solo, price);
    }

    //TODO
    public static boolean then(Solo solo, String price){
        return solo.waitForText("");
    }
}
