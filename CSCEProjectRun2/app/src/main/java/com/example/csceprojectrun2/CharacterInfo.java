package com.example.csceprojectrun2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Objects;

public class CharacterInfo extends AppCompatActivity {
    DrawerLayout drawerLayout;

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    TextView tftName, currentPage;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info_1);

        drawerLayout = findViewById(R.id.drawer_layout);

        tabLayout = findViewById(R.id.characterinfoTabLayout);
        viewPager2 = findViewById(R.id.characterinfoViewPager);

        tftName = findViewById(R.id.tftName);
        currentPage = findViewById(R.id.currentPage);



        Bundle bundle = getIntent().getExtras();

        Champion champion = (Champion)bundle.getSerializable("champion");


        final CharacterInfoAdapter cia = new CharacterInfoAdapter(this, tabLayout.getTabCount(), getSupportFragmentManager(), getLifecycle(), champion, this);

        viewPager2.setAdapter(cia);

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


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            public void onTabSelected(TabLayout.Tab t){
            viewPager2.setCurrentItem(t.getPosition());

        }
            public void onTabUnselected(TabLayout.Tab t){}
            public void onTabReselected(TabLayout.Tab t){}
        });

        String championImage = champion.getName().toLowerCase().replace(".", "").replace(" ", "").replace("'", "") + "_square";
        int championID = this.getResources().getIdentifier(championImage, "drawable", this.getPackageName());
        //LinearLayout linearLayout = characterContainer.findViewById(R.id.character_container_linear_layout);
        TextView characterNameUI = findViewById(R.id.characterinfoName);
        TextView characterCostValueUI = findViewById(R.id.characterinfoCostValue);
        ImageView characterImageUI = findViewById(R.id.characterinfoImage);

        characterNameUI.setText(champion.getName());
        characterCostValueUI.setText(Integer.toString(champion.getCost()));
        characterImageUI.setImageResource(championID);

    }
    public void PopulateAbilityInfo(Champion champion){
        TextView characterAbilityNameUI = findViewById(R.id.characterinfoAbilityName);
        TextView characterAbilityDescriptionUI = findViewById(R.id.characterinfoAbilityDescription);

        characterAbilityNameUI.setText(champion.getAbility().getAbilityName());
        characterAbilityDescriptionUI.setText(champion.getAbility().getAbilityDesc());
    }

    public void PopulateStatsInfo(Champion champion){





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


        DecimalFormat df = new DecimalFormat("0.00");



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
