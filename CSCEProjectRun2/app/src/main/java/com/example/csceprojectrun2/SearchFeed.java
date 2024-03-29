package com.example.csceprojectrun2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.json.JsonArray;
import javax.json.JsonObject;

public class SearchFeed extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ScrollView matchContainer;
    TextView tftName, currentPage;
    String userId, gameName, puuid, currentAPIKey;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_feed);

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.TFTName);
        currentPage = findViewById(R.id.currentPage);
        matchContainer = findViewById(R.id.match_container);

        //Receives match id and puuid from a match card clicked on Match feed
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gameName = bundle.getString("gameName");
            puuid = bundle.getString("puuid");
            currentAPIKey = bundle.getString("currentAPIKey");
        }


        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        //Coming from popular builds, community builds, current characters, or item builder
        if (gameName == null & puuid == null) {
            if (currentUser != null) {
                userId = currentUser.getUid(); //Do what you need to do with the id
                ClickSearch(matchContainer);
                matchContainer.setVisibility(View.INVISIBLE);

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
                currentPage.setText("Search");
            }
        }
        //Coming from search feed or home feed
        else {
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
                currentPage.setText(gameName);
            }

        }
    }

    //Convert time into a time stamp
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

    //Get match queue type
    private String getQueueType(int queueId) {
        if (queueId == 1100)
            return "Ranked";
        else
            return "Normal";
    }

    //Apply champion images to card
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

    //Create a match card for specific matchID
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
                Intent intent = new Intent(SearchFeed.this, MatchDetails.class);
                intent.putExtra("matchID", matchId);
                intent.putExtra("puuid", ownerPuuid);


                intent.putExtra("queueType", queueType);
                intent.putExtra("gameLength", gameLength);
                intent.putExtra("placementNum", placementNum);


                startActivity(intent);
            });
        });
    }

    //Render match history using a user's puuid
    public void renderMatchHistoryWithPuuid(String puuid, int numMatchesToReturn) {
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

    //default call, uses the logged-in user's data to render match history
    public void renderMatchHistory(ScrollView matchContainer) {
        // clear any existing match tiles
        LinearLayout linearLayout = matchContainer.findViewById(R.id.match_container_linear_layout);
        linearLayout.removeAllViews();
        Toast.makeText(SearchFeed.this, "Pulling Match Data for Player", Toast.LENGTH_LONG).show();
        renderMatchHistoryWithPuuid(puuid, 10);
    }

    //Click on search and return to search panel
    public void ClickSearch(View view) {
        System.out.println("Clicked search from Search Feed");

        //Initialize views
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
        Button searchBack = searchLinearLayout.findViewById(R.id.SearchBackBtn);
        ImageButton searchClear = searchLinearLayout.findViewById(R.id.SearchClearBtn);
        searchCard.setVisibility(View.VISIBLE);

        //CALL API KEY FROM FIREBASE
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");
                riotIDSearchActivate.setOnClickListener(v -> {

                    String gameName = searchRiotIDInput.getText().toString();
                    String tagLine = taglineInput.getText().toString();
                    String fullRiotID = gameName + "#" + tagLine;

                    //Display errors when game name or tag line are empty
                    if (TextUtils.isEmpty(gameName)) {
                        searchRiotIDInput.setError("Riot ID is Required.");
                        return;
                    }
                    if (TextUtils.isEmpty(tagLine)) {
                        taglineInput.setError("Tagline is Required.");
                        return;
                    }
                    System.out.println("Full Riot ID being searched: " + fullRiotID);

                    new Thread(() -> {
                        String puuid = RiotAPIHelper.getPuuidFromRiotID(gameName, tagLine, currentAPIKey);
                        //pass searched game name, tagline, currentAPIKey to search feed
                        Intent intent = new Intent(SearchFeed.this, SearchFeed.class);
                        intent.putExtra("gameName", gameName);
                        intent.putExtra("puuid", puuid);
                        intent.putExtra("currentAPIKey", currentAPIKey);
                        startActivity(intent);
                    }).start();
                });
            }
        });

        //Click on clear button to remove search panel
        searchClear.setOnClickListener(v -> searchCard.setVisibility(View.GONE));

        //Click on back button to return to previous page
        searchBack.setOnClickListener(v -> finish());
    }

    //Click menu to open drawer
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    //Open drawer layout
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    //Close drawer
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
        redirectActivity(this, MatchFeed.class);
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

    //Click to logout of app
    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(SearchFeed.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
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
