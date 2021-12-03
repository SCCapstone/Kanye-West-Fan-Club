package com.example.csceprojectrun2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.LayoutInflater;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class MatchDetails extends AppCompatActivity{

    ScrollView matchDetailsContainer;

//    String myValue = MainActivity.getMyString();
    //Pass this in from match container somehow
    //String GameID = MainActivity


    public void viewMatchData() {

    }
/*
    public void MatchStat(ScrollView matchDetailsContainer) {
        LinearLayout linearLayout = matchDetailsContainer.findViewById(R.id.match_details_container_linear_layout);
        // get match data (refactor)
        try {
            System.out.println("TESTING -- HTTP!!!!!!!!!!!!!!!!!");
            URL url = new URL("https://americas.api.riotgames.com/tft/match/v1/matches/by-puuid/9IkIogPfGJh-bx_f1KRGZj8AtMWHV_AIO7UFGxlptJ2q7TtlkV90a49FYfYt5HWhKenPapiF6wE-LA/ids?count=10");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("X-Riot-Token", APIKEY);
            String line = "";
            InputStreamReader inputStreamReader = new InputStreamReader(httpConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader((inputStreamReader));
            while((line=bufferedReader.readLine())!=null) {
                System.out.println(line);
            }
            bufferedReader.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        // clear any existing match tiles
        linearLayout.removeAllViews();
        // build new match tiles
        LayoutInflater inflater = getLayoutInflater();
        for(int i=0; i<10; i++) {
            // creates the card UI element
            inflater.inflate(R.layout.match_card, linearLayout, true);
            // get and update new card
            View newMatchCard = linearLayout.getChildAt(i);
            TextView gameType = newMatchCard.findViewById(R.id.gameType);
            TextView gameLength = newMatchCard.findViewById((R.id.gameLength));
            gameType.setText("Test RANKED");
            gameLength.setText("Length: " + i);
        }
        System.out.println("finished rendering matches!!!!!!!!!!!!!!!!!!!");
    }
*/



    //Stock Stuff
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.match_details);

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);




//IDKv
        //matchDetailsContainer = findViewById(R.id.match_details_container);
        //MatchStat(matchDetailsContainer);


    }

    public void ClickMenu(View view) {
        MainActivity.redirectActivity(this, MainActivity.class);

    }
/*
    public void ClickMenu(View view) {
        //Open drawer
        MainActivity.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view) {
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view) {
        //Redirect to home activity
        MainActivity.redirectActivity(this,MainActivity.class);
    }
    public void ClickPopularBuilds(View view){
        //Redirect to Popular Builds activity
        MainActivity.redirectActivity(this, PopularBuilds.class);
    }
    public void ClickCommunityBuilds(View view) {
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this, CommunityBuilds.class);
    }
    public void ClickCurrentCharacters(View view) {
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this, CurrentCharacters.class);
    }
    public void ClickItemBuilder(View view) {
        //Recreate the Item Builder activity
        MainActivity.redirectActivity(this,ItemBuilder.class);
    }
    public void ClickLogout(View view){
        //Logout and close app
        MainActivity.logout(this);
    }*/

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}