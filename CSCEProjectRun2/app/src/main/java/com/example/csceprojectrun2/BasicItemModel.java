package com.example.csceprojectrun2;

public class BasicItemModel {
    String basicItemName;
    int image;

    public BasicItemModel(String basicItemName, int image) {
        this.basicItemName = basicItemName;
        this.image = image;
    }

    public String getBasicItemName() {
        return basicItemName;
    }

    public int getImage() {
        return image;
    }
}