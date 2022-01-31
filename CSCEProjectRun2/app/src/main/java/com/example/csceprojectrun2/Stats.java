package com.example.csceprojectrun2;

public class Stats {
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

    Stats(int armor,double attackSpeed,float critChance,double critMultiplier,int damage,int hp,int initialMana,int magicResist,int mana,int range) {
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

}
