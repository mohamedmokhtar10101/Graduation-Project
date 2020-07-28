package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestItem {
    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("itemId")
    @Expose
    private String itemId;


    @SerializedName("founderId")
    @Expose
    private String founderId;

    public RequestItem(String description, String itemId, String founderId) {
        this.description = description;
        this.itemId = itemId;
        this.founderId = founderId;
    }

}
