package com.mcgill.ecse428.foodme.test;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robotium.solo.Solo;

public class CucumberActionSteps {

    private static String adminUsername = "admin";
    private static String adminPassword = "password";

    //Goes to login form from welcome page
    public static void getToLoginForm(Solo solo) {
        solo.clickOnButton("Sign in");
    }

    //Goes to register form from welcome page
    public static void getToRegisterForm(Solo solo) {
        solo.clickOnButton("Register");
    }

    //Checks whether user is logged in
    public static boolean loggedIn(Solo solo) {
        //TODO FIND A BETTER WAY TO CHECK IF USER IS LOGGED IN
        return !solo.searchButton("Sign in");
    }

    //Logs in using username and password (does not handle invalid input, instead that is asserted in the "Then" method)
    public static void logIn(Solo solo, String username, String password) {
        if (!loggedIn(solo)) {
            getToLoginForm(solo);

            //Fill prompt with username to test
            AutoCompleteTextView signInName = (AutoCompleteTextView) solo.getView("signInName");
            solo.typeText(signInName, username);

            //Fill prompt with password to test
            EditText signInPassword = (EditText) solo.getView("signInPassword");
            solo.typeText(signInPassword, password);

            solo.clickOnButton("Sign in");
        }
    }

    public static void logInAsAdmin(Solo solo) {
        logIn(solo, adminUsername, adminPassword);
    }

    //Logs in using username and password (does not handle invalid input, instead that is asserted in the "Then" method)
    public static void register(Solo solo, String username, String email, String phone, String password) {
        if (!loggedIn(solo)) {
            getToRegisterForm(solo);

            //Fill prompt with username to test
            EditText registerUsername = (EditText) solo.getView("registerName");
            solo.typeText(registerUsername, username);

            //Fill prompt with email to test
            AutoCompleteTextView registerEmail = (AutoCompleteTextView) solo.getView("registerEmail");
            solo.typeText(registerEmail, email);

            //Fill prompt with phone number to test
            AutoCompleteTextView registerPhone = (AutoCompleteTextView) solo.getView("registerPhone");
            solo.typeText(registerPhone, phone);

            //Fill prompt with password to test
            EditText registerPassword = (EditText) solo.getView("registerPassword");
            solo.typeText(registerPassword, password);

            solo.clickOnButton("Register");
        }
    }

    public static void signOut(Solo solo) {

        if (loggedIn(solo)) {
            //Settings -> Sign Out
            solo.clickOnMenuItem("Settings");
            solo.clickOnView(solo.getView("signOutButton"));
        }
    }
}
