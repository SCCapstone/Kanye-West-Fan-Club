package com.example.csceprojectrun2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.ArrayAdapter;
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


    String matchID[] ={};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_details);

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        currentPage = findViewById(R.id.currentPage);

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

        textViewMatch.setText ("Match:                "+MATCHID);
        textViewType.setText("Game Type:       "+queueType);
        textViewTime.setText("Time Elapsed:   "+gameLength);
        textViewPlace.setText("Placed:                "+ placementNum);


        /*
        String puuid = "bWxLgFEOjkoSZh8rQ4hGNAvIDd_gWRGlybnlqQzVaQJdMKvHACDu0fzrMJGRYNra_C61q8z2vkXKng";
        */
        //Top of screen
        currentPage.setText("Match Details");

        AtomicReference<String> kiki = null;

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();


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
                    }
                    else {
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
                        createCharacterFeed(matchIds);


                        //matchId needs to be added to list
                    }
                }).start();
            }
        });








        //Testing Scroll, match characters are being printed currently
        String matchID[] = {"1","2","3","4","5","6","7","8","9","10"};

        //String boink[] = viewMatchData(MATCHID, PUUID, "RGAPI-24d5854b-224c-4306-ad24-814c654a54e4");
        //String matchID[] =C

        System.out.println(MATCHID+"\n"+PUUID+"\n");
        currentPage.setText("Match Details");




        Ulist = findViewById(R.id.list);
        ArrayAdapter<String> arr;
        arr
                = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item, matchID);
        Ulist.setAdapter(arr);



    }


/*
    private void addToArray(String character){

        matchID.add(character);

    }
*/








    private void createCharacterFeed(String[] boink) {
        for (int i = 0; i < boink.length; i++) {

            System.out.println("Yo here?"+boink[i]);

        }
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


    //String matchID[] = {"Vi", "Zac", "Urgot", "Yuumi", "Jinx", "Tahmkench", "Jayce"};



    ListView Ulist;

    public void ClickBack(View view) {
        MainActivity.redirectActivity(this, MatchFeed.class);
    }

    public void ClickSearch(View view) {
        System.out.println("Clicked search from MainActivity");
        MainActivity.searchHandler.ClickSearch(view);
    }


}