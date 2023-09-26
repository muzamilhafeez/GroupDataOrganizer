package com.example.copytotext.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Shop {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("open_hours")
    private String openHours;

    @SerializedName("close_hours")
    private String closeHours;

    @SerializedName("create_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("image")
    private String image;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("owner_id")
    private int ownerId;

    @SerializedName("other_detail")
    private String otherDetail;

    @SerializedName("delivery_time")
    private String deliveryTime;

    @SerializedName("rating")
    private String rating;

    // Create constructors, getters, and setters as needed
}
