package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
    @SerializedName("message")
    @Expose
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }
}
