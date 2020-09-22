package com.example.vlsm.data.model;

import java.util.ArrayList;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String password;
    private String displayName;
    private ArrayList<Project> userProjectlist;

    public LoggedInUser(String password, String displayName) {
        this.password = password;
        this.displayName = displayName;

    }

    public LoggedInUser(String password, String displayName, ArrayList<Project> userProjectlist) {
        this.password = password;
        this.displayName = displayName;
        this.userProjectlist = userProjectlist;
    }

    public String getUserId() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ArrayList<Project> getUserProjectlist() {
        return userProjectlist;
    }
}