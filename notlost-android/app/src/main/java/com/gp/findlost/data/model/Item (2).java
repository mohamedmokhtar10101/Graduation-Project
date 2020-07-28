package com.gp.findlost.data.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gp.findlost.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("isCompleted")
    @Expose
    private boolean isCompleted;

    @SerializedName("images")
    @Expose
    private List<String> images;

    @SerializedName("city")
    @Expose
    private City city;

    @SerializedName("userId")
    @Expose
    private String userId;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getImages() {
        return images;
    }

    public City getCity() {
        return city;
    }

    public String getType() {
        return type;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    @BindingAdapter("loadImage")
    public static void setImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            if (!imageUrl.isEmpty()) {
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.img_placeholder)
                        .into(imageView);
            }
        }
    }
}
