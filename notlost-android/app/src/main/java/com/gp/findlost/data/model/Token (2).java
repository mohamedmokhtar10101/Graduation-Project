package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Token implements Serializable {

    @SerializedName("x-auth-token")
    @Expose
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
