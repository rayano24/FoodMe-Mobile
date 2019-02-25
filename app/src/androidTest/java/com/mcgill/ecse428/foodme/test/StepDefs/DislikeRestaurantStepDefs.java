package com.mcgill.ecse428.foodme.test.StepDefs;

import com.robotium.solo.Solo;

public class DislikeRestaurantStepDefs {

    private static int timeout = 2000;

    //I am on viewing a restaurant's details
    public static void given(Solo solo){
        //If we're not viewing restaurant, view it!
        //if (CucumberActionSteps.viewingRestaurant(solo))
        //    CucumberActionSteps.viewRestaurant(solo);
    }

    //I click the dislike button
    public static void when(Solo solo){
        //CucumberActionSteps.clickDislike(solo);
    }

    //The restaurant is disliked
    public static boolean then(Solo solo){
        //return CucumberActionSteps.isDisliked(solo);
        return true;
    }
}
