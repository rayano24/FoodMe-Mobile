package com.mcgill.ecse428.foodme.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

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
    private int timeout = 2000;

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

    // ================================== User Login feature: Scenario 1-2 =======================================================

    @Given("I am on the sign in form")
    public void i_am_on_the_sign_in_form(){
        solo = new Solo(getInstrumentation());
        getActivity();
        UserLoginStepDefs.given(solo);
    }

    @Given("I have an existing account")
    public void I_have_an_existing_account() throws Exception {
//        solo.waitForActivity("LoginActivity", timeout);
    }

    @When("I enter my {string} and my {string}")
    public void i_enter_my_username_and_my_password(String username, String password) throws Exception {
        solo.waitForActivity("LoginActivity", timeout);
        UserLoginStepDefs.when(solo, username, password);
    }

    @Then("I should be able to successfully login")
    public void i_should_be_able_to_successfully_login() throws Exception {
        solo.waitForActivity("MainActivity", timeout);
        assertTrue(UserLoginStepDefs.then(solo));
    }

    // ================================== User Login feature: Scenario 3 =======================================================

    @When("I enter an invalid combination of {string} and {string}")
    public void i_enter_an_invalid_combination_of_and(String username, String password) {

        solo.waitForActivity("LoginActivity", timeout);
        UserLoginStepDefs.when(solo, username, password);
    }

    @Then("I should not be able to login")
    public void i_should_not_be_able_to_login() throws Exception {

        solo.waitForActivity("LoginActivity", timeout);
        assertFalse(UserLoginStepDefs.then(solo));
    }

    @And("I should be prompted to re-enter my credentials")
    public void i_should_be_prompted_to_reenter_my_credentials() throws Exception {

        solo.waitForActivity("LoginActivity", timeout);
        assertTrue(UserLoginStepDefs.and(solo));
    }


    // ================================== User Logout feature: Scenario 1 =======================================================
    @Given("I am logged in")
    public void I_am_logged_in() throws Exception {
        solo = new Solo(getInstrumentation());
        getActivity();
        UserLogoutStepDefs.given(solo);
    }

    @When("I select the logout option")
    public void i_select_the_logout_option() throws Exception {

        solo.waitForActivity("LoginActivity", timeout);
        UserLogoutStepDefs.when(solo);
    }

    @Then("I should be able to successfully logout")
    public void i_should_be_able_to_successfully_logout() throws Exception {

        solo.waitForActivity("LoginActivity", timeout);
        assertTrue(UserLogoutStepDefs.then(solo));
    }

    // ================================== Create Account feature: Scenario 1 =======================================================
    @Given("I am on the register form")
    public void i_am_on_the_register_form(){
        solo = new Solo(getInstrumentation());
        getActivity();
        CreateAccountStepDefs.givenRegisterForm(solo);
    }

    @When("I enter a {string}, an {string}, a {string} and a {string}")
    public void i_enter_a_username_an_email_a_phone_and_a_pw(String username, String email, String phone, String password){

        solo.waitForActivity("LoginActivity", timeout);
        CreateAccountStepDefs.when(solo, username, email, phone, password);
    }

    @Then("I should have an account")
    public void i_should_have_an_account(){

        solo.waitForActivity("LoginActivity", timeout);
        assertTrue(CreateAccountStepDefs.validThen(solo));
    }

    // ================================== Create Account feature: Scenario 2 =======================================================
    @Then("I should not be able to create an account")
    public void i_should_not_be_able_to_create_an_account(){

        solo.waitForActivity("LoginActivity", timeout);
        assertFalse(CreateAccountStepDefs.invalidThen(solo));
    }
    // ================================== Dislike Restaurant feature: Scenario 1 ===========================================

    @Given("I am viewing a restaurant")
    public void viewing_restaurant() throws Exception {
        solo = new Solo(getInstrumentation());
        getActivity();
        DislikeRestaurantStepDefs.given(solo);
    }

    @When("I click dislike restaurant")
    public void dislike_restaurant() throws Exception {
        solo.waitForActivity("MainActivity", 2000);
        DislikeRestaurantStepDefs.when(solo);
    }

    @Then("The restaurant should be disliked")
    public void restaurant_should_be_disliked() throws Exception {
        solo.waitForActivity("MainActivity", 2000);
        assertTrue(DislikeRestaurantStepDefs.then(solo));
    }

    // ================================== End of Test Implementation =======================================================

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        getActivity().finish();
        super.tearDown();
    }
}