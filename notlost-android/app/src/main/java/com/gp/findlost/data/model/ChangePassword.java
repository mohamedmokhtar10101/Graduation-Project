package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChangePassword implements Serializable {

    @SerializedName("oldPass")
    @Expose
    private String oldPassword;

    @SerializedName("newPass")
    @Expose
    private String newPassword;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("userType")
    @Expose
    private String userType;

    public ChangePassword(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
