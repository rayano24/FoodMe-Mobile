package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class DislikeRestaurantStepDefs {

    private static int timeout = 2000;

    public static void givenViewingRestaurant(Solo solo) {
        CucumberActionSteps.clickRestaurant(solo);
    }

    //I click the dislike button
    public static void whenClickDislike(Solo solo){
        CucumberActionSteps.clickDislike(solo);
    }

    //The restaurant is disliked
    public static boolean thenDisliked(Solo solo){
        return CucumberActionSteps.checkDisliked(solo);
    }
}

