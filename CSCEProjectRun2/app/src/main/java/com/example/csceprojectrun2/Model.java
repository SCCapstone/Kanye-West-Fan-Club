package com.example.csceprojectrun2;

public class Model {
    String id;
    String title;
    String description;
    String accountName;
    String accountID;

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

    public String getAccountName() {
        return accountName;
    }

    public String getAccountID() {
        return accountID;
    }


    public Model(String id, String title, String description, String accountName, String accountID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.accountName = accountName;
        this.accountID = accountID;
    }
}