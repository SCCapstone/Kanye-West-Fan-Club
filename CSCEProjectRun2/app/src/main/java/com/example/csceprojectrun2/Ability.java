package com.example.csceprojectrun2;

import java.io.Serializable;

public class Ability implements Serializable {
    String abilityName;
    String abilityDesc;

    //Constructor for ability
    Ability(String abilityName, String abilityDesc) {
        this.abilityName = abilityName;
        this.abilityDesc = abilityDesc;
    }

    //Get a character's ability name
    public String getAbilityName() {
        return abilityName;
    }

    //Get a character's ability description
    public String getAbilityDesc() {
        return abilityDesc;
    }
}
