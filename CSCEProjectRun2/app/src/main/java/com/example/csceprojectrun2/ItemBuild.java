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

    //Item build constructor
    public ItemBuild(String buildName, String firstItemName, String secondItemName, String attr1, String attr2, String attr3, String desc) {
        this.buildName = buildName;
        this.firstItemName = firstItemName;
        this.secondItemName = secondItemName;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.desc = desc;
    }

    //Get build name of item build
    public String getBuildName() {
        return buildName;
    }

    //Get first item of item build
    public String getFirstItemName() {
        return firstItemName;
    }

    //Get second item of item build
    public String getSecondItemName() {
        return secondItemName;
    }

    //Get first attribute of item build
    public String getAttr1() {
        return attr1;
    }

    //Get second attribute of item build
    public String getAttr2() {
        return attr2;
    }

    //Get third attribute of item build
    public String getAttr3() {
        return attr3;
    }

    //Get description of item build
    public String getDesc() {
        return desc;
    }
}