package com.example.csceprojectrun2;

public class Model {
    String id;
    String title;
    String description;
    String accountName;
    String accountID;

    //Build model constructor
    public Model(String id, String title, String description, String accountName, String accountID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.accountName = accountName;
        this.accountID = accountID;
    }

    //Get id of a build model
    public String getId() {
        return id;
    }

    //Set id of a build model
    public void setId(String id) {
        this.id = id;
    }

    //Get title of a build model
    public String getTitle() {
        return title;
    }

    //Get description of a build model
    public String getDescription() {
        return description;
    }

    //Get account name of a build model
    public String getAccountName() {
        return accountName;
    }

    //Get account id of a build model
    public String getAccountID() {
        return accountID;
    }
}