package com.example.csceprojectrun2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PopularBuildList extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView tftName, currentPage;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    List<Model> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton mAddBtn;
    PopularBuildAdapter adapter;
    ProgressDialog pd;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_build_feed);

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.TFTName);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAddBtn = findViewById(R.id.addBtn);
        currentPage = findViewById(R.id.currentPage);

        //Set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //Initialize progress dialog
        pd = new ProgressDialog(this);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            userId = currentUser.getUid(); //Do what you need to do with the id
            //Show data in recyclerView
            displayData();

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
        }
        //Set title of current page
        currentPage.setText("Popular Builds");

        //Click add button to return to community build list to pick more builds
        mAddBtn.setOnClickListener(view -> {
            startActivity(new Intent(PopularBuildList.this, CommunityBuildList.class));
            finish();
        });
    }

    //Display data from popular builds on Firebase
    private void displayData() {
        //set title of progress dialog
        pd.setTitle("Loading Data...");
        //show progress dialog
        pd.show();

        if (currentUser != null) {
            userId = currentUser.getUid(); //Do what you need to do with the id

            fStore.collection("user").document(userId).collection("popularBuilds").get().addOnCompleteListener(task -> {
                modelList.clear();
                //called when data is retrieved
                pd.dismiss();
                //show data
                for (DocumentSnapshot doc : task.getResult()) {
                    Model model = new Model(
                            doc.getString("id"),
                            doc.getString("title"),
                            doc.getString("description"),
                            doc.getString("postedBy"),
                            doc.getString("userID"));
                    modelList.add(model);
                }
                //adapter
                adapter = new PopularBuildAdapter(PopularBuildList.this, modelList);
                //set adapter to recyclerview
                mRecyclerView.setAdapter(adapter);
            }).addOnFailureListener(e -> {
                //called when there is any error while retrieving
                pd.dismiss();
                Toast.makeText(PopularBuildList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    //Remove data from popular builds on Firebase
    public void removeData(int index) {
        //set title of progress dialog
        pd.setTitle("Removing  Data...");
        //show progress dialog
        pd.show();

        //Find selected build in Firebase
        fStore.collection("user").document(userId).collection("popularBuilds")
                .document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(task -> {
                    //Called when task is successful
                    Toast.makeText(PopularBuildList.this, "Removed...", Toast.LENGTH_SHORT).show();
                    //Display updated data
                    displayData();
                })
                .addOnFailureListener(e -> {
                    //Called when a error occur
                    pd.dismiss();
                    Toast.makeText(PopularBuildList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    //Click search to go to the search page
    public void ClickSearch(View view) {
        System.out.println("Clicked search from Popular Builds");
        //CALL API KEY FROM FIREBASE
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");
                //pass currentAPIKey to search feed
                Intent intent = new Intent(PopularBuildList.this, SearchFeed.class);
                intent.putExtra("currentAPIKey", currentAPIKey);
                startActivity(intent);
            }
        });
    }

    //Click menu to open drawer
    public void ClickMenu(View view) {
        MainActivity.openDrawer(drawerLayout);
    }

    //Redirect to home activity
    public void ClickHome(View view) {
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    //Redirect to Popular Builds activity
    public void ClickPopularBuilds(View view) {
        recreate();
    }

    //Recreate the Community Builds activity
    public void ClickCommunityBuilds(View view) {
        MainActivity.redirectActivity(this, CommunityBuildList.class);
    }

    //Redirect to Current Characters activity
    public void ClickCurrentCharacters(View view) {
        MainActivity.redirectActivity(this, CurrentCharacters.class);
    }

    //Redirect to Item Builder activity
    public void ClickItemBuilder(View view) {
        MainActivity.redirectActivity(this, ItemBuilder.class);
    }

    //Logout out of app
    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(PopularBuildList.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
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
