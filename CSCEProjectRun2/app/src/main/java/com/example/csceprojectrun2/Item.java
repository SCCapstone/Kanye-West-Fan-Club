package com.example.csceprojectrun2;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {
    private final String name;
    private final List<ItemBuild> itemBuilds;

    //Item constructor
    public Item(String name, List<ItemBuild> itemBuilds) {
        this.name = name;
        this.itemBuilds = itemBuilds;
    }

    //Get name of item
    public String getName() {
        return name;
    }

    //Get item details
    public List<ItemBuild> getDetails() {
        return itemBuilds;
    }
}