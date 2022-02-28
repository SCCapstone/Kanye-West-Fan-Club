package com.example.csceprojectrun2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class BasicItem_Details extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_build_details);

        ItemBuilder ib = new ItemBuilder();
        if(ib.pos == 1){
            setContentView(R.layout.item_build_bfsword);
            //System.out.print("ib.pos is : " + ib.pos);
        }
        else if(ib.pos == 7){
            setContentView(R.layout.item_build_spatula);
            //System.out.print("ib.pos is : " + ib.pos);
        }
        else {
            setContentView(R.layout.item_build_chainvest);
        }
        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }



    public void SwitchDetails(int pos) {
        if (pos == 0) {
            setContentView(R.layout.activity_item_build_details);
        } else if (pos == 1) {
            setContentView(R.layout.item_build_chainvest);
        } else if (pos == 2) {
            setContentView(R.layout.item_build_giantsbelt);
        } else if (pos == 3) {
            setContentView(R.layout.item_build_largerod);
        } else if (pos == 4) {
            setContentView(R.layout.item_build_negatroncloak);
        } else if (pos == 5) {
            setContentView(R.layout.item_build_recurvebow);
        } else if (pos == 6) {
            setContentView(R.layout.item_build_sparringgloves);
        } else if (pos == 7) {
            setContentView(R.layout.item_build_spatula);
        } else if (pos == 8) {
            setContentView(R.layout.item_build_tearofthegoddess);
        } else {
            setContentView(R.layout.item_build_spatula);
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
        //Recreate the Popular Builds activity
        recreate();
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