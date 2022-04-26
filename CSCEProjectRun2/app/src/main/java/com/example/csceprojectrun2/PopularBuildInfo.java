package com.example.csceprojectrun2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PopularBuildInfo extends AppCompatActivity {
    String userId, pId, pTitle, pDescription, pAccountName, pAccountID;
    TextView mTitle, mDescription, mAccountName, mAccountID, currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_build_info);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        //Initialize views
        currentPage = findViewById(R.id.currentPage);
        mTitle = findViewById(R.id.detailTitle);
        mDescription = findViewById(R.id.detailDescription);
        mAccountName = findViewById(R.id.detailAccountName);
        mAccountID = findViewById(R.id.detailAccountID);

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
        //Set title of current page
        currentPage.setText("Popular Builds");
    }

    //Return to previous page
    public void ClickBack(View view) {
        finish();
    }
}

