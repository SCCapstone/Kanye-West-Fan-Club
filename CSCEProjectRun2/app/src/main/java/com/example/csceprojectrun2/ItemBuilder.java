package com.example.csceprojectrun2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemBuilder extends AppCompatActivity implements BasicItem_RecyclerView {
    //Initialize variable
    DrawerLayout drawerLayout;
    ArrayList<BasicItemModel> basicItemsModels = new ArrayList<>();
    BasicItem_Details details = new BasicItem_Details();
    public int pos;

    int[] basicItemImages =
            {R.drawable.bfsword_square, R.drawable.chainvest_square,
                    R.drawable.giantsbelt_square, R.drawable.needlesslylargerod_square,
                    R.drawable.negatroncloak_square, R.drawable.recurvebow_square,
                    R.drawable.sparringgloves_square, R.drawable.spatula_square,
                    R.drawable.tearofgoddess_square};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_builder);

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);

        RecyclerView recyclerView = findViewById(R.id.itemRecyclerView);
        setUpBasicItemModels();
        BasicItem_Adapter adapter = new BasicItem_Adapter(this, basicItemsModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }

    private void setUpBasicItemModels() {
        String[] basicItemNames = getResources().getStringArray(R.array.basic_item_name);

        for (int i = 0; i < basicItemNames.length; i++) {
            basicItemsModels.add(new BasicItemModel(basicItemNames[i], basicItemImages[i]));
        }
    }

    public void ClickMenu(View view) {
        //Open drawer
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickSearch(View view){
        MainActivity.searchHandler.ClickSearch(view);
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
        MainActivity.redirectActivity(this, CommunityBuilds.class);
    }

    public void ClickCurrentCharacters(View view) {
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this, CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view) {
        //Recreate the Item Builder activity
        recreate();
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

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                position = 0;
                this.pos = 0;
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_bfsword);
                break;
            case 1:
                position = 1;
                this.pos = 1;
                //Switch to Chain Vest item details.
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_chainvest);
                break;
            case 2:
                position = 2;
                this.pos = 0;
                //Switch to Giant's Belt item details.
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_giantsbelt);
                break;
            case 3:
                position = 3;
                this.pos = 3;
                //Switch to Needlessly Large Rod item details.
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_largerod);
                break;
            case 4:
                position = 4;
                this.pos = 4;
                //Switch to Negatron Cloak item details.
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_negatroncloak);
                break;
            case 5:
                position = 5;
                this.pos = 5;
                //Switch to Recurve Bow item details.
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_recurvebow);
                break;
            case 6:
                position = 6;
                this.pos = 6;
                //Switch to Sparring Gloves item details.
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_sparringgloves);
                break;
            case 7:
                position = 7;
                this.pos = 7;
                //Switch to Spatula item details.
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_spatula);
                break;
            case 8:
                position = 8;
                this.pos = 8;
                //Switch to Tear of the Goddess item details.
                //startActivity(new Intent(getApplicationContext(),BasicItem_Details.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                setContentView(R.layout.item_build_tearofthegoddess);
                break;
        }
    }
}