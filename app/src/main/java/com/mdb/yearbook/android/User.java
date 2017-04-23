package com.mdb.yearbook.android;

import java.util.ArrayList;

/**
 * Created by datarsd1 on 4/4/17.
 */

public class User
{

    private String name;
    private String email;
    private String profilePicUrl;
    private ArrayList<String> groupIds;
    private String userId;

    public User(String name, String email, String profilePicUrl, ArrayList<String> groupIds)
    {
        this.name = name;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
        this.groupIds = groupIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public ArrayList<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(ArrayList<String> groupIds) {
        this.groupIds = groupIds;
    }
}

