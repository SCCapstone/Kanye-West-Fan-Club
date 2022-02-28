package com.example.csceprojectrun2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity {
    public static SearchHandler searchHandler = new SearchHandler();
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        // temp functionality to go to match feed, to make merging everything in easier
        Intent intent = new Intent(this, MatchFeed.class);
        Button tempOpenFeed = drawerLayout.findViewById(R.id.tempOpenMatchFeed);
        tempOpenFeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);

    }

    public static void openDrawer(DrawerLayout drawerLayout){
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        //Close drawer layout
        //Check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        //Recreate the Home activity
        recreate();
    }

    public void ClickSearch(View view){
        System.out.println("Clicked search from MainActivity");
        MainActivity.searchHandler.ClickSearch(view);
    }

    public void ClickPopularBuilds(View view){
        //Redirect to Popular Builds activity
        redirectActivity(this,PopularBuilds.class);
    }

    public void ClickCommunityBuilds(View view){
        //Redirect to Community Builds activity
        redirectActivity(this,CommunityBuilds.class);
    }

    public void ClickCurrentCharacters(View view){
        //Redirect to Current Characters activity
        redirectActivity(this,CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view){
        //Redirect to Item Builder activity
        redirectActivity(this,ItemBuilder.class);
    }

    public void ClickLogout(View view){
        //Logout and close the app
        logout(this);
    }

    public static void logout(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set title
        builder.setTitle("Logout");
        //Set message
        builder.setMessage("Are you sure you want to logout ?");
        //Click Yes Button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Complete activity
                activity.finishAffinity();
                //Exit app
                redirectActivity(activity, Login.class);
            }
        });
        //Click No Button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss dialog
                dialog.dismiss();
            }
        });
        //Show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity,aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}