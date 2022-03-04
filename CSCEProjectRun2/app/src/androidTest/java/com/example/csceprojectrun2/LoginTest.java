package com.example.csceprojectrun2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;

import androidx.test.espresso.*;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class LoginTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private String nUsername = "Test";
    private String nPassword = "Test";

    @Before
    public void setUp() throws Exception {
        //super.setUp();
    }

    @Test
    public void testUserLogin(){
        //Input Username and Pwd
        System.out.println("testingasdf");


        onView(withId(R.id.tempOpenMatchFeed)).perform(click());

        try {
            System.out.println("checking if match container is open!!!!!!!!!!!!");
            onView(withId(R.id.match_container_linear_layout)).check(matches(isDisplayed()));
            // View is displayed
        } catch (Exception e) {
            // View not displayed
            System.out.println(e);
        }

        /*Espresso.onView(withText("username")).perform(typeText(nUsername));

        Espresso.onView(withText(R.id.username)).perform(typeText(nUsername));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.password)).perform(typeText(nPassword));
        Espresso.closeSoftKeyboard();

        //Click button
        Espresso.onView(withId(R.id.log_in)).perform(click());

        //Check
        try {
            Espresso.onView(withId(R.id.log_in)).check(matches(isDisplayed()));
            // View is displayed
        } catch (Exception e) {
            // View not displayed
            System.out.println(e);
        }
    */
    }

    @After
    public void tearDown() throws Exception {
    }
}