package com.example.csceprojectrun2;

import java.io.Serializable;

public class ItemBuild implements Serializable {
    String buildName;
    String firstItemName;
    String secondItemName;
    String attr1;
    String attr2;
    String attr3;
    String desc;

    public ItemBuild(String buildName, String firstItemName, String secondItemName, String attr1, String attr2, String attr3, String desc) {
        this.buildName = buildName;
        this.firstItemName = firstItemName;
        this.secondItemName = secondItemName;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.desc = desc;
    }

    public String getBuildName() {
        return buildName;
    }

    public String getFirstItemName() {
        return firstItemName;
    }

    public String getSecondItemName() {
        return secondItemName;
    }

    public String getAttr1() {
        return attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public String getDesc() {
        return desc;
    }
}