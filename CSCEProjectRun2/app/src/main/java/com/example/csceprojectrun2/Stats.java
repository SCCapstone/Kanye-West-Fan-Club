package com.example.csceprojectrun2;

import java.io.Serializable;

public class Stats implements Serializable {
    int armor;
    double attackSpeed;
    float critChance;
    double critMultiplier;
    int damage;
    int hp;
    int initialMana;
    int magicResist;
    int mana;
    int range;

    //Stats constructor
    Stats(int armor, double attackSpeed, float critChance, double critMultiplier, int damage, int hp, int initialMana, int magicResist, int mana, int range) {
        this.armor = armor;
        this.attackSpeed = attackSpeed;
        this.critChance = critChance;
        this.critMultiplier = critMultiplier;
        this.damage = damage;
        this.hp = hp;
        this.initialMana = initialMana;
        this.magicResist = magicResist;
        this.mana = mana;
        this.range = range;
    }

    //Get armor from stats
    public int getArmor() {
        return armor;
    }

    //Get attack speed from stats
    public double getAttackSpeed() {
        return attackSpeed;
    }

    //Get critical chance from stats
    public float getCritChance() {
        return critChance;
    }

    //Get critical multiplier from stats
    public double getCritMultiplier() {
        return critMultiplier;
    }

    //Get damage from stats
    public int getDamage() {
        return damage;
    }

    //Get hp from stats
    public int getHp() {
        return hp;
    }

    //Get initial mana from stats
    public int getInitialMana() {
        return initialMana;
    }

    //Get magic resist from stats
    public int getMagicResist() {
        return magicResist;
    }

    //Get mana from stats
    public int getMana() {
        return mana;
    }

    //Get range from stats
    public int getRange() {
        return range;
    }
}
