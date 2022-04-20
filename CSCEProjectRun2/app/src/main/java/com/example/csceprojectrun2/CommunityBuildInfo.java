package com.example.csceprojectrun2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommunityBuildInfo extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String userId, pId, pTitle, pDescription, pAccountName, pAccountID;
    Button mSaveBtn;
    TextView mTitle, mDescription, mAccountName, mAccountID;
    ProgressDialog progressDialog;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_build_info);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        mSaveBtn = findViewById(R.id.saveBtn);
        mTitle = findViewById(R.id.detailTitle);
        mDescription = findViewById(R.id.detailDescription);
        mAccountName = findViewById(R.id.detailAccountName);
        mAccountID = findViewById(R.id.detailAccountID);

        //Initialize progress dialog
        progressDialog = new ProgressDialog(this);

        //Get data from Adapter
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pId = bundle.getString("pId");
            pTitle = bundle.getString("pTitle");
            pDescription = bundle.getString("pDescription");
            pAccountName = bundle.getString("pAccountName");
            pAccountID = bundle.getString("pAccountID");
            //set data
            mTitle.setText(pTitle);
            mDescription.setText(pDescription);
            mAccountName.setText(pAccountName);
            mAccountID.setText(pAccountID);
        }

        //Click on SAVE and save build to user's Popular Builds
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save/Upload the card data to a user's popular builds.
                uploadData(pId, pTitle, pDescription, pAccountName, pAccountID);
                //Go to Popular Builds
                startActivity(new Intent(CommunityBuildInfo.this, CommunityBuildList.class));
                finish();
            }
        });
    }

    //Upload data to Firebase for storage
    private void uploadData(String id, String title, String description, String accountName, String accountID) {
        ///Set title of progress bar
        progressDialog.setTitle("Adding Data to Firestore");
        //Show the progress bar when user click the save button
        progressDialog.show();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("description", description);
        doc.put("postedBy", accountName);
        doc.put("userID", accountID);
        doc.put("timeStamp", FieldValue.serverTimestamp());

        fStore.collection("user").document(userId).collection("popularBuilds").document(id).set(doc).addOnCompleteListener(task -> {
            //Data is added successfully
            progressDialog.dismiss();
            Toast.makeText(CommunityBuildInfo.this, "Saved to Popular Builds", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            //An error occurred when uploading data
            progressDialog.dismiss();
            Toast.makeText(CommunityBuildInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    public void ClickBack(View view) {
        MainActivity.redirectActivity(this, CommunityBuildList.class);
    }
}