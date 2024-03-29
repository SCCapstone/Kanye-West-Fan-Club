package com.example.csceprojectrun2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ItemBuildInfo extends AppCompatActivity {
    ScrollView buildItemContainer;
    TextView currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_build_feed);

        Bundle bundle = getIntent().getExtras();
        Item item = (Item) bundle.getSerializable("item");
        List<ItemBuild> details = item.getDetails();

        //Initialize views
        currentPage = findViewById(R.id.currentPage);
        buildItemContainer = findViewById(R.id.item_build_container);
        renderBuildItems(buildItemContainer, details);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Set title of the current page
        currentPage.setText("Item Builder");
    }

    //Render build items for a item
    private void renderBuildItems(ScrollView buildItemContainer, List<ItemBuild> itemBuildList) {
        // clear any existing build item tiles
        LinearLayout linearLayout = buildItemContainer.findViewById(R.id.item_build_container_linear_layout);
        linearLayout.removeAllViews();

        // spawn thread to create instances of build item card
        new Thread(() -> {
            // populate build item feed
            for (int i = 0; i < itemBuildList.size(); i++) {
                createBuildItemCard(i, itemBuildList.get(i));
            }
        }).start();
    }

    //Create a build item card
    private void createBuildItemCard(int cardPosition, ItemBuild build) {
        LinearLayout linearLayout = buildItemContainer.findViewById(R.id.item_build_container_linear_layout);
        String buildName = build.getBuildName();
        String firstItemName = build.getFirstItemName();
        String secondItemName = build.getSecondItemName();
        String attr1 = build.getAttr1();
        String attr2 = build.getAttr2();
        String attr3 = build.getAttr3();
        String desc = build.getDesc();

        String buildImage = buildName.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String firstItemImage = firstItemName.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String secondItemImage = secondItemName.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";

        int buildID = this.getResources().getIdentifier(buildImage, "drawable", this.getPackageName());
        int firstItemID = this.getResources().getIdentifier(firstItemImage, "drawable", this.getPackageName());
        int secondItemID = this.getResources().getIdentifier(secondItemImage, "drawable", this.getPackageName());

        runOnUiThread(() -> {
            // build new item tile
            LayoutInflater inflater = getLayoutInflater();
            // create the card UI element
            inflater.inflate(R.layout.item_build_card, linearLayout, true);
            // get and update new card
            View newBuildItemCard = linearLayout.getChildAt(cardPosition);

            TextView buildNameUI = newBuildItemCard.findViewById(R.id.buildName);
            TextView firstItemNameUI = newBuildItemCard.findViewById(R.id.firstItemName);
            TextView secondItemNameUI = newBuildItemCard.findViewById(R.id.secondItemName);
            TextView attr1UI = newBuildItemCard.findViewById(R.id.attr1);
            TextView attr2UI = newBuildItemCard.findViewById(R.id.attr2);
            TextView attr3UI = newBuildItemCard.findViewById(R.id.attr3);
            TextView descUI = newBuildItemCard.findViewById(R.id.desc);

            ImageView buildImageUI = newBuildItemCard.findViewById(R.id.buildImage);
            ImageView firstItemImageUI = newBuildItemCard.findViewById(R.id.firstItemImage);
            ImageView secondItemImageUI = newBuildItemCard.findViewById(R.id.secondItemImage);

            //Set up text
            buildNameUI.setText(buildName);
            firstItemNameUI.setText(firstItemName);
            secondItemNameUI.setText(secondItemName);
            attr1UI.setText(attr1);
            attr2UI.setText(attr2);
            attr3UI.setText(attr3);
            descUI.setText(desc);

            //Set up images
            buildImageUI.setImageResource(buildID);
            firstItemImageUI.setImageResource(firstItemID);
            secondItemImageUI.setImageResource(secondItemID);

            System.out.println(newBuildItemCard.getId());
        });
    }

    //Return to previous page
    public void ClickBack(View view) {
        finish();
    }
}