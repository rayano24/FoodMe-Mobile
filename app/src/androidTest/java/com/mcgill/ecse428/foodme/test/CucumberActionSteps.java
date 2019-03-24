package com.mcgill.ecse428.foodme.test;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robotium.solo.Solo;

import androidx.recyclerview.widget.RecyclerView;

public class CucumberActionSteps {

    private static String adminUsername = "admin";
    private static String adminPassword = "password";
    private static int timeout = 1000;

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
        return solo.waitForText("Find", 1, timeout);

//        return solo.waitForActivity("MainActivity") || solo.waitForText("Successful login");
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
    public static void register(Solo solo, String firstname, String lastname, String username, String email, String password) {
        if (!loggedIn(solo)) {
            getToRegisterForm(solo);

            //Fill prompt with username to test
            AutoCompleteTextView registerFirstName = (AutoCompleteTextView) solo.getView("registerFirstName");
            solo.typeText(registerFirstName, firstname);

            //Fill prompt with username to test
            AutoCompleteTextView registerLastName = (AutoCompleteTextView) solo.getView("registerLastName");
            solo.typeText(registerLastName, lastname);

            //Fill prompt with username to test
            EditText registerUsername = (EditText) solo.getView("registerName");
            solo.typeText(registerUsername, username);

            //Fill prompt with email to test
            AutoCompleteTextView registerEmail = (AutoCompleteTextView) solo.getView("registerEmail");
            solo.typeText(registerEmail, email);

            //Fill prompt with password to test
            EditText registerPassword = (EditText) solo.getView("registerPassword");
            solo.typeText(registerPassword, password);

            solo.clickOnButton("Register");
        }
    }

    // ================================== Filtering ===========================================

    public static void getToRestaurantsPage(Solo solo){
        //TODO
    }

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

    // ================================== Rating =============================================
    public static void clickRestaurant(Solo solo){
        solo.clickInRecyclerView(1);
    }

    public static void clickDislike(Solo solo){

        //If restaurant is not disliked -> dislike it, otherwise -> undislike it -> dislike it
        if (solo.getView("DislikeBtn").getVisibility() == View.VISIBLE)
            solo.clickOnView(solo.getView("DislikeBtn"));

        else {
            solo.clickOnView(solo.getView("UnDislikeButton"));
            solo.clickOnView(solo.getView("DislikeBtn"));
        }
    }

    public static boolean checkDisliked(Solo solo){
        solo.waitForView(solo.getView("UnDislikeButton"));
        return solo.getView("UnDislikeButton").getVisibility() == View.VISIBLE;
    }

    public static void clickLike(Solo solo){

        //If restaurant is not liked -> like it, otherwise -> unlike it -> like it
        if (solo.getView("LikeBtn").getVisibility() == View.VISIBLE)
            solo.clickOnView(solo.getView("LikeBtn"));

        else {
            solo.clickOnView(solo.getView("UnlikeBtn"));
            solo.clickOnView(solo.getView("LikeBtn"));
        }

    }

    public static boolean checkLiked(Solo solo){
        solo.waitForView(solo.getView("UnlikeBtn"));
        return solo.getView("UnlikeBtn").getVisibility() == View.VISIBLE;
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

    public static void selectChangePassword(Solo solo){
        if (getToSettings(solo))
        solo.clickOnView(solo.getView("noAccountButton"));

    }

    public static void changePassword(Solo solo, String oldPw, String newPw){

        //Fill prompt with username to test
        EditText oldPassword = (EditText) solo.getView("etOldPassword");
        solo.typeText(oldPassword, oldPw);

        //Fill prompt with email to test
        EditText newPassword = (EditText) solo.getView("etPassword");
        solo.typeText(newPassword, newPw);

        solo.clickOnView(solo.getView("save_changes"));
    }

    public static void signOut(Solo solo) {
        if (getToSettings(solo))
            solo.clickOnView(solo.getView("signOutButton"));
    }

    public static void deleteAccount(Solo solo) {
        if (getToSettings(solo)) {
            solo.clickOnView(solo.getView("noAccountButton"));
            solo.clickOnView(solo.getView("deleteAccount"));

        }
    }
}
