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

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ItemBuilder extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    ScrollView itemContainer;
    List<Item> itemList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_feed);

        // Connect to the item builder json file.
        try {
            InputStream input = getApplicationContext().getAssets().open("itemBuilder.json");
            itemList = readJsonStream(input);
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }

        //Assign variables
        drawerLayout = findViewById(R.id.drawer_layout);
        itemContainer = findViewById(R.id.item_container);
        renderItems(itemContainer);
    }

    private void createItemCard(int cardPosition, Item item) {
        LinearLayout linearLayout = itemContainer.findViewById(R.id.item_container_linear_layout);
        String itemName = item.getName();
        String itemImage = itemName.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        int itemID = this.getResources().getIdentifier(itemImage, "drawable", this.getPackageName());

        runOnUiThread(() -> {
            // build new item tile
            LayoutInflater inflater = getLayoutInflater();
            // create the card UI element
            inflater.inflate(R.layout.item_card, linearLayout, true);
            // get and update new card
            View newItemCard = linearLayout.getChildAt(cardPosition);
            TextView itemNameUI = newItemCard.findViewById(R.id.itemName);
            ImageView itemImageUI = newItemCard.findViewById(R.id.itemImage);
            itemNameUI.setText(itemName);
            itemImageUI.setImageResource(itemID);
            System.out.println(newItemCard.getId());
        });
    }


    public void renderItems(ScrollView itemContainer) {
        // clear any existing item tiles
        LinearLayout linearLayout = itemContainer.findViewById(R.id.item_container_linear_layout);
        linearLayout.removeAllViews();

        // spawn thread to create instances of item card
        new Thread(() -> {
            // populate item feed
            for (int i = 0; i < itemList.size(); i++) {
                createItemCard(i, itemList.get(i));
            }
        }).start();
    }


    public List<Item> readJsonStream(InputStream input) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return readItemArray(reader);
        }
    }

    public List<Item> readItemArray(JsonReader reader) throws IOException {
        // Create a list of items
        List<Item> items = new ArrayList<>();
        // Read item and add it to the list
        reader.beginArray();
        while (reader.hasNext()) {
            items.add(readItems(reader));
        }
        reader.endArray();
        return items;
    }

    public List<ItemBuild> readBuildItemArray(JsonReader reader) throws IOException {
        // Create a list of build items
        List<ItemBuild> itemBuilds = new ArrayList<>();
        // Read build item and add it to the list
        reader.beginArray();
        while (reader.hasNext()) {
            itemBuilds.add(readDetails(reader));
        }
        reader.endArray();
        return itemBuilds;
    }

    public Item readItems(JsonReader reader) throws IOException {
        String itemName = "";
        List<ItemBuild> itemBuilds = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                itemName = reader.nextString();
            } else if (name.equals("buildItems")) {
                itemBuilds = readBuildItemArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Item(itemName, itemBuilds);
    }

    public ItemBuild readDetails(JsonReader reader) throws IOException {
        String buildName = "";
        String firstItemName = "";
        String secondItemName = "";
        String attr1 = "";
        String attr2 = "";
        String attr3 = "";
        String desc = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("buildName") && reader.peek() != JsonToken.NULL) {
                buildName = reader.nextString();
            } else if (name.equals("firstItemName") && reader.peek() != JsonToken.NULL) {
                firstItemName = reader.nextString();
            } else if (name.equals("secondItemName") && reader.peek() != JsonToken.NULL) {
                secondItemName = reader.nextString();
            } else if (name.equals("attr1") && reader.peek() != JsonToken.NULL) {
                attr1 = reader.nextString();
            } else if (name.equals("attr2") && reader.peek() != JsonToken.NULL) {
                attr2 = reader.nextString();
            } else if (name.equals("attr3") && reader.peek() != JsonToken.NULL) {
                attr3 = reader.nextString();
            } else if (name.equals("desc") && reader.peek() != JsonToken.NULL) {
                desc = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new ItemBuild(buildName, firstItemName, secondItemName, attr1, attr2, attr3, desc);
    }

    public void ClickCard(View view) {
        Item item = null;
        TextView itemNameUI = view.findViewById(R.id.itemName);
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getName() == itemNameUI.getText()) {
                item = itemList.get(i);
            }
        }
        Intent intent = new Intent(this, ItemBuildInfo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("item", item);
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
        MainActivity.redirectActivity(this, MatchFeed.class);
    }

    public void ClickPopularBuilds(View view) {
        //Redirect to Popular Builds activity
        MainActivity.redirectActivity(this, PopularBuilds.class);
    }

    public void ClickCommunityBuilds(View view) {
        //Recreate the Community Builds activity
        recreate();
    }

    public void ClickCurrentCharacters(View view) {
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this, CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view) {
        //Redirect to Item Builder activity
        MainActivity.redirectActivity(this, ItemBuilder.class);
    }

    public void ClickLogout(View view) {
        //Logout and close app
        MainActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}
