package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestChild {
    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("childrenId")
    @Expose
    private String childId;


    @SerializedName("founderId")
    @Expose
    private String founderId;


    public RequestChild(String image, String description, String childId, String founderId) {
        this.image = image;
        this.description = description;
        this.childId = childId;
        this.founderId = founderId;
    }

    public RequestChild(String description, String childId, String founderId) {
        this.description = description;
        this.childId = childId;
        this.founderId = founderId;
    }

}
