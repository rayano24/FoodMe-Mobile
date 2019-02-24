package com.mcgill.ecse428.foodme.test;

import com.robotium.solo.Solo;

public class CreateAccountStepDefs {

    private static int timeout = 2000;

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
    public static void when(Solo solo, String username, String email, String phone, String password) {
        CucumberActionSteps.register(solo, username, email, phone, password);
    }

    //I should have an account
    public static boolean validThen(Solo solo) {
        return solo.waitForText("Account successfully created", 1, timeout);
    }

    //I should not be able to create an account
    public static boolean invalidThen(Solo solo) {
        return !solo.waitForText("Account could not be created", 1, timeout);
    }
}
