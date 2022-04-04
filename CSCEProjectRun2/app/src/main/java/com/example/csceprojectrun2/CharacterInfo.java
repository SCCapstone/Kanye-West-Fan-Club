package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Objects;

public class CharacterInfo extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView tftName, currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);

        drawerLayout = findViewById(R.id.drawer_layout);
        tftName = findViewById(R.id.tftName);
        currentPage = findViewById(R.id.currentPage);


        Bundle bundle = getIntent().getExtras();

        Champion champion = (Champion)bundle.getSerializable("champion");

        PopulateInfo(champion);

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
        currentPage.setText("Current Characters");
    }

    public void PopulateInfo(Champion champion){

        String championImage = champion.getName().toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        int championID = this.getResources().getIdentifier(championImage, "drawable", this.getPackageName());
        //LinearLayout linearLayout = characterContainer.findViewById(R.id.character_container_linear_layout);

        TextView characterNameUI = findViewById(R.id.characterinfoName);
        TextView characterCostValueUI = findViewById(R.id.characterinfoCostValue);
        TextView characterAbilityNameUI = findViewById(R.id.characterinfoAbilityName);
        TextView characterAbilityDescriptionUI = findViewById(R.id.characterinfoAbilityDescription);
        TextView characterStatArmor = findViewById(R.id.characterinfoStatArmorValue);
        TextView characterStatDamage = findViewById(R.id.characterinfoStatDamageValue);
        TextView characterStatHP = findViewById(R.id.characterinfoStatHPValue);
        TextView characterStatMana = findViewById(R.id.characterinfoStatManaValue);
        TextView characterStatRange = findViewById(R.id.characterinfoStatRangeValue);
        TextView characterStatAttackSpeed = findViewById(R.id.characterinfoStatAttackSpeedValue);
        TextView characterStatCritChance = findViewById(R.id.characterinfoStatCritChanceValue);
        TextView characterStatCritMultiplier = findViewById(R.id.characterinfoStatCritMultiplierValue);
        TextView characterStatInitialMana = findViewById(R.id.characterinfoStatInitialManaValue);
        TextView characterStatMagicResist = findViewById(R.id.characterinfoStatMagicResistValue);
        ImageView characterImageUI = findViewById(R.id.characterinfoImage);

        DecimalFormat df = new DecimalFormat("0.00");

        characterNameUI.setText(champion.getName());
        characterCostValueUI.setText(Integer.toString(champion.getCost()));
        characterAbilityNameUI.setText(champion.getAbility().getAbilityName());
        characterAbilityDescriptionUI.setText(champion.getAbility().getAbilityDesc());
        characterStatArmor.setText(Integer.toString(champion.getStats().getArmor()));
        characterStatDamage.setText(Integer.toString(champion.getStats().getDamage()));
        characterStatHP.setText(Integer.toString(champion.getStats().getHp()));
        characterStatMana.setText(Integer.toString(champion.getStats().getMana()));
        characterStatRange.setText(Integer.toString(champion.getStats().getRange()));
        characterStatAttackSpeed.setText(df.format(champion.getStats().getAttackSpeed()));
        characterStatCritChance.setText(Float.toString(champion.getStats().getCritChance()));
        characterStatCritMultiplier.setText(df.format(champion.getStats().getAttackSpeed()));
        characterStatInitialMana.setText(Integer.toString(champion.getStats().getInitialMana()));
        characterStatMagicResist.setText(Integer.toString(champion.getStats().getMagicResist()));
        characterImageUI.setImageResource(championID);
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
        MainActivity.redirectActivity(this,MatchFeed.class);
    }

    public void ClickPopularBuilds(View view){
        //Redirect to Popular Builds activity
        MainActivity.redirectActivity(this, PopularBuilds.class);
    }

    public void ClickCommunityBuilds(View view) {
        //Redirect to Community Builds activity
        MainActivity.redirectActivity(this,CommunityBuilds.class);
    }

    public void ClickCurrentCharacters(View view) {
        //Redirect to Current Character activity
        MainActivity.redirectActivity(this,CurrentCharacters.class);
    }

    public void ClickItemBuilder(View view) {
        //Redirect to Item Builder activity
        MainActivity.redirectActivity(this,ItemBuilder.class);
    }

    public void ClickLogout(View view) {
        //Signs the user out of account
        FirebaseAuth.getInstance().signOut();
        //Returns to Login screen
        Toast.makeText(CharacterInfo.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}
