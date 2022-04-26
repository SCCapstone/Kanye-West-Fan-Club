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

    //Initializes statistical data for use in Champion
    public Stats(int armor,double attackSpeed,float critChance,double critMultiplier,int damage,int hp,int initialMana,int magicResist,int mana,int range) {
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

    public int getArmor() {return armor;}
    public double getAttackSpeed() {return attackSpeed;}
    public float getCritChance() {return critChance;}
    public double getCritMultiplier() {return critMultiplier;}
    public int getDamage() {return damage;}
    public int getHp() {return hp;}
    public int getInitialMana() {return initialMana;}
    public int getMagicResist() {return magicResist;}
    public int getMana() {return mana;}
    public int getRange() {return range;}

}
