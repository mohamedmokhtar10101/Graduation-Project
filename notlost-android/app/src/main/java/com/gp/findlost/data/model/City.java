package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class City implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static List<String> getCities(List<City> cities) {
        List<String> citiesString = new ArrayList<>();
        for (City city : cities) {
            citiesString.add(city.name);
        }
        return citiesString;
    }
}
