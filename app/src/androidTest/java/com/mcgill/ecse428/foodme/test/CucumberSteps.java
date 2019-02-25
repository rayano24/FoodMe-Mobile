package com.mcgill.ecse428.foodme.test;

import android.test.ActivityInstrumentationTestCase2;

import com.mcgill.ecse428.foodme.test.StepDefs.ChangePasswordStepDefs;
import com.mcgill.ecse428.foodme.test.StepDefs.CreateAccountStepDefs;
import com.mcgill.ecse428.foodme.test.StepDefs.DeleteAccountStepDefs;
import com.mcgill.ecse428.foodme.test.StepDefs.DislikeRestaurantStepDefs;
import com.mcgill.ecse428.foodme.test.StepDefs.FilterCuisineStepDefs;
import com.mcgill.ecse428.foodme.test.StepDefs.FilterOpenRestaurantsStepDefs;
import com.mcgill.ecse428.foodme.test.StepDefs.FilterPriceStepDefs;
import com.mcgill.ecse428.foodme.test.StepDefs.UserLoginStepDefs;
import com.mcgill.ecse428.foodme.test.StepDefs.UserLogoutStepDefs;
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
    public void i_am_on_the_sign_in_form() {
        solo = new Solo(getInstrumentation());
        getActivity();
        UserLoginStepDefs.given(solo);
    }

    //TODO
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
    public void i_am_on_the_register_form() {
        solo = new Solo(getInstrumentation());
        getActivity();
        CreateAccountStepDefs.givenRegisterForm(solo);
    }

    @When("I enter a {string}, an {string}, a {string} and a {string}")
    public void i_enter_a_username_an_email_a_phone_and_a_pw(String username, String email, String phone, String password) {

        solo.waitForActivity("LoginActivity", timeout);
        CreateAccountStepDefs.when(solo, username, email, phone, password);
    }

    @Then("I should have an account")
    public void i_should_have_an_account() {

        solo.waitForActivity("LoginActivity", timeout);
        assertTrue(CreateAccountStepDefs.validThen(solo));
    }

    // ================================== Create Account feature: Scenario 2 =======================================================
    @Then("I should not be able to create an account")
    public void i_should_not_be_able_to_create_an_account() {

        solo.waitForActivity("LoginActivity", timeout);
        assertFalse(CreateAccountStepDefs.invalidThen(solo));
    }

    //TODO Change when feature is implemented
    // ================================== Delete Account feature: Scenario 1 =======================================================

    @When("I select the delete account option")
    public void i_select_the_delete_account_option() {
        solo.waitForActivity("MainActivity", timeout);
        DeleteAccountStepDefs.when(solo);
    }

    @Then("I should not have an account")
    public void i_should_not_have_an_account() {
        solo.waitForActivity("MainActivity", timeout);
        DeleteAccountStepDefs.then(solo);
    }

    @And("I should be logged out")
    public void i_should_be_logged_out() {
        solo.waitForActivity("MainActivity", timeout);
        assertFalse(DeleteAccountStepDefs.and(solo));
    }

    //TODO Change when feature is implemented
    // ================================== Change Password feature: Scenario 1-2 =======================================================
    @When("I select the change password option")
    public void i_select_the_change_password_option() {
        solo.waitForActivity("MainActivity", timeout);
        ChangePasswordStepDefs.whenSelect(solo);
    }

    @When("I enter my {string} and a {string}")
    public void i_enter_my_old_pw_and_a_new_pw(String oldPw, String newPw) {
        solo.waitForActivity("MainActivity", timeout);
        ChangePasswordStepDefs.whenEnter(solo, oldPw, newPw);
    }

    @Then("My password should be changed")
    public void my_pw_should_be_changed() {
        solo.waitForActivity("MainActivity", timeout);
        assertTrue(ChangePasswordStepDefs.then1(solo));
    }

    @Then("My password should not change")
    public void my_pw_should_not_change() {
        solo.waitForActivity("MainActivity", timeout);
        assertTrue(ChangePasswordStepDefs.then2(solo));
    }

    //TODO Change when feature is implemented
    // ================================== Filter Cuisine feature: Scenario 1 - 2 =======================================================
    @Given("I choose not to log in")
    public void i_choose_not_to_log_in() {
        solo = new Solo(getInstrumentation());
        getActivity();
        FilterCuisineStepDefs.givenNotLogIn(solo);
    }

    @When("I select my cuisine {string} preferences")
    public void i_select_my_cuisine_pref(String cuisine) {
        solo.waitForActivity("MainActivity", timeout);
        FilterCuisineStepDefs.when(solo, cuisine);
    }

    @Then("I should see restaurants that fit my cuisine {string} selection")
    public void i_should_see_restaurants_that_fit_my_cuisine_selection(String cuisine) {
        solo.waitForActivity("MainActivity", timeout);
        assertTrue(FilterCuisineStepDefs.then(solo, cuisine));
    }

    //TODO Change when feature is implemented
    // ================================== Filter Open feature: Scenario 1 =======================================================
    @When("I select my open {string} preferences")
    public void i_select_my_open_pref(String open) {
        solo.waitForActivity("MainActivity", timeout);
        FilterOpenRestaurantsStepDefs.when(solo, Boolean.valueOf(open));
    }

    @Then("I should see restaurants that fit my open {string} selection")
    public void i_should_see_restaurants_that_fit_my_open_selection(String open) {
        solo.waitForActivity("MainActivity", timeout);
        assertTrue(FilterOpenRestaurantsStepDefs.then(solo, Boolean.valueOf(open)));
    }

    //TODO Change when feature is implemented
    // ================================== Filter Price feature: Scenario 1 =======================================================
    @When("I select my price {string} preferences")
    public void i_select_my_price_pref(String price) {
        solo.waitForActivity("MainActivity", timeout);
        FilterPriceStepDefs.when(solo, price);
    }

    @Then("I should see restaurants that fit my price {string} selection")
    public void i_should_see_restaurants_that_fit_my_price_selection(String price) {
        solo.waitForActivity("MainActivity", timeout);
        assertTrue(FilterPriceStepDefs.then(solo, price));
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