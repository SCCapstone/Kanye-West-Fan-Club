package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashPage extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1000;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        new Handler().postDelayed(() -> {
            if (user != null) {
                // User is signed in
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            } else {
                // No user is signed in
                startActivity(new Intent(getApplicationContext(), com.example.csceprojectrun2.Login.class));
            }
            finish();
        }, SPLASH_TIME_OUT);
    }
}
