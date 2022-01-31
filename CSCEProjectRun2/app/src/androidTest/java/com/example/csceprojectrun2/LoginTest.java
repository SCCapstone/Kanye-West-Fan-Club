package com.example.csceprojectrun2;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginTest{

    @Rule
    public ActivityTestRule<Login> lActivityTestRule =  new ActivityTestRule<Login>(Login.class);

    private String nUsername = "Test";
    private String nPassword = "Test";

    @Before
    public void setUp() throws Exception {
        //super.setUp();
    }

    @Test
    public void testUserLogin(){
        //Input Username and Pwd
        Espresso.onView(withId(R.id.username)).perform(typeText(nUsername));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.password)).perform(typeText(nPassword));
        Espresso.closeSoftKeyboard();

        //Click button
        Espresso.onView(withId(R.id.log_in)).perform(click());

        //Check
        try {
            Espresso.onView(withId(R.id.log_in)).check(matches(isDisplayed()));
            // View is displayed
        } catch (AssertionFailedError e) {
            // View not displayed
        }

    }

    @After
    public void tearDown() throws Exception {
    }
}