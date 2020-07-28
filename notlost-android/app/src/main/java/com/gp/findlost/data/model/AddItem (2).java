package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AddItem implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("type")
    @Expose
    private String type;


    @SerializedName("cityId")
    @Expose
    private String cityId;


    @SerializedName("images")
    @Expose
    private List<String> images;

    public AddItem(String name, String description, String address, String type, List<String> images, String cityId) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.type = type;
        this.images = images;
        this.cityId = cityId;
    }


}
