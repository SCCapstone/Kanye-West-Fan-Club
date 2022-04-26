package com.example.csceprojectrun2;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SearchHandler extends AppCompatActivity {
    //Click on search
    public void ClickSearch(View view) {
        System.out.println("Clicked Search in SearchHandler");
        View currentView = view;

        System.out.println("CURRENT VIEW:!!!!!!!!!!!!!!!!!!");
        System.out.println(currentView.toString());
        System.out.println(currentView.getId());

        System.out.println("testview2:");
        View testView2 = findViewById(R.id.MainTopBar);
        System.out.println(testView2);

        System.out.println("testview2");

        View test = findViewById(R.id.MainTopBarParentHeader);
        System.out.println(test);
        System.out.println(testView2);
    }
}
