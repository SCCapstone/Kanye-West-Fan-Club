package com.example.csceprojectrun2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.util.JsonReader;
import android.util.JsonToken;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.json.JsonObject;

public class MatchDetails extends AppCompatActivity {
    String MATCHID;
    String PUUID;
    TextView currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser currentUser;
    String queueType;
    String gameLength;
    String placementNum;
    List<Champion> championList = null;
    ScrollView detailContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_details);

        //Initialize views
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

        try {
            InputStream input = getApplicationContext().getAssets().open("set6.json");
            championList = readJsonStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }


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
            inflater.inflate(R.layout.detail_card, linearLayout, true);
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


    //Below holds for adding to clickCard
    public List<Champion> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            return readChampionsArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Champion> readChampionsArray(JsonReader reader) throws IOException {
        List<Champion> champions = new ArrayList<Champion>();

        reader.beginArray();
        while (reader.hasNext()) {
            champions.add(readChampion(reader));
        }
        reader.endArray();
        return champions;
    }

    public Champion readChampion(JsonReader reader) throws IOException {
        String cname = "";
        int cost = -1;
        Stats stats = null;
        Ability ability = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                cname = reader.nextString();
            } else if (name.equals("cost") && reader.peek() != JsonToken.NULL) {
                cost = reader.nextInt();
            } else if (name.equals("ability")) {
                ability = readAbility(reader);
            } else if (name.equals("stats")) {
                stats = readStats(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Champion(cname, cost, ability, stats);
    }

    public Ability readAbility(JsonReader reader) throws IOException {
        String a = "";
        String adesc = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name") && reader.peek() != JsonToken.NULL) {
                a = reader.nextString();
            } else if (name.equals("desc") && reader.peek() != JsonToken.NULL) {
                adesc = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Ability(a, adesc);
    }

    public Stats readStats(JsonReader reader) throws IOException {
        int armor = -1;
        double attackSpeed = -1;
        float critChance = -1;
        double critMultiplier = -1;
        int damage = -1;
        int hp = -1;
        int initialMana = -1;
        int magicResist = -1;
        int mana = -1;
        int range = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("armor") && reader.peek() != JsonToken.NULL) {
                armor = reader.nextInt();
            } else if (name.equals("attackSpeed") && reader.peek() != JsonToken.NULL) {
                attackSpeed = reader.nextDouble();
            } else if (name.equals("critChance") && reader.peek() != JsonToken.NULL) {
                critChance = (float) reader.nextDouble();
            } else if (name.equals("critMultiplier") && reader.peek() != JsonToken.NULL) {
                critMultiplier = reader.nextDouble();
            } else if (name.equals("damage") && reader.peek() != JsonToken.NULL) {
                damage = reader.nextInt();
            } else if (name.equals("hp") && reader.peek() != JsonToken.NULL) {
                hp = reader.nextInt();
            } else if (name.equals("initialMana") && reader.peek() != JsonToken.NULL) {
                initialMana = reader.nextInt();
            } else if (name.equals("magicResist") && reader.peek() != JsonToken.NULL) {
                magicResist = reader.nextInt();
            } else if (name.equals("mana") && reader.peek() != JsonToken.NULL) {
                mana = reader.nextInt();
            } else if (name.equals("range") && reader.peek() != JsonToken.NULL) {
                range = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Stats(armor, attackSpeed, critChance, critMultiplier, damage, hp, initialMana, magicResist, mana, range);
    }


    public void ClickCard(View view) {

        /*
        Champion champion = null;
        TextView detailedCharacterNameUI = view.findViewById(R.id.detailName);


        for (int i = 0; i < championList.size(); i++) {
            if (championList.get(i).getName() == detailedCharacterNameUI.getText()) {
                champion = championList.get(i);
                System.out.println(champion);
            }
        }
        //Redirect to Character Info
        //Initialize intent
        Intent intent = new Intent(this, CharacterInfo.class);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Champion champion = (Champion)view.getTag(R.id.tag);
        intent.putExtra("champion", champion);
        //Start activity
        this.startActivity(intent);*/
    }

    public void ClickBack(View view) {
        finish();
    }

    public void ClickSearch(View view) {
        System.out.println("Clicked search from MainActivity");
        MainActivity.searchHandler.ClickSearch(view);
    }
}