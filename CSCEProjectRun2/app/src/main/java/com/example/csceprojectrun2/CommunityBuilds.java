package com.example.csceprojectrun2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CommunityBuilds extends AppCompatActivity {
    TextView currentPage;
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
        DocumentReference documentReference = fStore.collection("user").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve tft name from Firebase
            assert value != null;
            String loggedInUser = value.getString("tftName");

            //click button to upload data
            mSaveBtn.setOnClickListener(view -> {
                if (bundle != null) {
                    //UPDATING DATA
                    //input data
                    String id = pId;
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();

                    //Display errors when title or description is empty.
                    if (TextUtils.isEmpty(title)) {
                        mTitleEt.setError("Title is Required.");
                        return;
                    }
                    if (TextUtils.isEmpty(description)) {
                        mDescriptionEt.setError("Description is Required.");
                        return;
                    }

                    //Call function to update data
                    updateData(id, title, description);
                    startActivity(new Intent(CommunityBuilds.this, CommunityBuildList.class));
                    finish();
                } else {
                    //ADDING NEW DATA
                    //input data
                    String title = mTitleEt.getText().toString().trim();
                    String description = mDescriptionEt.getText().toString().trim();

                    //Display errors when title or description is empty.
                    if (TextUtils.isEmpty(title)) {
                        mTitleEt.setError("Title is Required.");
                        return;
                    }
                    if (TextUtils.isEmpty(description)) {
                        mDescriptionEt.setError("Description is Required.");
                        return;
                    }

                    //Call function to upload data
                    uploadData(title, description, loggedInUser);
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

        //Update in community builds
        fStore.collection("communityBuilds").document(id)
                .update("title", title, "description", description, "timeStamp", FieldValue.serverTimestamp())
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

        //Update in logged in user's popular builds
        fStore.collection("user").document(userId).collection("popularBuilds").document(id)
                .update("title", title, "description", description, "timeStamp", FieldValue.serverTimestamp())
                .addOnCompleteListener(task -> {
                    //called when updated successfully
                    progressDialog.dismiss();
                    Toast.makeText(CommunityBuilds.this, "Updated...", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //called when there is any error
                    progressDialog.dismiss();
                });


        //Update in other in user's popular builds
        fStore.collection("user").document().collection("popularBuilds").document(id)
                .update("title", title, "description", description, "timeStamp", FieldValue.serverTimestamp())
                .addOnCompleteListener(task -> {
                    //called when updated successfully
                    progressDialog.dismiss();
                    Toast.makeText(CommunityBuilds.this, "Updated...", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //called when there is any error
                    progressDialog.dismiss();
                });

    }

    //Upload data to Firebase for storage
    private void uploadData(String title, String description, String loggedInUser) {
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
        doc.put("postedBy", loggedInUser);
        doc.put("userID", userId);
        doc.put("timeStamp", FieldValue.serverTimestamp());


        //add this data
        fStore.collection("communityBuilds").document(id).set(doc).addOnCompleteListener(task -> {
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
}