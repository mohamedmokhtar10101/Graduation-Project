package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignIn implements Serializable {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;

    public SignIn(String username, String password, String deviceToken) {
        this.username = username;
        this.password = password;
        this.deviceToken = deviceToken;
    }

}