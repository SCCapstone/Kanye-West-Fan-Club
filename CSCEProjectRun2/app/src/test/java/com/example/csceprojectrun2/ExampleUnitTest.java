package com.example.csceprojectrun2;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.util.Map;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void api_key_isValid() {
        try  {
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            // test api call that requires no input and should always work
            String urlOrigin = "https://na1.api.riotgames.com/lol/status/v4/platform-data";

            url = new URL(urlOrigin
                    + "?api_key=" + RiotAPIHelper.DEV_KEY_NOT_SECURE
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