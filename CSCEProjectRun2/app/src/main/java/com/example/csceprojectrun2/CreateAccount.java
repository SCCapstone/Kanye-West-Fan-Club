package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccount extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mEmail, mPassword, mTFTName, mPUUID;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mEmail = findViewById(R.id.create_account_email);
        mPassword = findViewById(R.id.create_account_password);
        mTFTName = findViewById(R.id.create_account_tft);
        mPUUID = findViewById(R.id.create_account_puuid);
        mRegisterBtn = findViewById(R.id.createAccountBtn);
        mLoginBtn = findViewById(R.id.create_account_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.create_account_progressBar);

        //Check if user is already logged in
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(view -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String tftName = mTFTName.getText().toString().trim();
            String puuid = mPUUID.getText().toString().trim();

            //Display errors when email or password are empty, or password is too short
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is Required.");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is Required.");
                return;
            }
            if (password.length() < 6) {
                mPassword.setError("Password Must Be At Least 6 Characters.");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            //Register the user in firebase
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                //Check if registration is successful or not
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccount.this, "User Created and Registered.", Toast.LENGTH_SHORT).show();

                    //Store user profile data to Firestore
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);

                    Map<String, Object> user = new HashMap<>();
                    user.put("tftName", tftName);
                    user.put("puiid", puuid);

                    documentReference.set(user).addOnSuccessListener((OnSuccessListener) (aVoid) -> Log.d(TAG, "onSuccess: " + " User Profile is created for " + userID)).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e));
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(CreateAccount.this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
        mLoginBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }
}
