package com.example.csceprojectrun2;

import java.io.Serializable;

public class Ability implements Serializable {
    String abilityName;
    String abilityDesc;

    Ability(String abilityname, String abilitydesc) {
        this.abilityName = abilityname;
        this.abilityDesc = abilitydesc;
    }

    public String getAbilityName() {return abilityName;}
    public String getAbilityDesc() {return abilityDesc;}
}
