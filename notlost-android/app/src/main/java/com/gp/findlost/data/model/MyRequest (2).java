package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyRequest implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("founder")
    @Expose
    private User founder;


    @SerializedName("image")
    @Expose
    private String image;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getState() {
        return state;
    }

    public User getFounder() {
        return founder;
    }
}
