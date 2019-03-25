package com.mcgill.ecse428.foodme.test.StepDefs;

import com.mcgill.ecse428.foodme.test.CucumberActionSteps;
import com.robotium.solo.Solo;

public class ChangePasswordStepDefs {

    private static long timeout = 200;

    //Given I am logged in as username and password
    public static void given(Solo solo, String username, String password){

        CucumberActionSteps.signOut(solo);
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

        return solo.waitForText("Password changed successfully");
    }

    //Then my pw should not be changed
    public static boolean then2(Solo solo){

        return  solo.waitForText("Invalid old password", 1, timeout);
    }
}