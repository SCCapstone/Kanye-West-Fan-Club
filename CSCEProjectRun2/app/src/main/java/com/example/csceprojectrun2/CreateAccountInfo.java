package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountInfo extends AppCompatActivity {
    Button mContinueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_info);

        mContinueBtn = findViewById(R.id.continueBtn);
        mContinueBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CreateAccount.class)));
    }
}
