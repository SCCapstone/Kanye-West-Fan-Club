package com.example.csceprojectrun2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PopularBuilds extends AppCompatActivity implements PopularBuild_RecyclerView {
    //Initialize variable
    DrawerLayout drawerLayout;
    ArrayList<PopularBuildModel> buildModels = new ArrayList<>();
    public int pos;

    int[] buildImages = {R.drawable.ionicspark_square, R.drawable.ionicspark_square, R.drawable.ionicspark_square};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_builds);

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);

        RecyclerView recyclerView = findViewById(R.id.itemRecyclerView);
        setUpPopularBuildModels();
        PopularBuild_Adapter adapter = new PopularBuild_Adapter(this, buildModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }

    private void setUpPopularBuildModels() {
        String[] buildNames = getResources().getStringArray(R.array.popular_build_name);
        for (int i = 0; i < buildNames.length; i++) {
            buildModels.add(new PopularBuildModel(buildNames[i], buildImages[i]));
        }
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
                setContentView(R.layout.popular_build1);
                break;
            case 1:
                position = 1;
                this.pos = 1;
                //Switch to Chain Vest item details.
                setContentView(R.layout.popular_build2);
                break;
            case 2:
                position = 2;
                this.pos = 0;
                //Switch to Giant's Belt item details.
                setContentView(R.layout.popular_build3);
                break;
        }
    }
}