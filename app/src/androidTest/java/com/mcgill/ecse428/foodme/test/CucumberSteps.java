package com.mcgill.ecse428.foodme.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.robotium.solo.Solo;

import com.mcgill.ecse428.foodme.activity.MainActivity;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CucumberSteps extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.mcgill.ecse428.foodme.activity.LoginActivity";
    private static Class<?> launcherActivityClass;

    static {
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public CucumberSteps() throws ClassNotFoundException {
        super((Class<MainActivity>) launcherActivityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    // ================================== Test Implementation =======================================================

    public void getToLoginForm(){ solo.clickOnButton("Sign in"); }

    public boolean atLogin(){ return solo.searchButton("Sign in"); }

    public void signOut() {

        //Settings -> Sign Out
        solo.clickOnMenuItem("Settings");
        solo.clickOnView(solo.getView("signOutButton"));
    }

    // ================================== User Login feature: Scenario 1 =======================================================

    @Given("I have an existing account")
    public void I_have_an_existing_account() throws Exception {
        solo = new Solo(getInstrumentation());
        getActivity();

        if (!atLogin()) signOut();
        getToLoginForm();
    }

    @When("I enter my {string} and my {string}")
    public void i_enter_my_username(String username, String password) throws Exception {
        solo.waitForActivity("LoginActivity", 2000);

        //Fill prompt with username to test
        AutoCompleteTextView signInName = (AutoCompleteTextView)solo.getView("signInName");
        solo.typeText(signInName, username);

        //Fill prompt with password to test
        EditText signInPassword = (EditText) solo.getView("signInPassword");
        solo.typeText(signInPassword, password);

        solo.clickOnButton("Sign in");
    }

    @Then("I should be able to successfully login")
    public void i_should_not_be_able_to_drop_block() throws Exception {

        solo.waitForActivity("MainActivity", 2000);
        assertFalse(solo.searchButton("Sign in"));
    }

    // ================================== User Login feature: Scenario 2 =======================================================


    // ================================== User Login feature: Scenario 3 =======================================================

    @When("I enter an invalid combination of {string} and {string}")
    public void i_enter_an_invalid_combination_of_and  (String username, String password){

        solo.waitForActivity("LoginActivity", 2000);

        //Fill prompt with username to test
        AutoCompleteTextView signInName = (AutoCompleteTextView)solo.getView("signInName");
        solo.typeText(signInName, username);

        //Fill prompt with password to test
        EditText signInPassword = (EditText) solo.getView("signInPassword");
        solo.typeText(signInPassword, password);

        solo.clickOnButton("Sign in");
    }

    @Then("I should not be able to login")
    public void i_should_not_be_able_to_login() throws Exception {

        solo.waitForActivity("MainActivity", 2000);
//        assertTrue(solo.searchButton("Sign in"));
    }

    @And("I should be prompted to re-enter my credentials")
    public void i_should_be_prompted_to_reenter_my_credentials() throws Exception {

        solo.waitForActivity("MainActivity", 2000);
//        assertTrue(solo.waitForText("This field is required"));
    }

    // ================================== Dislike Restaurant feature: Scenario 1 ===========================================

    @Given("I am viewing a restaurant")
    public void viewing_restaurant() throws Exception {
        solo = new Solo(getInstrumentation());
        getActivity();

        if (!atLogin()) signOut();
        getToLoginForm();
    }

    @When("I click dislike restaurant")
    public void dislike_restaurant() throws Exception {
        solo.waitForActivity("MainActivity", 2000);

        solo.clickOnButton("Dislike");
    }

    @Then("The restaurant should be disliked")
    public void restaurant_should_be_disliked() throws Exception {

        solo.waitForActivity("MainActivity", 2000);
        assertFalse(solo.searchButton("Sign in"));
    }

    // ================================== End of Test Implementation =======================================================

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        getActivity().finish();
        super.tearDown();
    }
}