package com.example.csceprojectrun2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.InputStream;

import javax.json.*;

public class RiotAPIHelper {
    static String DEV_KEY_NOT_SECURE = "RGAPI-0295837d-0183-4c01-a150-401605e990a5";
    static String samplePuuid = "9IkIogPfGJh-bx_f1KRGZj8AtMWHV_AIO7UFGxlptJ2q7TtlkV90a49FYfYt5HWhKenPapiF6wE-LA";
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
    public static final String[] getMatchesFromPuuid(String puuid, int numMatchesToReturn) {
        System.out.println("SUPPLIED PUUID: " + puuid);
        try {
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            String urlOrigin = "https://americas.api.riotgames.com/tft/match/v1/matches/by-puuid/";

            url = new URL(urlOrigin + samplePuuid + "/ids"
                    + "?count=" + numMatchesToReturn
                    + "&api_key=" + DEV_KEY_NOT_SECURE
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
    public static final JsonObject getMatchData(String matchId) {
        try {
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            String urlOrigin = "https://americas.api.riotgames.com/tft/match/v1/matches/";

            url = new URL(urlOrigin
                    + matchId
                    + "?api_key=" + DEV_KEY_NOT_SECURE
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

    public static final String getPuuidFromRiotID(String gameName, String tagline) {
        try  {
            URL url;
            String callResp;
            StringBuilder sb = new StringBuilder();

            String urlOrigin = "https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/";

            url = new URL(urlOrigin
                    + gameName + "/" + tagline
                    + "?api_key=" + DEV_KEY_NOT_SECURE
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
}
