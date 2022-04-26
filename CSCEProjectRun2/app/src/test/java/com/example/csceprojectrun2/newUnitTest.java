package com.example.csceprojectrun2;
import org.junit.Test;

import static org.junit.Assert.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class newUnitTest {


    @Test
    public void viewMatchDataErrorHandling() {

        try  {
            //Initial test milestone test, just calls riot games api with invalid dev key
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            // test api call that requires no input and should always work
            String urlOrigin = "https://na1.api.riotgames.com/lol/status/v4/platform-data";

            url = new URL(urlOrigin
                    + "?api_key=" + ""
            );

            URLConnection connection = url.openConnection();
            Map<String, List<String>> map = connection.getHeaderFields();
            String responseCode = map.get(null).get(0).split(" ")[1];

            // only check for rejection cases due to authentication, not server downtime
            assertNotEquals(responseCode, "403");
            assertNotEquals(responseCode, "400");
            assertNotEquals(responseCode, "401");
            assertNotEquals(responseCode, "404");
            assertNotEquals(responseCode, "405");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Code errored");
        }


    }

}
