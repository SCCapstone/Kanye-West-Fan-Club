package com.example.csceprojectrun2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Objects;

public class CharacterInfo extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TextView tftName, currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info_1);

        //Initialize views
        tabLayout = findViewById(R.id.characterinfoTabLayout);
        viewPager2 = findViewById(R.id.characterinfoViewPager);
        tftName = findViewById(R.id.TFTName);
        currentPage = findViewById(R.id.currentPage);
        //preparing an instance of Champion to be passed between states
        Bundle bundle = getIntent().getExtras();
        Champion champion = (Champion) bundle.getSerializable("champion");

        final CharacterInfoAdapter cia = new CharacterInfoAdapter(this, tabLayout.getTabCount(), getSupportFragmentManager(), getLifecycle(), champion, this);
        viewPager2.setAdapter(cia);
        currentPage.setText("Current Characters");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab t) {
                viewPager2.setCurrentItem(t.getPosition());
            }

            public void onTabUnselected(TabLayout.Tab t) {
            }

            public void onTabReselected(TabLayout.Tab t) {
            }
        });
        //formatting championName into a valid file descriptor
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

    public void PopulateAbilityInfo(Champion champion) {
        TextView characterAbilityNameUI = findViewById(R.id.characterinfoAbilityName);
        TextView characterAbilityDescriptionUI = findViewById(R.id.characterinfoAbilityDescription);

        characterAbilityNameUI.setText(champion.getAbility().getAbilityName());
        characterAbilityDescriptionUI.setText(champion.getAbility().getAbilityDesc());
    }

    public void PopulateStatsInfo(Champion champion) {
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

    public void ClickBack(View view) {
        finish();
    }
}
