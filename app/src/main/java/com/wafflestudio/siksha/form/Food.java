package com.wafflestudio.siksha.form;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Food implements Serializable {
    @SerializedName("time")
    public String time;

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public String price;

    @SerializedName("rating")
    public String rating;
}