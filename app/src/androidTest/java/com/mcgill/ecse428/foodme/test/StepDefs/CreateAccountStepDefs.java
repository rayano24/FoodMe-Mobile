package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class CreateAccountStepDefs {

    private static int timeout = 200;

    //I am on the register form
    public static void givenRegisterForm(Solo solo) {

        //If we're not on the register form, go back to it
        if (CucumberActionSteps.loggedIn(solo))
            CucumberActionSteps.signOut(solo);
    }

    public static void givenHaveAccount(Solo solo) {
        //TODO DO SOMETHING HERE OR REMOVE FROM GHERKIN FILE
    }

    //When I enter an <email>, a <username> and a <password>
    public static void when(Solo solo, String firstname, String lastname, String username, String email, String password) {
        CucumberActionSteps.register(solo, firstname, lastname, username, email, password);
    }

    //I should have an account
    public static boolean validThen(Solo solo) {
//        return solo.waitForText("Account successfully created", 1, timeout);
        return !CucumberActionSteps.loggedIn(solo);
    }

    //I should not be able to create an account
    public static boolean invalidThen(Solo solo) {

        boolean b3 = solo.waitForText("password must be longer than 6", 1, timeout, false, true);
        boolean b4 = solo.waitForText("valid email address", 1, timeout, false, true);
        boolean b1 = solo.waitForText("User already exists", 1, timeout, false, true);
        boolean b2 = solo.waitForText("This field is required", 1, timeout, false, true);

        return (b1 || b2 || b3 || b4);
    }
}
