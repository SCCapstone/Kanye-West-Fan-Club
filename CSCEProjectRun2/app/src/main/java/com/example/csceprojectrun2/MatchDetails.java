package com.example.csceprojectrun2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

public class MatchDetails extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ScrollView matchDetailsContainer;
    String MATCHID;
    String PUUID;
    TextView currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_details);

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        currentPage = findViewById(R.id.currentPage);

        //Receives match id and puuid from a match card clicked on Match feed
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            MATCHID = bundle.getString("matchID");
            PUUID = bundle.getString("puuid");
        }

        currentPage.setText("Match Details");


        Ulist = findViewById(R.id.list);
        ArrayAdapter<String> arr;
        arr
                = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item, matchID);
        Ulist.setAdapter(arr);
    }

    public static String[] viewMatchData(String MATCH, String puuid, String APIKEY) {
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

        ;
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


    public static String[] viewMatchData2() {


        Random rand = new Random();

        String boink1[] = {"TFT6_Zyra", "TFT6_Zac", "TFT6_Lissandra", "TFT6_Heimerdinger", "TFT6_Taric", "TFT6_Orianna", "TFT6_Janna", "TFT6_Janna"};
        String boink2[] = {"TFT6_Ezreal", "TFT6_Zilean", " TFT6_Heimerdinger", "TFT6_Braum", "TFT6_Taric", "TFT6_Seraphine", "TFT6_Jayce", "TFT6_Janna"};
        String boink3[] = {"TFT6_Twitch", "TFT6_Katarina", "TFT6_Blitzcrank", "TFT6_Talon", "TFT6_Leona", "TFT6_Shaco", "TFT6_Ekko"};
        int mat = rand.nextInt(3);
        if (mat == 1) {
            return (boink1);
        }
        if (mat == 2) {
            return (boink2);
        } else {
            return (boink3);
        }
    }


    String MATCH = "NA1_4265735286";
    String APIKEY = "RGAPI-f1315bcb-8c6e-41fa-9559-d9a069b1fb9c";
    String puuid = "bWxLgFEOjkoSZh8rQ4hGNAvIDd_gWRGlybnlqQzVaQJdMKvHACDu0fzrMJGRYNra_C61q8z2vkXKng";
    //String matchID[] = viewMatchData(MATCH, puuid, APIKEY);

    //String matID[] = viewMatchData2();
    String matchID[] = {"Vi", "Zac", "Urgot", "Yuumi", "Jinx", "Tahmkench", "Jayce"};


    ListView Ulist;

    public void ClickBack(View view) {
        MainActivity.redirectActivity(this, MatchFeed.class);
    }

    public void ClickSearch(View view) {
        System.out.println("Clicked search from MainActivity");
        MainActivity.searchHandler.ClickSearch(view);
    }
}