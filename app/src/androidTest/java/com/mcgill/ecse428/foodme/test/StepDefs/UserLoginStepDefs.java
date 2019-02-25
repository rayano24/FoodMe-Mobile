package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class UserLoginStepDefs {

    private static int timeout = 2000;

    //I am on the sign in form
    public static void given(Solo solo){

        //If we're not on the sign in page, go back to it
        if (CucumberActionSteps.loggedIn(solo))
            CucumberActionSteps.signOut(solo);
    }

    //TODO DO SOMETHING FOR "I HAVE AN EXISTING ACCOUNT"

    //I enter my <username> and my <password>
    //I enter an invalid combination of <username> and <password>
    public static void when(Solo solo, String username, String password){

        CucumberActionSteps.logIn(solo, username, password);
    }

    //I should be able to successfully login
    public static boolean then(Solo solo){

        return CucumberActionSteps.loggedIn(solo);
    }

    //I should be prompted to re-enter my credentials
    public static boolean and(Solo solo){

        return solo.waitForText("Invalid username or password", 1, timeout)
                || solo.waitForText("This field is required", 1, timeout);
    }
}
