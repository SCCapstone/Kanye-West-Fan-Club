package com.example.csceprojectrun2;

public class Champion {
    String name;
    Ability ability;
    int cost;
    Stats stats;

    Champion(String name, int cost, Ability ability, Stats stats) {
        this.name = name;
        this.ability = ability;
        this.cost = cost;
        this.stats = stats;
    }

    public String getName() {
        return name;
    }
}
