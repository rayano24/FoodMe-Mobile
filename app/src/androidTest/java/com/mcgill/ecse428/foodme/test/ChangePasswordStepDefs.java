package com.mcgill.ecse428.foodme.test;

import com.robotium.solo.Solo;

public class ChangePasswordStepDefs {

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
        return solo.waitForText("Password successfully changed");
    }

    //Then my pw should not be changed
    public static boolean then2(Solo solo){
        //TODO
        return solo.waitForText("Password could not be changed");
    }
}