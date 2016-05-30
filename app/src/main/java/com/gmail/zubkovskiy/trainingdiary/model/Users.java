package com.gmail.zubkovskiy.trainingdiary.model;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Users {

    @SerializedName("user-token")
    private String userToken;

    private String email;
    private Date lastLogin;
    private String name;

    private String objectId;
    private Date updated;
    private Date created;
    private String ownerId;

    public String getUserToken() {
        return userToken;
    }

    public String getEmail() {
        return email;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public String getName() {
        return name;
    }

    public String getObjectId() {
        return objectId;
    }


    public Date getUpdated() {
        return updated;
    }

    public Date getCreated() {
        return created;
    }

    public String getOwnerId() {
        return ownerId;
    }


    @Override
    public String toString() {
        return "Users{" +
                "userToken='" + userToken + '\'' +
                ", email='" + email + '\'' +
                ", lastLogin=" + lastLogin +
                ", name='" + name + '\'' +
                ", objectId='" + objectId + '\'' +
                ", updated=" + updated +
                ", created=" + created +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}
