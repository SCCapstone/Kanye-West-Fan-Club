package com.example.csceprojectrun2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class viewDisplayExampleTest {
    @Test
    public void viewMatchDataErrorHandling() {
        //This tests the viewMatchData2 in riot gamesapihelper
        //The function of the method was to populate an long array
        //that could be called to test the display of the match details
        String test[] = {};
        String[] testDisplay = RiotAPIHelper.viewMatchData2();

        int i = test.length;
        int j = testDisplay.length;
        assertNotEquals(i,j);
    }
}
