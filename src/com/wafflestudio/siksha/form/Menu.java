package com.wafflestudio.siksha.form;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyu Kang on 2015-09-17.
 */

public class Menu {
    @SerializedName("time")
    public String time;

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public String price;
}