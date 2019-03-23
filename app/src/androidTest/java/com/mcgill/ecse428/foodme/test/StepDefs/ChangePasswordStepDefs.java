package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class ChangePasswordStepDefs {

    //Given I am logged in as username and password
    public static void given(Solo solo, String username, String password){

        CucumberActionSteps.logIn(solo, username, password);
    }

    //When I select the change pw option
    public static void whenSelect(Solo solo){

        CucumberActionSteps.selectChangePassword(solo);
    }

    //When I enter a new password
    public static void whenEnter(Solo solo, String oldPw, String newPw){

        CucumberActionSteps.changePassword(solo, oldPw, newPw);
    }

    //Then my pw should be changed
    public static boolean then1(Solo solo){
        //TODO
        return solo.waitForText("");
    }

    //Then my pw should not be changed
    public static boolean then2(Solo solo){

        return solo.waitForText("There was an error, try again later.");
    }
}