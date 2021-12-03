package com.example.csceprojectrun2;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CurrentCharacters extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    List<Champion> championList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_characters);
        try {
            InputStream input = getApplicationContext().getAssets().open("set6.json");
            championList = readJsonStream(input);
        }catch(IOException e) {}
        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
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