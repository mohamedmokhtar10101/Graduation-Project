package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckRequests implements Serializable {
    @SerializedName(("hasNonCompletedRequest"))
    @Expose
    private boolean hasNonCompletedRequest;

    @SerializedName(("itemCount"))
    @Expose
    private int itemsCount;

    @SerializedName(("childrenCount"))
    @Expose
    private int childrenCount;

    public boolean isHasNonCompletedRequest() {
        return hasNonCompletedRequest;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public int getChildrenCount() {
        return childrenCount;
    }
}
