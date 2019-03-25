package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class LikeRestaurantStepDefs {

    //I click the dislike button
    public static void when(Solo solo){
        CucumberActionSteps.clickLike(solo);
    }

    //The restaurant is disliked
    public static boolean then(Solo solo){
        return CucumberActionSteps.checkLiked(solo);
    }
}
