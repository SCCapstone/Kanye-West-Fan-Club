package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PopularBuilds extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    ScrollView typeContainer;
    List<BuildType> typeList = null;
    TextView tftName, currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.build_type_feed);

        // Connect to the popular builds json
        try {
            InputStream input = getApplicationContext().getAssets().open("popularBuilds.json");
            typeList = readJsonStream(input);
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }

        //Assign variables
        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.tftName);
        currentPage = findViewById(R.id.currentPage);
        typeContainer = findViewById(R.id.kanye_container);
        renderTypes(typeContainer);

        //Initialize Firebase elements
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        //Display current user's tft name in navigation drawer
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (value, error) -> {
            //Retrieve tft name and puiid from Firebase
            assert value != null;
            String TFTName = value.getString("tftName");
            tftName.setVisibility(View.VISIBLE);
            tftName.setText(TFTName);
        });
        currentPage.setText("Popular Builds");
    }

    private List<BuildType> readJsonStream(InputStream input) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            return readTypeArray(reader);
        }
    }

    private List<BuildType> readTypeArray(JsonReader reader) throws IOException {
        // Create a list of types
        List<BuildType> types = new ArrayList<>();
        // Read type and add it to the list
        reader.beginArray();
        while (reader.hasNext()) {
            types.add(readTypes(reader));
        }
        reader.endArray();
        return types;
    }

    private BuildType readTypes(JsonReader reader) throws IOException {
        String typeName = "";
        List<PopularBuild> popularBuilds = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String type = reader.nextName();
            if (type.equals("type")) {
                typeName = reader.nextString();
            } else if (type.equals("popularBuilds")) {
                popularBuilds = readPopularBuildArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new BuildType(typeName, popularBuilds);
    }

    private List<PopularBuild> readPopularBuildArray(JsonReader reader) throws IOException {
        // Create a list of popular builds
        List<PopularBuild> popularBuilds = new ArrayList<>();
        // Read popular build and add it to the list
        reader.beginArray();
        while (reader.hasNext()) {
            popularBuilds.add(readDetails(reader));
        }
        reader.endArray();
        return popularBuilds;
    }

    private PopularBuild readDetails(JsonReader reader) throws IOException {
        String buildName = "";
        String unit1 = "";
        String unit2 = "";
        String unit3 = "";
        String unit4 = "";
        String unit5 = "";
        String unit6 = "";
        String unit7 = "";
        String unit8 = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("buildName") && reader.peek() != JsonToken.NULL) {
                buildName = reader.nextString();
            } else if (name.equals("unit1") && reader.peek() != JsonToken.NULL) {
                unit1 = reader.nextString();
            } else if (name.equals("unit2") && reader.peek() != JsonToken.NULL) {
                unit2 = reader.nextString();
            } else if (name.equals("unit3") && reader.peek() != JsonToken.NULL) {
                unit3 = reader.nextString();
            } else if (name.equals("unit4") && reader.peek() != JsonToken.NULL) {
                unit4 = reader.nextString();
            } else if (name.equals("unit5") && reader.peek() != JsonToken.NULL) {
                unit5 = reader.nextString();
            } else if (name.equals("unit6") && reader.peek() != JsonToken.NULL) {
                unit6 = reader.nextString();
            } else if (name.equals("unit7") && reader.peek() != JsonToken.NULL) {
                unit7 = reader.nextString();
            } else if (name.equals("unit8") && reader.peek() != JsonToken.NULL) {
                unit8 = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new PopularBuild(buildName, unit1, unit2, unit3, unit4, unit5, unit6, unit7, unit8);
    }

    private void renderTypes(ScrollView typeContainer) {
        // clear any existing type tiles
        LinearLayout linearLayout = typeContainer.findViewById(R.id.kanye_container_linear_layout);
        linearLayout.removeAllViews();

        // spawn thread to create instances of type card
        new Thread(() -> {
            // populate item feed
            for (int i = 0; i < typeList.size(); i++) {
                createTypeCard(i, typeList.get(i));
            }
        }).start();
    }

    private void createTypeCard(int cardPosition, BuildType buildType) {
        LinearLayout linearLayout = typeContainer.findViewById(R.id.kanye_container_linear_layout);
        String type = buildType.getName();

        runOnUiThread(() -> {
            // build new type tile
            LayoutInflater inflater = getLayoutInflater();
            // create the card UI element
            inflater.inflate(R.layout.build_type_card, linearLayout, true);
            // get and update new card
            View newTypeCard = linearLayout.getChildAt(cardPosition);
            TextView typeUI = newTypeCard.findViewById(R.id.popularName);
            typeUI.setText(type);
            System.out.println(newTypeCard.getId());
        });
    }

    public void ClickCard(View view) {
        BuildType type = null;
        TextView typeNameUI = view.findViewById(R.id.popularName);
        for (int i = 0; i < typeList.size(); i++) {
            if (typeList.get(i).getName() == typeNameUI.getText()) {
                type = typeList.get(i);
            }
        }
        Intent intent = new Intent(this, PopularBuildInfo.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", type);
        this.startActivity(intent);
    }

    public void ClickMenu(View view) {
        //Open drawer
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        //Redirect to home activity
        MainActivity.redirectActivity(this, MatchFeed.class);
    }

    public void ClickPopularBuilds(View view) {
        //Redirect to Popular Builds activity
        recreate();
    }

    public void ClickCommunityBuilds(View view) {
        //Recreate the Community Builds activity
        MainActivity.redirectActivity(this, CommunityBuilds.class);
    }

    public void ClickCurrentCharacters(View view) {
        //Redirect to Current Characters activity
        MainActivity.redirectActivity(this, CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view) {
        //Redirect to Item Builder activity
        MainActivity.redirectActivity(this, ItemBuilder.class);
    }

    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(PopularBuilds.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}
