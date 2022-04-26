package com.example.csceprojectrun2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CommunityBuildList extends AppCompatActivity {
    DrawerLayout drawerLayout;
    List<Model> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    String userId;
    TextView tftName, currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FloatingActionButton mAddBtn;
    CommunityBuildAdapter adapter;
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
            currentPage.setText("Community Builds");

            //Click add button
            mAddBtn.setOnClickListener(view -> {
                startActivity(new Intent(CommunityBuildList.this, CommunityBuilds.class));
                finish();
            });
        }
    }

    //Display data from community builds list on Firebase
    private void displayData() {
        //set title of progress dialog
        pd.setTitle("Loading Data...");
        //show progress dialog
        pd.show();
        fStore.collection("communityBuilds").get().addOnCompleteListener(task -> {
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
            adapter = new CommunityBuildAdapter(CommunityBuildList.this, modelList);
            //set adapter to recyclerview
            mRecyclerView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            //called when there is any error while retrieving
            pd.dismiss();
            Toast.makeText(CommunityBuildList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    //Delete build from community build on Firebase
    public void deleteData(int index) {
        //set title of progress dialog
        pd.setTitle("Deleting Data...");
        //show progress dialog
        pd.show();

        //Find selected build in Firebase
        fStore.collection("communityBuilds")
                .document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(task -> {
                    //Called when task is successful
                    Toast.makeText(CommunityBuildList.this, "Deleted...", Toast.LENGTH_SHORT).show();
                    //Display updated data
                    displayData();
                })
                .addOnFailureListener(e -> {
                    //Called when a error occur
                    pd.dismiss();
                    Toast.makeText(CommunityBuildList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        //Delete in individual user's popular builds
        fStore.collection("user").document(userId).collection("popularBuilds")
                .document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(task -> {
                    //Called when task is successful
                    Toast.makeText(CommunityBuildList.this, "Deleted...", Toast.LENGTH_SHORT).show();
                    //Display updated data
                    displayData();
                })
                .addOnFailureListener(e -> {
                    //Called when a error occur
                    pd.dismiss();
                });
    }

    //Click search to go to search page
    public void ClickSearch(View view) {
        System.out.println("Clicked search from Community Builds");
        //CALL API KEY FROM FIREBASE
        DocumentReference documentReference = fStore.collection("apikey").document("key");
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve api key from Firebase
            if (value != null) {
                String currentAPIKey = value.getString("apikey");
                //pass currentAPIKey to search feed
                Intent intent = new Intent(CommunityBuildList.this, SearchFeed.class);
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
        MainActivity.redirectActivity(this, PopularBuildList.class);
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

    //Logout of app
    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(CommunityBuildList.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
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