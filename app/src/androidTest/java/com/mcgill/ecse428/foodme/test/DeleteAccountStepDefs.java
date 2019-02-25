package com.mcgill.ecse428.foodme.test;

import com.robotium.solo.Solo;

public class DeleteAccountStepDefs {

    //When I select the logout option
    public static void when(Solo solo) {
        CucumberActionSteps.deleteAccount(solo);
    }

    //Then I should not have an account
    public static void then(Solo solo) {
    //TODO
    }

    //And I should be logged out
    public static boolean and(Solo solo) {
        return CucumberActionSteps.loggedIn(solo);
    }
}
