package com.example.csceprojectrun2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PopularBuildInfo extends AppCompatActivity {
    DrawerLayout drawerLayout;
    String userId, pId, pTitle, pDescription, pAccountName, pAccountID;
    TextView mTitle, mDescription, mAccountName, mAccountID;
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
        drawerLayout = findViewById(R.id.drawer_layout);
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
    }

    public void ClickBack(View view) {
        MainActivity.redirectActivity(this, PopularBuildList.class);
    }
}

