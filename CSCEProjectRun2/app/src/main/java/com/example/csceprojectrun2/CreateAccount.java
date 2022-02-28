package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    FirebaseFirestore db;
    EditText username;
    EditText password;
    EditText tftname;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        username = findViewById(R.id.user);
        password = findViewById(R.id.pass);
        tftname = findViewById(R.id.tft);
        create = findViewById(R.id.CreateAccount);
        db = FirebaseFirestore.getInstance();
    }

    public void accountMaker(View v) {
        checks();
        switchToMain();
    }

    public void blankFields() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Some Fields are not Filled Out")
                .setPositiveButton("Retry", (dialog, id) -> dialog.dismiss());
    }

    public void switchToMain() {
        Intent mainSwitch = new Intent(getApplicationContext(), MatchFeed.class);
        startActivity(mainSwitch);
    }

    public void checks() {
        if (username.getText().toString().equals("Username") || username.getText().toString().equals("") ||
                password.getText().toString().equals("Password") || password.getText().toString().equals("") ||
                tftname.getText().toString().equals("TFT Name") || password.getText().toString().equals("")) {
            blankFields();
        } else {
            Map<String, Object> user = new HashMap<>();
            user.put("username", username.getText().toString());
            user.put("password", password.getText().toString());
            user.put("tftname", tftname.getText().toString());

            db.collection("login").document(username.getText().toString()).set(user)
                    .addOnSuccessListener(unused -> Log.d("Success", "DocumentSnapshot written successfully"))
                    .addOnFailureListener(e -> Log.w("Failure", "Error writing document", e));
        }
    }
}