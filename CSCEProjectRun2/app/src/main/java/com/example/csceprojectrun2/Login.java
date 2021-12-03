package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {

    public FirebaseFirestore db;
    public Button log_in;
    public Button create_account;
    public EditText username;
    public EditText password;
    public Map<String, Object> test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        log_in = findViewById(R.id.log_in);
        create_account = findViewById(R.id.create_account);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        db = FirebaseFirestore.getInstance();
        test = new HashMap<>();
    }

    public void accountCreate(View v) {
        create_account.setOnClickListener(v1 -> switchToCreateAccount());
    }

    public void userCheck(View v) {
        docSearch();
        log_in.setOnClickListener(v1 -> {
            if(!test.isEmpty() && test.get("username").equals(username.getText().toString())) {
                if (test.get("password").equals(password.getText().toString())) {
                    switchToMain();
                } else {
                    incorrectPassword();
                }
            } else {
                incorrectPassword();
            }
        });

    }

    public void docSearch() {
        DocumentReference docRef = db.collection("login").document(username.getText().toString());
        docRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot snap = task.getResult();
                assert snap != null;
                if(snap.exists()) {
                    Log.d("Success", "Document Snapshot data: " + snap.getData());
                    test = snap.getData();
                } else {
                    Log.d("Error", "No such document");
                }
            } else {
                Log.d("Task Not Successful", "Failed", task.getException());
            }
        });
    }

    private void switchToCreateAccount() {
        Intent createSwitch = new Intent(getApplicationContext(), CreateAccount.class);
        startActivity(createSwitch);
    }

    public void switchToMain() {
        Intent mainSwitch = new Intent(getApplicationContext(), MatchFeed.class);
        startActivity(mainSwitch);
    }

    public void incorrectPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Incorrect Username or Password")
                .setPositiveButton("Retry", (dialog, id) -> dialog.dismiss()).show();
    }

}