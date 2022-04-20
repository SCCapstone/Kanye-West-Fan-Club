package com.example.csceprojectrun2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public static SearchHandler searchHandler = new SearchHandler();
    public static final String TAG = "TAG";
    DrawerLayout drawerLayout;
    TextView tftName, currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity( new Intent(this, MatchFeed.class));

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.TFTName);
        currentPage = findViewById(R.id.currentPage);

        // temp functionality to go to match feed, to make merging everything in easier
        Intent intent = new Intent(this, MatchFeed.class);
        Button tempOpenFeed = drawerLayout.findViewById(R.id.tempOpenMatchFeed);
        tempOpenFeed.setOnClickListener(v -> startActivity(intent));
    }

    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        //Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        //Recreate the Home activity
        recreate();
    }

    public void ClickSearch(View view) {
        System.out.println("Clicked search from MainActivity");
        MainActivity.searchHandler.ClickSearch(view);
    }

    public void ClickPopularBuilds(View view) {
        //Redirect to Popular Builds activity
        redirectActivity(this, PopularBuildList.class);
    }

    public void ClickCommunityBuilds(View view) {
        //Redirect to Community Builds activity
        redirectActivity(this, CommunityBuildList.class);
    }

    public void ClickCurrentCharacters(View view) {
        //Redirect to Current Characters activity
        redirectActivity(this, CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view) {
        //Redirect to Item Builder activity
        redirectActivity(this, ItemBuilder.class);
    }

    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(MainActivity.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
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