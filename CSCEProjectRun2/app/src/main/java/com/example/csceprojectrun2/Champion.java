package com.example.csceprojectrun2;

import java.io.Serializable;

public class Champion implements Serializable {
    String name;
    Ability ability;
    int cost;
    Stats stats;

    //Champion constructor
    Champion(String name, int cost, Ability ability, Stats stats) {
        this.name = name;
        this.ability = ability;
        this.cost = cost;
        this.stats = stats;
    }

    //Get name of champion
    public String getName() {
        return name;
    }

    //Get cost of champion
    public int getCost() {
        return cost;
    }

    //Get ability of champion
    public Ability getAbility() {
        return ability;
    }

    //Get stats of champion
    public Stats getStats() {
        return stats;
    }
}