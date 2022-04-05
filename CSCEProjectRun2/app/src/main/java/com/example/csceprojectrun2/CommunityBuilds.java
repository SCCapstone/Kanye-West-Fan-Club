package com.example.csceprojectrun2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CommunityBuilds extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView tftName, currentPage;
    EditText mTitleEt, mDescriptionEt;
    Button mSaveBtn, mShowAllBtn;
    String pId, pTitle, pDescription, userId;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_builds);

        //action bar and title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Add Data");

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.tftName);
        currentPage = findViewById(R.id.currentPage);
        mTitleEt = findViewById(R.id.titleEt);
        mDescriptionEt = findViewById(R.id.descriptionEt);
        mSaveBtn = findViewById(R.id.saveBtn);
        mShowAllBtn = findViewById(R.id.showAllBtn);

        //Initialize progress dialog
        progressDialog = new ProgressDialog(this);

        //Get data from Adapter or CommunityBuildInfo
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Update data
            actionBar.setTitle("Update Data");
            mSaveBtn.setText("Update");
            pId = bundle.getString("pId");
            pTitle = bundle.getString("pTitle");
            pDescription = bundle.getString("pDescription");
            //set data
            mTitleEt.setText(pTitle);
            mDescriptionEt.setText(pDescription);
        } else {
            //Add data
            actionBar.setTitle("Add Data");
            mSaveBtn.setText("Save");
        }

        //Display current user's tft name in navigation drawer
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve tft name from Firebase
            assert value != null;
            tftName.setText(value.getString("tftName"));
            tftName.setVisibility(View.VISIBLE);

            //click button to upload data
            mSaveBtn.setOnClickListener(view -> {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle != null) {
                    //UPDATING DATA
                    //input data
                    String id = pId;
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();
                    //Call function to update data
                    updateData(id, title, description);
                    startActivity(new Intent(CommunityBuilds.this, CommunityBuildList.class));
                    finish();
                } else {
                    //ADDING NEW DATA
                    //input data
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();
                    //Call function to upload data
                    uploadData(title, description);
                    startActivity(new Intent(CommunityBuilds.this, CommunityBuildList.class));
                    finish();
                }
            });

            //Click button to display all builds in collection
            mShowAllBtn.setOnClickListener(view -> {
                startActivity(new Intent(CommunityBuilds.this, CommunityBuildList.class));
                finish();
            });
        });
        currentPage.setText("Community Builds");
    }

    private void updateData(String id, String title, String description) {
        ///set title of progress bar
        progressDialog.setTitle("Updating Data...");
        //show progress bar when user click save button
        progressDialog.show();

        fStore.collection("users").document(userId).collection("builds").document(id)
                .update("title", title, "description", description)
                .addOnCompleteListener(task -> {
                    //called when updated successfully
                    progressDialog.dismiss();
                    Toast.makeText(CommunityBuilds.this, "Updated...", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    //called when there is any error
                    progressDialog.dismiss();
                    Toast.makeText(CommunityBuilds.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    //Upload data to Firebase for storage
    private void uploadData(String title, String description) {
        ///Set title of progress bar
        progressDialog.setTitle("Adding Data to Firestore");
        //Show the progress bar when user click the save button
        progressDialog.show();
        //Assign random ID to each build stored
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("description", description);

        //add this data
        fStore.collection("users").document(userId).collection("builds").document(id).set(doc).addOnCompleteListener(task -> {
            //Data is added successfully
            progressDialog.dismiss();
            Toast.makeText(CommunityBuilds.this, "Uploaded...", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            //An error occurred when uploading data
            progressDialog.dismiss();
            Toast.makeText(CommunityBuilds.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void ClickBack(View view) {
        MainActivity.redirectActivity(this, CommunityBuildList.class);
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
        MainActivity.redirectActivity(this, CommunityBuildList.class);
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
        Toast.makeText(CommunityBuilds.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
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