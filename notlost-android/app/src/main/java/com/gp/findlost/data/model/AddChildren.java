package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddChildren {
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

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("cityId")
    @Expose
    private String cityId;

    @SerializedName("age")
    @Expose
    private int age;

    @SerializedName("images")
    @Expose
    private List<String> images;

    public AddChildren(String name, String description, String address, String type, String gender, int age, List<String> images, String cityId) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.type = type;
        this.gender = gender;
        this.age = age;
        this.images = images;
        this.cityId = cityId;
    }
}
