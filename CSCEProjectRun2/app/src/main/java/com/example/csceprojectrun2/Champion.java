package com.example.csceprojectrun2;

import java.io.Serializable;

public class Champion implements Serializable {
    String name;
    Ability ability;
    int cost;
    Stats stats;


    //Initializes the attributes of a champion
    public Champion(String name, int cost, Ability ability, Stats stats) {
        this.name = name;
        this.ability = ability;
        this.cost = cost;
        this.stats = stats;
    }

    public String getName() {
        return name;
    }
    public int getCost() { return cost; }
    public Ability getAbility() {return ability; }
    public Stats getStats() {return stats; }
}
