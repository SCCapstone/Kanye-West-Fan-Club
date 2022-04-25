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

        //Top of screen
        currentPage.setText("Match Details");
        detailContainer = findViewById(R.id.detail_container);


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


        renderMatchHistory(detailContainer);

        DocumentReference documentReference = fStore.collection("apikey").document("key");
        //DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");

                System.out.println("HEY API KEY HERE" + currentAPIKey);
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


                            //addToArray(matchIds[i]);


                            System.out.println(matchId);

                            //test[i]= matchIds[i];
                            //JsonObject matchData = RiotAPIHelper.getMatchData(matchId, currentAPIKey);
                            //assert matchData != null;
                            //createMatchCard(i, matchId, matchData, puuid);
                        }

                        //matchId needs to be added to list
                    }
                }).start();
            }
        });


        //Testing Scroll, match characters are being printed



        //String matchID[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        //String boink[] = viewMatchData(MATCHID, PUUID, "RGAPI-24d5854b-224c-4306-ad24-814c654a54e4");
        //String matchID[] =C

        System.out.println(MATCHID + "\n" + PUUID + "\n");
        currentPage.setText("Match Details");

    }








    private void createCharacterCard(int cardPosition, String matchId) {
        LinearLayout linearLayout = detailContainer.findViewById(R.id.detail_container_linear_layout);

        runOnUiThread(() -> {

            // build new character tiles
            LayoutInflater inflater = getLayoutInflater();

            // create the card UI element
            inflater.inflate(R.layout.detail_card, linearLayout, true);

            // get and update new card
            View newCharacterCard = linearLayout.getChildAt(cardPosition);

            //newCharacterCard.setTag(R.id.tag, storedValue);

            TextView characterNameUI = newCharacterCard.findViewById(R.id.detailName);

            //ImageView characterImageUI = newCharacterCard.findViewById(R.id.characterImage);

            characterNameUI.setText(matchId);

            //characterImageUI.setImageResource(championID);

            System.out.println(newCharacterCard.getId());




        });

        //picture too
        /*
        String championImage = championName.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        int championID = this.getResources().getIdentifier(championImage, "drawable", this.getPackageName());

        runOnUiThread(() -> {
            // build new character tiles
            LayoutInflater inflater = getLayoutInflater();

            // create the card UI element
            inflater.inflate(R.layout.detail_card, linearLayout, true);

            // get and update new card
            View newCharacterCard = linearLayout.getChildAt(cardPosition);

            //newCharacterCard.setTag(R.id.tag, storedValue);

            TextView characterNameUI = newCharacterCard.findViewById(R.id.characterName);

            ImageView characterImageUI = newCharacterCard.findViewById(R.id.characterImage);

            characterNameUI.setText(championName);

            characterImageUI.setImageResource(championID);

            System.out.println(newCharacterCard.getId());
        });*/
    }


    public void renderMatchHistory(ScrollView detailContainer) {
        // clear any existing character tiles
        LinearLayout linearLayout = detailContainer.findViewById(R.id.detail_container_linear_layout);
        linearLayout.removeAllViews();


/*
        // spawn thread to create instances of character card
        new Thread(() -> {

            // populate match feed
            for (int i = 0; i < detailContainer.size(); i++) {
                createCharacterCard(i, detailContainer.get(i));
            }
        }).start();
*/

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
                    //Retrieve api key from Firebase
                    if (value != null) {
                        String currentAPIKey = value.getString("apikey");

                        System.out.println("HEY API KEY HERE" + currentAPIKey);
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







/*
    private void addToArray(String character){

        matchID.add(character);

    }
*/








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


    //String matchID[] = {"Vi", "Zac", "Urgot", "Yuumi", "Jinx", "Tahmkench", "Jayce"};




    public void ClickBack(View view) {
        finish();
    }

    public void ClickSearch(View view) {
        System.out.println("Clicked search from MainActivity");
        MainActivity.searchHandler.ClickSearch(view);
    }


}