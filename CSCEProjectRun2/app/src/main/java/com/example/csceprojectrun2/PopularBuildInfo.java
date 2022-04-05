package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class PopularBuildInfo extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ScrollView popularBuildContainer;
    TextView tftName, currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_build_feed);
        drawerLayout = findViewById(R.id.drawer_layout);

        Bundle bundle = getIntent().getExtras();
        BuildType type = (BuildType) bundle.getSerializable("type");
        List<PopularBuild> popularBuilds = type.getPopularBuilds();

        //Assign variables
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.tftName);
        currentPage = findViewById(R.id.currentPage);
        popularBuildContainer = findViewById(R.id.west_container);
        renderPopularBuilds(popularBuildContainer, popularBuilds);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();


        //Display current user's tft name in navigation drawer
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve tft name and puiid from Firebase
            assert value != null;
            String TFTName = value.getString("tftName");
            tftName.setVisibility(View.VISIBLE);
            tftName.setText(TFTName);
        });
        currentPage.setText("Popular Builds");
    }

    private void renderPopularBuilds(ScrollView popularBuildContainer, List<PopularBuild> popularBuildList) {
        // clear any existing popular build tiles
        LinearLayout linearLayout = popularBuildContainer.findViewById(R.id.west_container_linear_layout);
        linearLayout.removeAllViews();

        // spawn thread to create instances of popular build card
        new Thread(() -> {
            // populate popular build feed
            for (int i = 0; i < popularBuildList.size(); i++) {
                createPopularBuildCard(i, popularBuildList.get(i));
            }
        }).start();
    }

    private void createPopularBuildCard(int cardPosition, PopularBuild popularBuild) {
        LinearLayout linearLayout = popularBuildContainer.findViewById(R.id.west_container_linear_layout);
        String buildName = popularBuild.getBuildName();
        String unit1 = popularBuild.getUnit1();
        String unit2 = popularBuild.getUnit2();
        String unit3 = popularBuild.getUnit3();
        String unit4 = popularBuild.getUnit4();
        String unit5 = popularBuild.getUnit5();
        String unit6 = popularBuild.getUnit6();
        String unit7 = popularBuild.getUnit7();
        String unit8 = popularBuild.getUnit8();

        String unit1Image = unit1.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String unit2Image = unit2.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String unit3Image = unit3.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String unit4Image = unit4.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String unit5Image = unit5.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String unit6Image = unit6.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String unit7Image = unit7.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        String unit8Image = unit8.toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";

        int unit1ID = this.getResources().getIdentifier(unit1Image, "drawable", this.getPackageName());
        int unit2ID = this.getResources().getIdentifier(unit2Image, "drawable", this.getPackageName());
        int unit3ID = this.getResources().getIdentifier(unit3Image, "drawable", this.getPackageName());
        int unit4ID = this.getResources().getIdentifier(unit4Image, "drawable", this.getPackageName());
        int unit5ID = this.getResources().getIdentifier(unit5Image, "drawable", this.getPackageName());
        int unit6ID = this.getResources().getIdentifier(unit6Image, "drawable", this.getPackageName());
        int unit7ID = this.getResources().getIdentifier(unit7Image, "drawable", this.getPackageName());
        int unit8ID = this.getResources().getIdentifier(unit8Image, "drawable", this.getPackageName());

        runOnUiThread(() -> {
            // build new popular build tile
            LayoutInflater inflater = getLayoutInflater();
            // create the card UI element
            inflater.inflate(R.layout.popular_build_card, linearLayout, true);
            // get and update new card
            View newPopularBuildCard = linearLayout.getChildAt(cardPosition);

            TextView buildNameUI = newPopularBuildCard.findViewById(R.id.buildName);
            TextView unit1UI = newPopularBuildCard.findViewById(R.id.unit1Name);
            TextView unit2UI = newPopularBuildCard.findViewById(R.id.unit2Name);
            TextView unit3UI = newPopularBuildCard.findViewById(R.id.unit3Name);
            TextView unit4UI = newPopularBuildCard.findViewById(R.id.unit4Name);
            TextView unit5UI = newPopularBuildCard.findViewById(R.id.unit5Name);
            TextView unit6UI = newPopularBuildCard.findViewById(R.id.unit6Name);
            TextView unit7UI = newPopularBuildCard.findViewById(R.id.unit7Name);
            TextView unit8UI = newPopularBuildCard.findViewById(R.id.unit8Name);

            ImageView unit1ImageUI = newPopularBuildCard.findViewById(R.id.unit1Image);
            ImageView unit2ImageUI = newPopularBuildCard.findViewById(R.id.unit2Image);
            ImageView unit3ImageUI = newPopularBuildCard.findViewById(R.id.unit3Image);
            ImageView unit4ImageUI = newPopularBuildCard.findViewById(R.id.unit4Image);
            ImageView unit5ImageUI = newPopularBuildCard.findViewById(R.id.unit5Image);
            ImageView unit6ImageUI = newPopularBuildCard.findViewById(R.id.unit6Image);
            ImageView unit7ImageUI = newPopularBuildCard.findViewById(R.id.unit7Image);
            ImageView unit8ImageUI = newPopularBuildCard.findViewById(R.id.unit8Image);

            //Set up text
            buildNameUI.setText(buildName);
            unit1UI.setText(unit1);
            unit2UI.setText(unit2);
            unit3UI.setText(unit3);
            unit4UI.setText(unit4);
            unit5UI.setText(unit5);
            unit6UI.setText(unit6);
            unit7UI.setText(unit7);
            unit8UI.setText(unit8);

            //Set up images
            unit1ImageUI.setImageResource(unit1ID);
            unit2ImageUI.setImageResource(unit2ID);
            unit3ImageUI.setImageResource(unit3ID);
            unit4ImageUI.setImageResource(unit4ID);
            unit5ImageUI.setImageResource(unit5ID);
            unit6ImageUI.setImageResource(unit6ID);
            unit7ImageUI.setImageResource(unit7ID);
            unit8ImageUI.setImageResource(unit8ID);

            System.out.println(newPopularBuildCard.getId());
        });


    }

    public void ClickBack(View view) {
        MainActivity.redirectActivity(this, PopularBuilds.class);
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
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this, CommunityBuildList.class);
    }

    public void ClickCurrentCharacters(View view) {
        //Redirect to Current Character activity
        MainActivity.redirectActivity(this, CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view) {
        //Redirect to Item Builder activity
        MainActivity.redirectActivity(this, ItemBuilder.class);
    }

    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(PopularBuildInfo.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
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