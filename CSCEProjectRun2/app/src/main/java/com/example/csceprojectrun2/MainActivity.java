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


public class MainActivity extends AppCompatActivity {
    public static SearchHandler searchHandler = new SearchHandler();
    public static final String TAG = "TAG";
    DrawerLayout drawerLayout;
    TextView tftName, currentPage;
    FirebaseAuth fAuth;
    String userId;
    FirebaseFirestore fStore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, MatchFeed.class));

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.TFTName);
        currentPage = findViewById(R.id.currentPage);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        // temp functionality to go to match feed, to make merging everything in easier
        Intent intent = new Intent(this, MatchFeed.class);
        Button tempOpenFeed = drawerLayout.findViewById(R.id.tempOpenMatchFeed);
        tempOpenFeed.setOnClickListener(v -> startActivity(intent));

        if (currentUser != null) {
            userId = currentUser.getUid();
            //Display current user's tft name in navigation drawer
            DocumentReference documentReference = fStore.collection("user").document(userId);
            documentReference.addSnapshotListener(this, (value, error) -> {
                //Retrieve tft name and puuid from Firebase
                if (value != null) {
                    String TFTName = value.getString("tftName");
                    tftName.setVisibility(View.VISIBLE);
                    tftName.setText(TFTName);
                }
            });
            currentPage.setText("Home");
        }
    }

    //Click menu to open drawer
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    //Open drawer layout
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    //Close drawer layout
    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //Redirect to match feed
    public void ClickHome(View view) {
        //Recreate the Home activity
        redirectActivity(this, MatchFeed.class);
    }

    //Click search to go to the search page
    public void ClickSearch(View view) {
        System.out.println("Clicked search from Main Activity");
        //CALL API KEY FROM FIREBASE
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");
                //pass currentAPIKey to search feed
                Intent intent = new Intent(MainActivity.this, SearchFeed.class);
                intent.putExtra("currentAPIKey", currentAPIKey);
                startActivity(intent);
            }
        });
    }

    //Redirect to Popular Builds activity
    public void ClickPopularBuilds(View view) {
        redirectActivity(this, PopularBuildList.class);
    }

    //Redirect to Community Builds activity
    public void ClickCommunityBuilds(View view) {
        redirectActivity(this, CommunityBuildList.class);
    }

    //Redirect to Current Characters activity
    public void ClickCurrentCharacters(View view) {
        redirectActivity(this, CurrentCharacters.class);
    }

    //Redirect to Item Builder activity
    public void ClickItemBuilder(View view) {
        redirectActivity(this, ItemBuilder.class);
    }

    //Logout of app
    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(MainActivity.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    //Redirect from one activity to another
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