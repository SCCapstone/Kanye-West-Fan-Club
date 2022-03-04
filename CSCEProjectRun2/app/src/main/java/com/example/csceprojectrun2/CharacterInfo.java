package com.example.csceprojectrun2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.DecimalFormat;

public class CharacterInfo extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);

        drawerLayout = findViewById(R.id.drawer_layout);

        Bundle bundle = getIntent().getExtras();

        Champion champion = (Champion)bundle.getSerializable("champion");

        PopulateInfo(champion);
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
        MainActivity.redirectActivity(this,PopularBuilds.class);
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

    public void ClickLogout(View view){
        //Logout and close app
        MainActivity.logout(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}