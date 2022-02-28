package com.example.csceprojectrun2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

public class CurrentCharacters extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    ScrollView characterContainer;
    List<Champion> championList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_feed);
        try {
            InputStream input = getApplicationContext().getAssets().open("set6.json");
            championList = readJsonStream(input);
        }catch(IOException e) {}


        drawerLayout = findViewById(R.id.drawer_layout);
        characterContainer = findViewById(R.id.character_container);
        renderMatchHistory(characterContainer);

    }

    private void createCharacterCard(int cardPosition, Champion champion) {
        Champion storedValue = champion;
        LinearLayout linearLayout = characterContainer.findViewById(R.id.character_container_linear_layout);

        String championName = champion.getName();
        String championImage = championName.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        int championID = this.getResources().getIdentifier(championImage, "drawable", this.getPackageName());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
            }
        });
    }

    public void renderMatchHistory(ScrollView characterContainer) {
        // clear any existing character tiles
        LinearLayout linearLayout = characterContainer.findViewById(R.id.character_container_linear_layout);
        linearLayout.removeAllViews();

        // spawn thread to create instances of character card
        new Thread(new Runnable(){
            @Override
            public void run() {

                // populate match feed
                for(int i=0; i<championList.size(); i++) {
                    createCharacterCard(i, championList.get(i));
                }
            }
        }).start();
    }

    public List<Champion> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
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
                critChance = (float)reader.nextDouble();
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
        Champion champion = null;
        TextView characterNameUI = view.findViewById(R.id.characterName);
        for(int i = 0; i < championList.size(); i++) {
            if(championList.get(i).getName() == characterNameUI.getText()) {
                champion = championList.get(i);
            }
        }
        //Redirect to Character Info
        //MainActivity.redirectActivity(this,CharacterInfo.class);
        //Initialize intent
        Intent intent = new Intent(this,CharacterInfo.class);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Champion champion = (Champion)view.getTag(R.id.tag);
        intent.putExtra("champion", champion);
        //System.out.println(view.getId());
        //System.out.println("-----------@-@-------------" + champion.getName());
        //Start activity
        this.startActivity(intent);

    }

    public void ClickMenu(View view) {
        //Open drawer
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        //Redirect to home activity
        MainActivity.redirectActivity(this,MatchFeed.class);
    }

    public void ClickPopularBuilds(View view){
        //Redirect to Popular Builds activity
        MainActivity.redirectActivity(this,PopularBuilds.class);
    }

    public void ClickCommunityBuilds(View view) {
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this,CommunityBuilds.class);
    }

    public void ClickCurrentCharacters(View view) {
        //Recreate the Current Characters activity
        recreate();
    }

    public void ClickItemBuilder(View view) {
        //Redirect to Item Builder activity
        MainActivity.redirectActivity(this,ItemBuilder.class);
    }

    public void ClickLogout(View view){
        //Logout and close app
        MainActivity.logout(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}