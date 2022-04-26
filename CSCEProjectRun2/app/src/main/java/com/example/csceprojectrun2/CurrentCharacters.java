package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CurrentCharacters extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ScrollView characterContainer;
    List<Champion> championList = null;
    TextView tftName, currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_feed);
        try {
            InputStream input = getApplicationContext().getAssets().open("set6.json");
            championList = readJsonStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.TFTName);
        currentPage = findViewById(R.id.currentPage);
        characterContainer = findViewById(R.id.character_container);

        renderMatchHistory(characterContainer);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            userId = currentUser.getUid(); //Do what you need to do with the id

            //Display current user's tft name in navigation drawer
            DocumentReference documentReference = fStore.collection("user").document(userId);
            documentReference.addSnapshotListener(this, (value, error) -> {
                //Retrieve tft name and puuid from Firebase
                if (value != null) {
                    String TFTName = value.getString("tftName");
                    tftName.setVisibility(View.VISIBLE);
                    tftName.setText(TFTName);
                }
            });
        }
        currentPage.setText("Current Characters");
    }

    //Create a character card for given champion
    private void createCharacterCard(int cardPosition, Champion champion) {
        Champion storedValue = champion;
        LinearLayout linearLayout = characterContainer.findViewById(R.id.character_container_linear_layout);

        String championName = champion.getName();
        String championImage = championName.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        int championID = this.getResources().getIdentifier(championImage, "drawable", this.getPackageName());

        runOnUiThread(() -> {
            // build new character tiles
            LayoutInflater inflater = getLayoutInflater();

            // create the card UI element
            inflater.inflate(R.layout.character_card, linearLayout, true);

            // get and update new card
            View newCharacterCard = linearLayout.getChildAt(cardPosition);

            //newCharacterCard.setTag(R.id.tag, storedValue);

            TextView characterNameUI = newCharacterCard.findViewById(R.id.characterName);

            ImageView characterImageUI = newCharacterCard.findViewById(R.id.characterImage);

            characterNameUI.setText(championName);

            characterImageUI.setImageResource(championID);

            System.out.println(newCharacterCard.getId());
        });
    }

    //Render champion list
    public void renderMatchHistory(ScrollView characterContainer) {
        // clear any existing character tiles
        LinearLayout linearLayout = characterContainer.findViewById(R.id.character_container_linear_layout);
        linearLayout.removeAllViews();

        // spawn thread to create instances of character card
        new Thread(() -> {

            // populate match feed
            for (int i = 0; i < championList.size(); i++) {
                createCharacterCard(i, championList.get(i));
            }
        }).start();
    }

    //Read Json Stream
    public List<Champion> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            return readChampionsArray(reader);
        } finally {
            reader.close();
        }
    }

    //Read array of champions
    public List<Champion> readChampionsArray(JsonReader reader) throws IOException {
        List<Champion> champions = new ArrayList<Champion>();

        reader.beginArray();
        while (reader.hasNext()) {
            champions.add(readChampion(reader));
        }
        reader.endArray();
        return champions;
    }

    //Read data from a champion
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

    //Read ability from a champion
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

    //Read stats from a champion
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

    //Click on a card
    public void ClickCard(View view) {
        Champion champion = null;
        TextView characterNameUI = view.findViewById(R.id.characterName);
        for (int i = 0; i < championList.size(); i++) {
            if (championList.get(i).getName() == characterNameUI.getText()) {
                champion = championList.get(i);
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
        this.startActivity(intent);
    }

    //Click search to go to search screen
    public void ClickSearch(View view) {
        System.out.println("Clicked search from Current Characters");
        //CALL API KEY FROM FIREBASE
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");
                //pass currentAPIKey to search feed
                Intent intent = new Intent(CurrentCharacters.this, SearchFeed.class);
                intent.putExtra("currentAPIKey", currentAPIKey);
                startActivity(intent);
            }
        });
    }

    //Click menu to open drawer
    public void ClickMenu(View view) {
        MainActivity.openDrawer(drawerLayout);
    }

    //Redirect to home activity
    public void ClickHome(View view) {
        MainActivity.redirectActivity(this, MatchFeed.class);
    }

    //Redirect to Popular Builds activity
    public void ClickPopularBuilds(View view) {
        MainActivity.redirectActivity(this, PopularBuildList.class);
    }

    //Redirect to Community Builds activity
    public void ClickCommunityBuilds(View view) {
        MainActivity.redirectActivity(this, CommunityBuildList.class);
    }

    //Recreate the Current Characters activity
    public void ClickCurrentCharacters(View view) {
        recreate();
    }

    //Redirect to Item Builder activity
    public void ClickItemBuilder(View view) {
        MainActivity.redirectActivity(this, ItemBuilder.class);
    }

    //Logout of app
    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(CurrentCharacters.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}