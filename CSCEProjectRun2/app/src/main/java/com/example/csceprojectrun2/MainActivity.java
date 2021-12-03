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

import java.lang.Runnable;
import java.lang.Thread;

import javax.json.JsonObject;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    ScrollView matchContainer;

    private String convertToTimestamp(int seconds) {
        int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);

        String strMinutes = Integer.toString(minutes);
        String strSeconds = Integer.toString(seconds);

        if (strMinutes.length() < 2)
            strMinutes = "0" + strMinutes;

        if (strSeconds.length() < 2)
            strSeconds = "0" + strSeconds;

        return strMinutes + ":" + strSeconds;
    }

    private String getQueueType(int queueId) {
        if (queueId == 1100)
            return "Ranked";
        else
            return "Normal";
    }

    private void createMatchCard(int cardPosition, String matchId, JsonObject matchData, String ownerPuuid) {
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);

        JsonObject info = matchData.getJsonObject("info");
        JsonObject participantData = RiotAPIHelper.getParticipantByPuuid(matchData, ownerPuuid);

        String gameLength = convertToTimestamp(info.getInt("game_length"));
        String queueType = getQueueType(info.getInt("queue_id"));
        String placementNum = Integer.toString(participantData.getInt("placement"));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // build new match tiles
                LayoutInflater inflater = getLayoutInflater();

                // create the card UI element
                inflater.inflate(R.layout.match_card, linearLayout, true);

                // get and update new card
                View newMatchCard = linearLayout.getChildAt(cardPosition);

                TextView gameTypeUI = newMatchCard.findViewById(R.id.gameType);
                TextView gameLengthUI = newMatchCard.findViewById((R.id.gameLength));
                TextView matchIdUI = newMatchCard.findViewById(R.id.matchID);
                TextView placementUI = newMatchCard.findViewById(R.id.placement);

                matchIdUI.setText(matchId);
                gameLengthUI.setText(gameLength);
                gameTypeUI.setText(queueType);
                placementUI.setText("Placed: " + placementNum);
            }
        });
    }

    public void renderMatchHistory(ScrollView matchContainer) {
        // clear any existing match tiles
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);
        linearLayout.removeAllViews();

        // spawn thread and collect data from riot api
        new Thread(new Runnable(){
            @Override
            public void run() {
                // get recent played match's IDs
                String[] matchIds = RiotAPIHelper.getMatchesFromPuuid(RiotAPIHelper.samplePuuid, 6);

                // populate match feed
                for(int i=0; i<matchIds.length; i++) {
                    String matchId = matchIds[i];
                    JsonObject matchData = RiotAPIHelper.getMatchData(matchId);
                    createMatchCard(i, matchId, matchData, RiotAPIHelper.samplePuuid);
                }
            }
        }).start();
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