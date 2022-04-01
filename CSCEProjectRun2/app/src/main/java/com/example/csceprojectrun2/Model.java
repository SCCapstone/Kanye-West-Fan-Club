package com.example.csceprojectrun2;

public class Model {
    String id, title, description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Model(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}