package com.example.csceprojectrun2;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CharacterInfoAdapter extends FragmentStateAdapter {

    private Context context;
    int totaltabs;
    Champion champion;
    CharacterInfo characterInfo;
    //Initializing an Adapter with a Lifecycle
    CharacterInfoAdapter(Context context, int totaltabs, FragmentManager fm, Lifecycle l, Champion champion, CharacterInfo characterInfo){
        super(fm, l);
        this.context = context;
        this.totaltabs = totaltabs;
        this.champion = champion;
        this.characterInfo = characterInfo;
    }
    //Creating Fragments for Ability and Stats
    public Fragment createFragment(int p){
        switch(p){
            case 0:
                FragmentAbility fa = new FragmentAbility(champion);
                return fa;
            case 1:
                FragmentStats fs = new FragmentStats(champion, characterInfo);
                return fs;
            default:
                return null;
        }

    }

    public int getItemCount(){
        return totaltabs;
    }
}
