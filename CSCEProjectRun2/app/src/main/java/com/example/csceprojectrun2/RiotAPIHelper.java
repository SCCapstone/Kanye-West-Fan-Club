package com.example.csceprojectrun2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.InputStream;

import javax.json.*;

public class RiotAPIHelper {

    static String DEV_KEY_NOT_SECURE = "RGAPI-90a13922-9f2e-445d-9bd5-e9987e708114";
    static String samplePuuid = "lDQ-bP2nWGatqLp1xBbGLoOYXUouZ8X4u6oyatUitMNIXlvWdZ4FXoQepcne5NpIymRjmbKGyoO0Rw";


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
        try {
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            String urlOrigin = "https://americas.api.riotgames.com/tft/match/v1/matches/by-puuid/";

            url = new URL(urlOrigin + puuid + "/ids"
                    + "?count=" + numMatchesToReturn
                    + "&api_key=" + validDevKey
            );

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

            // cull starting & ending brackets
            matchIdStr = matchIdStr.substring(2, matchIdStr.length() - 3);

            // split into a string array
            String[] matchIds = matchIdStr.split("\",\"");

            // debug print string array
            /*System.out.println("match ids:");
            for(int i=0; i<matchIds.length; i++) {
                System.out.println(matchIds[i]);
            }*/

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
        JsonObject info = matchData.getJsonObject("info");
        JsonArray participants = info.getJsonArray("participants");
        for (JsonValue jsonVal : participants) {
            JsonObject participant = (JsonObject) jsonVal;

            //System.out.println("random Participant:");
            //System.out.println(participant.toString());

            if (participant.getString("puuid").equals(puuid))
                return participant;
        }
        return null;
    }

    public static final String getPuuidFromRiotID(String gameName, String tagline, String validDevKey) {
        try  {
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            String urlOrigin = "https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/";

            url = new URL(urlOrigin
                    + gameName + "/" + tagline
                    + "?api_key=" + validDevKey
            );

            try {
                BufferedReader read = new BufferedReader( new InputStreamReader(url.openStream()));

                while((callResp = read.readLine()) != null)
                    sb.append(callResp + "\n");

                read.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            // get output string
            String fullReturnString = sb.toString();

            // cull starting & ending brackets
            String culledReturnStr = fullReturnString.substring(2, fullReturnString.length()-3);

            // split into a string array
            String[] finalStrings = culledReturnStr.split("\",\"");

            String puuid = finalStrings[0].substring(8); // cut off the first 7 letters
            return puuid;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }













/////////////////////////////////////////////////////////////


    public static final String[] viewMatchData(String MATCH, String puuid, String APIKEY) {


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


            String tester = sb.toString();
            //TOTAL SUBSTRING
            String total = tester.substring(tester.indexOf(""));
            //finding puuid in match data
            int x = 0;
            for (int index = total.indexOf(puuid); index >= 0; index = total.indexOf(puuid, index + 1)) {
                x = index;

            }
            x = total.lastIndexOf(puuid);


            //finding the end of players match data
            int y = 0;
            int first = 0;
            int index2;
            for (index2 = total.indexOf("companion"); index2 >= 0; index2 = total.indexOf("companion", index2 + 1)) {
                if (index2 > x && first == 0) {
                    ++first;
                    y = index2;
                }
            }

            String playersData = tester.substring(x, y);

            int[] units = new int[700];
            int i = 0;

            for (int unitIndex = playersData.indexOf("\"character_id\":\"TFT"); unitIndex >= 0; unitIndex = playersData.indexOf("\"character_id\":\"TFT", unitIndex + 1)) {
                units[i] = unitIndex;

                i++;
            }

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
                unitlist[z] = playersData.substring(units[z] + 16, unitE[z]);
            }
            return (unitlist);


        }


        catch (Exception e) {
            System.out.println("Something went wrong.");
            System.out.println(e);
            String boink[] = {};

            return(boink);
        }






    }

/////////////////////////////////////////////////////////////////////////

}