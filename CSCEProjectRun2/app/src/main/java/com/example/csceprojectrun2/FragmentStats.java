package com.example.csceprojectrun2;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class FragmentStats extends Fragment {

    CharacterInfo characterInfo;
    Champion champion;
    public FragmentStats(Champion champion, CharacterInfo characterInfo) {
    this.champion = champion;
    this.characterInfo = characterInfo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView characterStatArmor = view.findViewById(R.id.characterinfoStatArmorValue);
        TextView characterStatDamage = view.findViewById(R.id.characterinfoStatDamageValue);
        TextView characterStatHP = view.findViewById(R.id.characterinfoStatHPValue);
        TextView characterStatMana = view.findViewById(R.id.characterinfoStatManaValue);
        TextView characterStatRange = view.findViewById(R.id.characterinfoStatRangeValue);
        TextView characterStatAttackSpeed = view.findViewById(R.id.characterinfoStatAttackSpeedValue);
        TextView characterStatCritChance = view.findViewById(R.id.characterinfoStatCritChanceValue);
        TextView characterStatCritMultiplier = view.findViewById(R.id.characterinfoStatCritMultiplierValue);
        TextView characterStatInitialMana = view.findViewById(R.id.characterinfoStatInitialManaValue);
        TextView characterStatMagicResist = view.findViewById(R.id.characterinfoStatMagicResistValue);


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

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View v = layoutInflater.inflate(R.layout.fragment_stats, viewGroup, false);
        return v;
    }


}
