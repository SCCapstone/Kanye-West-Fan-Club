package com.example.csceprojectrun2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class RiotAPIHelper {


    /* SAMPLE ACCOUNT
    {
        "puuid": "9IkIogPfGJh-bx_f1KRGZj8AtMWHV_AIO7UFGxlptJ2q7TtlkV90a49FYfYt5HWhKenPapiF6wE-LA",
        "gameName": "Liquid Goose",
        "tagLine": "NA1"
    }
    */
    // https://developer.riotgames.com/apis#tft-match-v1/GET_getMatchIdsByPUUID
    // Returns previous X matches a given player participated in
    // TODO: refactor to use Json?
    public static final String[] getMatchesFromPuuid(String puuid, int numMatchesToReturn, String validDevKey) {
        System.out.println("SUPPLIED PUUID: " + puuid);
        //calls the riot games api to get the matches from the logged in puuid
        try {
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();
            //query parameter
            String urlOrigin = "https://americas.api.riotgames.com/tft/match/v1/matches/by-puuid/";

            url = new URL(urlOrigin + puuid + "/ids"
                    + "?count=" + numMatchesToReturn
                    + "&api_key=" + validDevKey
            );
            //grabs the response body
            try {
                BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));

                while ((callResp = read.readLine()) != null)
                    sb.append(callResp + "\n");

                read.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            // get output string
            String matchIdStr = sb.toString();

            // calls starting & ending brackets for matchID to set the match id
            matchIdStr = matchIdStr.substring(2, matchIdStr.length() - 3);

            // split into a string array
            String[] matchIds = matchIdStr.split("\",\"");

            return matchIds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // https://developer.riotgames.com/apis#tft-match-v1/GET_getMatch
    // Returns match data JSON for a given match id
    public static final JsonObject getMatchData(String matchId, String validDevKey) {
        try {
            //Interacts with riot games api
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            String urlOrigin = "https://americas.api.riotgames.com/tft/match/v1/matches/";

            url = new URL(urlOrigin
                    + matchId
                    + "?api_key=" + validDevKey
            );

            // convert string HTTP get results into a Json object
            InputStream is = url.openStream();
            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            is.close();
            rdr.close();
            //debug

            System.out.println((obj.toString()));
            //gets the game info
            JsonObject info = obj.getJsonObject("info");
            System.out.println(info.toString());
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // takes matchdata and returns participant data array for a specific puuid
    public static final JsonObject getParticipantByPuuid(JsonObject matchData, String puuid) {
        //gets the game info
        JsonObject info = matchData.getJsonObject("info");
        //gets the game participants
        JsonArray participants = info.getJsonArray("participants");
        for (JsonValue jsonVal : participants) {
            JsonObject participant = (JsonObject) jsonVal;
            //finds the puuid of the participant we need
            if (participant.getString("puuid").equals(puuid))
                return participant;
        }
        return null;
    }

    //Gets the users puuid from gameName and tagline used for search
    public static final String getPuuidFromRiotID(String gameName, String tagline, String validDevKey) {
        //Interacts with api
        try {
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            String urlOrigin = "https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/";

            url = new URL(urlOrigin
                    + gameName + "/" + tagline
                    + "?api_key=" + validDevKey
            );

            //gets response body
            try {
                BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));

                while ((callResp = read.readLine()) != null)
                    sb.append(callResp + "\n");

                read.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            // set response body to a string
            String fullReturnString = sb.toString();

            // culls starting & ending brackets
            String culledReturnStr = fullReturnString.substring(2, fullReturnString.length() - 3);
            // split into a string array
            String[] finalStrings = culledReturnStr.split("\",\"");
            // cut off the first 7 letters to just get puuid
            String puuid = finalStrings[0].substring(8);
            return puuid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //Takes match id, puuid, and the api key and returns the characters
    public static final String[] viewMatchData(String MATCH, String puuid, String APIKEY) {
        //interacts with the rg api
        try {
            URL call1;
            String call1resp;

            StringBuilder sb = new StringBuilder();
            try {
                call1 = new URL("https://americas.api.riotgames.com/tft/match/v1/matches/" + MATCH + "?api_key=" + APIKEY);
                BufferedReader read = new BufferedReader(new InputStreamReader(call1.openStream()));
                while ((call1resp = read.readLine()) != null)
                    sb.append(call1resp + "\n");
                read.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            //grabs response body
            String tester = sb.toString();
            //TOTAL SUBSTRING
            String total = tester.substring(tester.indexOf(""));
            //finding index of puuid in match data              x
            int x = 0;
            for (int index = total.indexOf(puuid); index >= 0; index = total.indexOf(puuid, index + 1)) {
                x = index;
            }
            x = total.lastIndexOf(puuid);

            //finding index of the end of players match data    y
            int y = 0;
            int first = 0;
            int index2;
            for (index2 = total.indexOf("companion"); index2 >= 0; index2 = total.indexOf("companion", index2 + 1)) {
                if (index2 > x && first == 0) {
                    ++first;
                    y = index2;
                }
            }
            if (y == 0) {
                y = total.lastIndexOf("queue_id");
            }
            //grabs just the players info
            String playersData = tester.substring(x, y);

            int[] units = new int[700];
            int i = 0;
            //gets the index of where each character is and stores it
            for (int unitIndex = playersData.indexOf("\"character_id\":\"TFT"); unitIndex >= 0; unitIndex = playersData.indexOf("\"character_id\":\"TFT", unitIndex + 1)) {
                units[i] = unitIndex;

                i++;
            }
            //gets the index of where each character ends and stores it
            int j = 0;
            int[] unitE = new int[700];
            for (int unitEnd = playersData.indexOf("\",\"item"); unitEnd >= 0; unitEnd = playersData.indexOf("\",\"item", unitEnd + 1)) {
                unitE[j] = unitEnd;
                j++;
            }
            //adding units to list
            String[] unitlist = new String[i];
            for (int z = 0; i > z; z++) {
                //Creates a list of units
                //unitlist[z] = playersData.substring(units[z] + 16 + 5, unitE[z]);
                //Makes sure the character name will be displayed correctly
                String underscore = playersData.substring(units[z] + 16 + 5, units[z] + 22);
                if (underscore.contains("_")) {
                    unitlist[z] = playersData.substring(units[z] + 16 + 6, unitE[z]);
                } else {
                    unitlist[z] = playersData.substring(units[z] + 16 + 5, unitE[z]);
                }
            }
            //returns the array of characters
            return (unitlist);

        } catch (Exception e) {
            //error handling
            System.out.println("Something went wrong.");
            System.out.println(e);
            String boink[] = {};

            return (boink);
        }

    }

    //Used to test and debug the display of detailed character containers
    public static final String[] viewMatchData2() {
        //Populates random array to display
        Random rand = new Random();

        String boink1[] = {"TFT6_Zyra", "TFT6_Zac", "TFT6_Lissandra", "TFT6_Heimerdinger", "TFT6_Taric", "TFT6_Orianna", "TFT6_Janna", "TFT6_Janna", "TFT6_Ezreal", "TFT6_Zilean", " TFT6_Heimerdinger", "TFT6_Braum", "TFT6_Taric", "TFT6_Seraphine", "TFT6_Jayce", "TFT6_Janna"};

        return (boink1);

    }

    //This takes in api kay and tft name to result in puuid
    public static String playerName(String tftname, String APIKEY) {

        try {
            URL call1;
            String call1resp;
            StringBuilder sb = new StringBuilder();
            try {
                call1 = new URL("https://br1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + tftname + "?api_key=" + APIKEY);
                BufferedReader read = new BufferedReader(new InputStreamReader(call1.openStream()));
                while ((call1resp = read.readLine()) != null)
                    sb.append(call1resp + "\n");
                read.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            //Grabs the response body
            String tester = sb.toString();
            //System.out.println(tester);
            //Gets the puuid from the response body
            String id = tester.substring(tester.indexOf("puuid") + 8, tester.indexOf("\",\"name"));
            return (id);
        } catch (Exception e) {
            System.out.println(e);
            String err = "";
            return (err);
        }
    }
}