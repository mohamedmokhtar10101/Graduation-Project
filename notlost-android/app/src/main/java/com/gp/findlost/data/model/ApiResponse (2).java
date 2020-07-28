package com.gp.findlost.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {
    @SerializedName("currentPage")
    @Expose
    private int currentPage;

    @SerializedName("pageSize")
    @Expose
    private int pageSize;

    @SerializedName("totalSize")
    @Expose
    private int totalSize;

    @SerializedName("data")
    @Expose
    private T data;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public T getData() {
        return data;
    }
}
