package com.example.csceprojectrun2;

public class PopularBuildModel {
    String buildName;
    int image;

    public PopularBuildModel(String basicItemName, int image) {
        this.buildName = basicItemName;
        this.image = image;
    }

    public String getBasicItemName() {
        return buildName;
    }

    public int getImage() {
        return image;
    }
}