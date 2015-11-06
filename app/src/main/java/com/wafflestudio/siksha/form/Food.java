package com.wafflestudio.siksha.form;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Gyu Kang on 2015-09-17.
 */

public class Food implements Serializable {
    @SerializedName("time")
    public String time;

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public String price;
}