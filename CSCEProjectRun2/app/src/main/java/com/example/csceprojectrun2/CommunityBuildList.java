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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommunityBuildList extends AppCompatActivity {
    DrawerLayout drawerLayout;
    List<Model> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FloatingActionButton mAddBtn;
    CustomAdapter adapter;
    TextView tftName, currentPage;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_build_feed);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAddBtn = findViewById(R.id.addBtn);
        tftName = findViewById(R.id.tftName);
        currentPage = findViewById(R.id.currentPage);

        //Set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //Initialize progress dialog
        pd = new ProgressDialog(this);

        //Show data in recyclerView
        displayData();

        //Click add button
        mAddBtn.setOnClickListener(view -> {
            startActivity(new Intent(CommunityBuildList.this, CommunityBuilds.class));
            finish();
        });

        //Display current user's tft name in navigation drawer
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve tft name and puiid from Firebase
            assert value != null;
            String TFTName = value.getString("tftName");
            tftName.setVisibility(View.VISIBLE);
            tftName.setText(TFTName);
        });
        currentPage.setText("Community Builds");
    }

    private void displayData() {
        //set title of progress dialog
        pd.setTitle("Loading Data...");
        //show progress dialog
        pd.show();

        fStore.collection("users").document(userId).collection("builds").get().addOnCompleteListener(task -> {
            modelList.clear();
            //called when data is retrieved
            pd.dismiss();
            //show data
            for (DocumentSnapshot doc : task.getResult()) {
                Model model = new Model(doc.getString("id"),
                        doc.getString("title"),
                        doc.getString("description"));
                modelList.add(model);
            }
            //adapter
            adapter = new CustomAdapter(CommunityBuildList.this, modelList);
            //set adapter to recyclerview
            mRecyclerView.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            //called when there is any error while retrieving
            pd.dismiss();
            Toast.makeText(CommunityBuildList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void deleteData(int index) {
        //set title of progress dialog
        pd.setTitle("Deleting Data...");
        //show progress dialog
        pd.show();

        //Find selected build in Firebase
        fStore.collection("users").document(userId).collection("builds")
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
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void ClickPopularBuilds(View view) {
        //Redirect to Popular Builds activity
        MainActivity.redirectActivity(this, PopularBuilds.class);
    }

    public void ClickCommunityBuilds(View view) {
        //Recreate the Community Builds activity
        recreate();
    }

    public void ClickCurrentCharacters(View view) {
        //Redirect to Current Characters activity
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