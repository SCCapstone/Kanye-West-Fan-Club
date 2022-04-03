package com.example.csceprojectrun2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

public class MatchDetails extends AppCompatActivity {

    ScrollView matchDetailsContainer;

//    String myValue = MainActivity.getMyString();
    //Pass this in from match container somehow
    //String GameID = MainActivity



    public static String[] viewMatchData() {
        String APIKEY = "RGAPI-f1315bcb-8c6e-41fa-9559-d9a069b1fb9c";
        String puuid = "bWxLgFEOjkoSZh8rQ4hGNAvIDd_gWRGlybnlqQzVaQJdMKvHACDu0fzrMJGRYNra_C61q8z2vkXKng";
        String MATCH;

        String MATCH1 = "NA1_4107774217";
        String MATCH2 = "NA1_4172043823";
        String MATCH3 = "NA1_4172045804";
        String MATCH4 = "NA1_4172133214";
        String MATCH5 = "NA1_4172088092";
        String MATCH6 = "NA1_4172138764";

        String[] boink = {MATCH1, MATCH2, MATCH3, MATCH4, MATCH5, MATCH6};
        Random rand = new Random();
        int mat = rand.nextInt(boink.length);
        MATCH = boink[mat];
        //String MATCH = "NA1_4172138764";

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
        for (int unitIndex = playersData.indexOf("TFT"); unitIndex >= 0; unitIndex = playersData.indexOf("TFT", unitIndex + 1)) {
            units[i] = unitIndex;
            i++;
        }


        int j = 0;
        int[] unitE = new int[700];
        for (int unitEnd = playersData.indexOf("\",\"items\":"); unitEnd >= 0; unitEnd = playersData.indexOf("\",\"items\":", unitEnd + 1)) {
            unitE[j] = unitEnd;
            j++;
        }

        //adding units to list
        String[] unitlist = new String[i];
        for (int z = 0; i > z; z++) {
            //Creates a list of units
            unitlist[z] = playersData.substring(units[z], unitE[z]);


            System.out.println(unitlist[z]);
        }


        return (unitlist);
    }



    //Stock Stuff
    DrawerLayout drawerLayout;

    /////////////////////////////////////////////////////////////////////////////////////////
    //private static String z = MATCH;
/*
    public static void viewMatchData(){
        String a = MATCH;
        String z = a;
    }
    public static String matID(){
        return z;
    }*/
    ////////////////////////////////////////////////////////////////////////////////////////////

  /*
    public static String units()
    {
        return z;
    }
    String myValue = MatchDetails.viewMatchData();
    //Pass this in from match container somehow
    String GameID =
*/


    /////////////////////////////////////////////////////////////////////////////////
/*

    //MatchID fetch
    String matID[];
    String [] listOfUnits = viewMatchData();

    public class addtolist(){

        int y = listOfUnits.length();
        ListView Ulist;
        for(int z = 0; y >= z; z++) {
            matID[z] = listOfUnits[z];
        }
    }*/

    public static String[] viewMatchData2() {


        Random rand = new Random();

        String boink1[] = {"TFT6_Zyra","TFT6_Zac","TFT6_Lissandra","TFT6_Heimerdinger","TFT6_Taric","TFT6_Orianna","TFT6_Janna","TFT6_Janna"};
        String boink2[] = {"TFT6_Ezreal","TFT6_Zilean"," TFT6_Heimerdinger","TFT6_Braum","TFT6_Taric","TFT6_Seraphine","TFT6_Jayce","TFT6_Janna"};
        String boink3[] = {"TFT6_Twitch", "TFT6_Katarina", "TFT6_Blitzcrank", "TFT6_Talon", "TFT6_Leona", "TFT6_Shaco", "TFT6_Ekko"};
        int mat = rand.nextInt(3);
        if(mat==1){
            return(boink1);
        }
        if(mat==2){
            return(boink2);
        }
        else {
            return(boink3);
        }
    }
    String matID[] = viewMatchData2();
    //String matID[] = {"Vi", "Zac", "Urgot", "Yuumi", "Jinx", "Tahmkench", "Jayce"};




    ListView Ulist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_details);
//v
        Ulist = findViewById(R.id.list);
        ArrayAdapter<String> arr;
        arr
                = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item, matID);
        Ulist.setAdapter(arr);
//^




        drawerLayout = findViewById(R.id.drawer_layout);

        // temp functionality to go to match feed, to make merging everything in easier

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        //Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        //Recreate the Home activity
        recreate();
    }

    public void ClickSearch(View view) {
        System.out.println("Clicked search from MainActivity");
        MainActivity.searchHandler.ClickSearch(view);
    }

    public void ClickPopularBuilds(View view) {
        //Redirect to Popular Builds activity
        redirectActivity(this, PopularBuilds.class);
    }

    public void ClickCommunityBuilds(View view) {
        //Redirect to Community Builds activity
        redirectActivity(this, CommunityBuilds.class);
    }

    public void ClickCurrentCharacters(View view) {
        //Redirect to Current Characters activity
        redirectActivity(this, CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view) {
        //Redirect to Item Builder activity
        redirectActivity(this, ItemBuilder.class);
    }

    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(MatchDetails.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}