package com.example.csceprojectrun2;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {
    private final String name;
    private final List<ItemBuild> itemBuilds;

    public Item(String name, List<ItemBuild> itemBuilds) {
        this.name = name;
        this.itemBuilds = itemBuilds;
    }

    public String getName() {
        return name;
    }

    public List<ItemBuild> getDetails() {
        return itemBuilds;
    }
}