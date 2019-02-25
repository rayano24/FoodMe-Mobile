package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class FilterOpenRestaurantsStepDefs {

    public static void when(Solo solo, boolean open){
        CucumberActionSteps.filterByOpenRestaurants(solo, open);
    }

    //TODO
    public static boolean then(Solo solo, boolean open){
        return solo.waitForText("");
    }
}
