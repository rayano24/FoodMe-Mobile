package com.mcgill.ecse428.foodme.test;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robotium.solo.Solo;

public class CucumberActionSteps {

    private static String adminUsername = "admin";
    private static String adminPassword = "password";

    // ================================== Logging in / Accounts ===========================================

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

    public static void accessAppWithoutLogginIn(Solo solo){

        if (loggedIn(solo))
            signOut(solo);

        solo.clickOnView(solo.getView("noAccount"));
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

    // ================================== Filtering ===========================================

    public static void filterByCuisine(Solo solo, String cuisine){
        //TODO Change when feature is implemented
    }

    public static void filterByPrice(Solo solo, String price){
        //TODO Change when feature is implemented
    }

    public static void filterByOpenRestaurants(Solo solo, boolean filter){
        //TODO Change when feature is implemented
    }

    public static void filterByDistance(Solo solo, int maxDistance) {
        //TODO Change when feature is implemented
    }

    // ================================== Settings ===========================================

    private static boolean getToSettings(Solo solo) {
        if (loggedIn(solo)) {
            //Settings -> Sign Out
            solo.clickOnMenuItem("Settings");
            return true;
        }
        return false;
    }

    //TODO Change when feature is implemented
    public static void selectChangePassword(Solo solo){
        if (getToSettings(solo))
            solo.clickOnView(solo.getView("changePasswordButton"));
    }

    //TODO Change when feature is implemented
    public static void changePassword(Solo solo, String oldPw, String newPw){

        //Fill prompt with username to test
        EditText oldPassword = (EditText) solo.getView("oldPassword");
        solo.typeText(oldPassword, oldPw);

        //Fill prompt with email to test
        EditText newPassword = (EditText) solo.getView("newPassword");
        solo.typeText(newPassword, newPw);
    }

    public static void signOut(Solo solo) {
        if (getToSettings(solo))
            solo.clickOnView(solo.getView("signOutButton"));
    }

    //TODO Change when feature is implemented
    public static void deleteAccount(Solo solo) {
        if (getToSettings(solo))
            solo.clickOnView(solo.getView("deleteAccountButton"));
    }
}