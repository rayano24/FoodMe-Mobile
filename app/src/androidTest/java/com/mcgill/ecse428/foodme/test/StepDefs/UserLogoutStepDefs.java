package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class UserLogoutStepDefs {

    //I am logged in
    public static void given(Solo solo) {

        //If we're not logged in, log in first
        if (!CucumberActionSteps.loggedIn(solo))
            CucumberActionSteps.logInAsAdmin(solo);
    }

    //I select the logout option
    public static void when(Solo solo){

        CucumberActionSteps.signOut(solo);
    }

    //I should be able to successfully logout
    public static boolean then(Solo solo){

        return !CucumberActionSteps.loggedIn(solo);
    }
}
