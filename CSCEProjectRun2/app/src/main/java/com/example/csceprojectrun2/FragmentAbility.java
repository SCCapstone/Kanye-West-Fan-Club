package com.example.csceprojectrun2;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentAbility extends Fragment {
    Champion champion;

    //Fragment ability constructor
    public FragmentAbility(Champion champion) {
        this.champion = champion;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize views
        TextView characterAbilityNameUI = view.findViewById(R.id.characterinfoAbilityName);
        TextView characterAbilityDescriptionUI = view.findViewById(R.id.characterinfoAbilityDescription);

        //Get ability name and description from a champion to display
        characterAbilityNameUI.setText(champion.getAbility().getAbilityName());
        characterAbilityDescriptionUI.setText(champion.getAbility().getAbilityDesc());
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_ability, viewGroup, false);
    }
}