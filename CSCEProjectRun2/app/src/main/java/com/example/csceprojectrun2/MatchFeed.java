package com.example.csceprojectrun2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import android.content.Intent;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class MatchFeed extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ScrollView matchContainer;
    TextView tftName, currentPage;

    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageView summonerIcon, player1, player2, player3, player4, player5, player6;

    String matid;

    public void ClickSearch(View view) {
        System.out.println("Clicked search from MatchFeed");

        LinearLayout topBarLinearLayout = findViewById(R.id.MainTopBar);
        CardView searchCard = topBarLinearLayout.findViewById(R.id.SearchCard);
        CardView backgroundCard = searchCard.findViewById(R.id.SearchCard);
        LinearLayout searchLinearLayout = backgroundCard.findViewById(R.id.SearchLinearLayout);

        // Riot ID input
        TextInputLayout riotIDTextInputLayout = searchLinearLayout.findViewById(R.id.RiotIDTextInputLayout);
        TextInputEditText searchRiotIDInput = riotIDTextInputLayout.findViewById(R.id.SearchRiotIDInput);

        // Tagline Input
        LinearLayout searchTaglineLinearLayout = searchLinearLayout.findViewById(R.id.SearchTaglineLinearLayout);
        TextInputLayout taglineTextInputLayout = searchTaglineLinearLayout.findViewById(R.id.TaglineTextInputLayout);
        TextInputEditText taglineInput = taglineTextInputLayout.findViewById(R.id.TaglineInput);

        // Button
        Button riotIDSearchActivate = searchLinearLayout.findViewById(R.id.RiotIDSearchActivate);

        searchCard.setVisibility(View.VISIBLE);


        //CALL API KEY FROM FIREBASE
        //Display the current api key
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            assert value != null;
            String currentAPIKey = value.getString("apikey");
            ////////////////////////////////////////////////////////
            riotIDSearchActivate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    searchCard.setVisibility(View.GONE);

                    String gameName = searchRiotIDInput.getText().toString();
                    String tagLine = taglineInput.getText().toString();
                    String fullRiotID = gameName + "#" + tagLine;

                    System.out.println("Full Riot ID being searched: " + fullRiotID);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String puuid = RiotAPIHelper.getPuuidFromRiotID(gameName, tagLine, currentAPIKey);

                            renderMatchHistoryWithPuuid(matchContainer, puuid, 6);
                        }
                    }).start();
                }
            });
            ////////////////////////////////////////////////////////

        });
    }

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

                applyChampionImages(newMatchCard, participantData);
            }
        });
    }

    public void renderMatchHistoryWithPuuid(ScrollView matchContainer, String puuid, int numMatchesToReturn) {
        // clear any existing match tiles
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);
        linearLayout.removeAllViews();

        //CALL API KEY FROM FIREBASE
        //Display the current api key
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            assert value != null;
            String currentAPIKey = value.getString("apikey");
            // spawn thread and collect data from riot api
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // get recent played match's IDs
                    String[] matchIds = RiotAPIHelper.getMatchesFromPuuid(puuid, 6, currentAPIKey);

                    if (matchIds == null) {
                        System.out.println("Unable to retrieve match ids!");
                        return;
                    }

                    // populate match feed
                    for (int i = 0; i < matchIds.length; i++) {
                        String matchId = matchIds[i];
                        JsonObject matchData = RiotAPIHelper.getMatchData(matchId, currentAPIKey);
                        createMatchCard(i, matchId, matchData, puuid);
                    }
                }
            }).start();
        });
    }

    // default call, uses the logged-in user's data
    public void renderMatchHistory(ScrollView matchContainer) {
        // clear any existing match tiles
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);
        linearLayout.removeAllViews();

        //Get a user's puiid
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve tft name and puiid from Firebase
            assert value != null;
            //RETRIEVE PUIID FROM FIREBASE
            String PUIID = value.getString("puiid");
            Toast.makeText(MatchFeed.this, "Pulling from your puuid through firebase", Toast.LENGTH_LONG).show();
            renderMatchHistoryWithPuuid(matchContainer, PUIID, 6);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_feed);

        System.out.println("onCreate!!!!!!!!!");

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.tftName);
        currentPage = findViewById(R.id.currentPage);
        matchContainer = findViewById(R.id.match_container);
        summonerIcon = findViewById(R.id.summonerIcon);
        player1 = findViewById(R.id.imageView1);
        player2 = findViewById(R.id.imageView2);
        player3 = findViewById(R.id.imageView3);
        player4 = findViewById(R.id.imageView4);
        player5 = findViewById(R.id.imageView5);
        player6 = findViewById(R.id.imageView6);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        renderMatchHistory(matchContainer);

        //Display current user's tft name in navigation drawer
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve tft name from Firebase
            assert value != null;
            tftName.setText(value.getString("tftName"));
            tftName.setVisibility(View.VISIBLE);
        });
        currentPage.setText("Home");
    }
/////////////////////////////////////////////////////////////////////////////////////////////

    public void ClickMatch(View view) {
        //Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();


        /*
        Intent intent = new Intent(MatchFeed.this,MatchDetails.class);
        String message = "yoink yoink";
        intent.putExtra("message_key", message);
        startActivity(intent);

         */
        matid = "";

//MatchData MatchID -> passed
        redirectActivity(this, MatchDetails.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

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

    public void ClickPopularBuilds(View view) {
        //Redirect to Popular Builds activity
        redirectActivity(this, PopularBuilds.class);
    }

    public void ClickCommunityBuilds(View view) {
        //Redirect to Community Builds activity
        redirectActivity(this, CommunityBuildList.class);
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
        Toast.makeText(MatchFeed.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
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