package com.example.csceprojectrun2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.InputStream;

import javax.json.*;

public class RiotAPIHelper {
<<<<<<< Updated upstream
    static String DEV_KEY_NOT_SECURE = "RGAPI-90a13922-9f2e-445d-9bd5-e9987e708114";
    static String samplePuuid = "lDQ-bP2nWGatqLp1xBbGLoOYXUouZ8X4u6oyatUitMNIXlvWdZ4FXoQepcne5NpIymRjmbKGyoO0Rw";
=======
    static String DEV_KEY_NOT_SECURE = "RGAPI-d6efc463-d5e1-4428-b4c9-629ca3e253c2";
    //static String samplePuuid = "9IkIogPfGJh-bx_f1KRGZj8AtMWHV_AIO7UFGxlptJ2q7TtlkV90a49FYfYt5HWhKenPapiF6wE-LA";
    static String samplePuuid = "bWxLgFEOjkoSZh8rQ4hGNAvIDd_gWRGlybnlqQzVaQJdMKvHACDu0fzrMJGRYNra_C61q8z2vkXKng";
>>>>>>> Stashed changes
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
}