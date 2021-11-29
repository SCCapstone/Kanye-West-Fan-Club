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

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    ScrollView matchContainer;

    String DEV_KEY_NOT_SECURE = "RGAPI-bcc29098-add9-49f9-a91a-072329de3b73";
    /* SAMPLE ACCOUNT
    {
        "puuid": "9IkIogPfGJh-bx_f1KRGZj8AtMWHV_AIO7UFGxlptJ2q7TtlkV90a49FYfYt5HWhKenPapiF6wE-LA",
        "gameName": "Liquid Goose",
        "tagLine": "NA1"
    }
    */

    public void createMatch() {

    }

    public void renderMatchHistory(ScrollView matchContainer) {
        System.out.println("rendering matches");
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);

        // get match data (refactor)
        try {
            System.out.println("TESTING -- HTTP!!!!!!!!!!!!!!!!!");

            URL url = new URL("https://americas.api.riotgames.com/tft/match/v1/matches/by-puuid/9IkIogPfGJh-bx_f1KRGZj8AtMWHV_AIO7UFGxlptJ2q7TtlkV90a49FYfYt5HWhKenPapiF6wE-LA/ids?count=10");
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("X-Riot-Token", DEV_KEY_NOT_SECURE);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("onCreate!!!!!!!!!");

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        matchContainer = findViewById(R.id.match_container);

        renderMatchHistory(matchContainer);
    }

    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        //Close drawer layout
        //Check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        //Recreate the Home activity
        recreate();
    }

    public void ClickPopularBuilds(View view){
        //Redirect to Popular Builds activity
        redirectActivity(this,PopularBuilds.class);
    }

    public void ClickCommunityBuilds(View view){
        //Redirect to Community Builds activity
        redirectActivity(this,CommunityBuilds.class);
    }

    public void ClickCurrentCharacters(View view){
        //Redirect to Current Characters activity
        redirectActivity(this,CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view){
        //Redirect to Item Builder activity
        redirectActivity(this,ItemBuilder.class);
    }

    public void ClickLogout(View view){
        //Logout and close the app
        logout(this);
    }

    public static void logout(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set title
        builder.setTitle("Logout");
        //Set message
        builder.setMessage("Are you sure you want to logout ?");
        //Click Yes Button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Complete activity
                activity.finishAffinity();
                //Exit app
                System.exit(0);
            }
        });
        //Click No Button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss dialog
                dialog.dismiss();
            }
        });
        //Show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity,aClass);
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