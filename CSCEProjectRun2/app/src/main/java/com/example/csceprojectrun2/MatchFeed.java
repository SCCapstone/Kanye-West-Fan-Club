package com.example.csceprojectrun2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class MatchFeed extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ScrollView matchContainer;
    TextView tftName, currentPage;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_feed);

        System.out.println("onCreate!!!!!!!!!");

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.TFTName);
        currentPage = findViewById(R.id.currentPage);
        matchContainer = findViewById(R.id.match_container);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            userId = currentUser.getUid(); //Do what you need to do with the id
            renderMatchHistory(matchContainer);

            //Display current user's tft name in navigation drawer
            DocumentReference documentReference = fStore.collection("user").document(userId);
            documentReference.addSnapshotListener(this, (value, error) -> {
                //Retrieve tft name from Firebase
                if (value != null) {
                    String TFTName = value.getString("tftName");
                    tftName.setVisibility(View.VISIBLE);
                    tftName.setText(TFTName);
                }
            });
            currentPage.setText("Home");
        }
    }

    //Convert time to time stamp
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

    //Get queue type of match
    private String getQueueType(int queueId) {
        if (queueId == 1100)
            return "Ranked";
        else
            return "Normal";
    }

    //Apply champion images
    private void applyChampionImages(View matchCard, JsonObject participant) {
        JsonArray units = participant.getJsonArray("units");

        ImageView[] imageViews = new ImageView[]{
                matchCard.findViewById(R.id.imageView1),
                matchCard.findViewById(R.id.imageView2),
                matchCard.findViewById(R.id.imageView3),
                matchCard.findViewById(R.id.imageView4),
                matchCard.findViewById(R.id.imageView5),
                matchCard.findViewById(R.id.imageView6),
        };

        for (int i = 0; i < units.size() && i < imageViews.length; i++) {
            JsonObject unitData = (JsonObject) units.get(i);
            String nameId = unitData.getString("character_id");
            String name = nameId.split("_")[1];
        }
    }

    //Create match card
    private void createMatchCard(int cardPosition, String matchId, JsonObject matchData, String ownerPuuid) {
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);

        JsonObject info = matchData.getJsonObject("info");
        JsonObject participantData = RiotAPIHelper.getParticipantByPuuid(matchData, ownerPuuid);

        String gameLength = convertToTimestamp(info.getInt("game_length"));
        String queueType = getQueueType(info.getInt("queue_id"));
        assert participantData != null;
        String placementNum = Integer.toString(participantData.getInt("placement"));

        runOnUiThread(() -> {
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

            applyChampionImages(newMatchCard, participantData);

            newMatchCard.setOnClickListener(view -> {
                Intent intent = new Intent(MatchFeed.this, MatchDetails.class);
                intent.putExtra("matchID", matchId);
                intent.putExtra("puuid", ownerPuuid);
                intent.putExtra("queueType", queueType);
                intent.putExtra("gameLength", gameLength);
                intent.putExtra("placementNum", placementNum);
                startActivity(intent);
            });
        });
    }

    //Render match history with a user's puuid
    public void renderMatchHistoryWithPuuid(ScrollView matchContainer, String puuid, int numMatchesToReturn) {
        // clear any existing match tiles
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);
        //linearLayout.removeAllViews();

        //CALL API KEY FROM FIREBASE
        //Display the current api key
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");
                // spawn thread and collect data from riot api
                new Thread(() -> {
                    // get recent played match's IDs
                    String[] matchIds = RiotAPIHelper.getMatchesFromPuuid(puuid, numMatchesToReturn, currentAPIKey);

                    if (matchIds == null) {
                        System.out.println("Unable to retrieve match ids!");
                        return;
                    }

                    // populate match feed
                    for (int i = 0; i < matchIds.length; i++) {
                        String matchId = matchIds[i];
                        JsonObject matchData = RiotAPIHelper.getMatchData(matchId, currentAPIKey);
                        assert matchData != null;
                        createMatchCard(i, matchId, matchData, puuid);
                    }
                }).start();
            }
        });
    }

    // default call, uses the logged-in user's data
    public void renderMatchHistory(ScrollView matchContainer) {
        // clear any existing match tiles
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);
        linearLayout.removeAllViews();

        if (currentUser != null) {
            userId = currentUser.getUid(); //Do what you need to do with the id
            //Get a user's puuid
            DocumentReference documentReference = fStore.collection("user").document(userId);
            documentReference.addSnapshotListener(this, (value, error) -> {
                //Retrieve tft name and puuid from Firebase
                if (value != null) {
                    //RETRIEVE PUUID FROM FIREBASE
                    String PUUID = value.getString("puiid");
                    Toast.makeText(MatchFeed.this, "Pulling from your puuid through firebase", Toast.LENGTH_LONG).show();
                    renderMatchHistoryWithPuuid(matchContainer, PUUID, 10);
                }
            });
        }
    }

    //Click search to go to search page
    public void ClickSearch(View view) {
        System.out.println("Clicked search from Home");
        //CALL API KEY FROM FIREBASE
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");
                //pass currentAPIKey to search feed
                Intent intent = new Intent(MatchFeed.this, SearchFeed.class);
                intent.putExtra("currentAPIKey", currentAPIKey);
                startActivity(intent);
            }
        });
    }

    //Click menu to open drawer
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    //Open drawer layout
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    //Close drawer layout
    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //Recreate the Home activity
    public void ClickHome(View view) {
        recreate();
    }

    //Redirect to Popular Builds activity
    public void ClickPopularBuilds(View view) {
        redirectActivity(this, PopularBuildList.class);
    }

    //Redirect to Community Builds activity
    public void ClickCommunityBuilds(View view) {
        redirectActivity(this, CommunityBuildList.class);
    }

    //Redirect to Current Characters activity
    public void ClickCurrentCharacters(View view) {
        redirectActivity(this, CurrentCharacters.class);
    }

    //Redirect to Item Builder activity
    public void ClickItemBuilder(View view) {
        redirectActivity(this, ItemBuilder.class);
    }

    //Logout of app
    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(MatchFeed.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    //Redirect from one activity to another
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