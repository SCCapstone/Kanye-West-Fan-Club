package com.example.csceprojectrun2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.json.JsonObject;

public class MatchDetails extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String MATCHID;
    String PUUID;
    TextView currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser currentUser;
    String queueType;
    String gameLength;
    String placementNum;

    ScrollView detailContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_details);


        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        currentPage = findViewById(R.id.currentPage);
        detailContainer = findViewById(R.id.detail_container);

        //Top of screen
        currentPage.setText("Match Details");

        final TextView textViewMatch = findViewById(R.id.match);
        final TextView textViewType = findViewById(R.id.type);
        final TextView textViewTime = findViewById(R.id.time);
        final TextView textViewPlace = findViewById(R.id.place);

        //Receives match id and puuid from a match card clicked on Match feed
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            MATCHID = bundle.getString("matchID");
            PUUID = bundle.getString("puuid");
            queueType = bundle.getString("queueType");
            gameLength = bundle.getString("gameLength");
            placementNum = bundle.getString("placementNum");
        }

        textViewMatch.setText("Match:                " + MATCHID);
        textViewType.setText("Game Type:       " + queueType);
        textViewTime.setText("Time Elapsed:   " + gameLength);
        textViewPlace.setText("Placed:                " + placementNum);


        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        //start to create detailed cards
        renderMatchHistory(detailContainer);

        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");

                // spawn thread and collect data from riot api
                new Thread(() -> {
                    // get recent played match's IDs
                    String[] matchIds = RiotAPIHelper.viewMatchData(MATCHID, PUUID, currentAPIKey);

                    if (matchIds == null) {
                        System.out.println("Unable to retrieve match characters!");
                        return;
                    } else {
                        // populate match feed
                        for (int i = 0; i < matchIds.length; i++) {
                            String matchId = matchIds[i];

                            System.out.println(matchId);

                        }

                    }
                }).start();
            }
        });


        System.out.println(MATCHID + "\n" + PUUID + "\n");

    }



    private void createCharacterCard(int cardPosition, String matchId) {
        LinearLayout linearLayout = detailContainer.findViewById(R.id.detail_container_linear_layout);

        runOnUiThread(() -> {

            // build new detailed character tiles
            LayoutInflater inflater = getLayoutInflater();

            // create the card UI element
            inflater.inflate( R.layout.detail_card, linearLayout, true);

            // get and update new card
            View newDetailedCharacterCard = linearLayout.getChildAt(cardPosition);

            //newCharacterCard.setTag(R.id.tag, storedValue);

            TextView detailedCharacterNameUI = newDetailedCharacterCard.findViewById(R.id.detailName);

            //ImageView detailedCharacterImageUI = newDetailedCharacterCard.findViewById(R.id.characterImage);

            detailedCharacterNameUI.setText(matchId);

            //detailedCharacterImageUI.setImageResource(championID);

            System.out.println(newDetailedCharacterCard.getId());




        });

    }


    public void renderMatchHistory(ScrollView detailContainer) {
        // clear any existing character tiles
        LinearLayout linearLayout = detailContainer.findViewById(R.id.detail_container_linear_layout);
        linearLayout.removeAllViews();


        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
                    //Retrieve api key from Firebase
                    if (value != null) {
                        String currentAPIKey = value.getString("apikey");

                        // spawn thread and collect data from riot api
                        new Thread(() -> {
                            // get recent played characters played
                            String[] matchIds = RiotAPIHelper.viewMatchData(MATCHID, PUUID, currentAPIKey);

                            if (matchIds == null) {
                                System.out.println("Unable to retrieve match characters!");
                                return;
                            } else {
                                // populate match feed
                                for (int i = 0; i < matchIds.length; i++) {
                                    String matchId = matchIds[i];
                                    System.out.println(matchId);

                                    createCharacterCard(i, matchId);
                                }
                                //matchId needs to be added to list
                            }
                        }).start();
                    }
                });

    }





    public void ClickCard(View view) {

    }






    public void ClickBack(View view) {
        finish();
    }

    public void ClickSearch(View view) {
        System.out.println("Clicked search from MainActivity");
        MainActivity.searchHandler.ClickSearch(view);
    }


}