package com.wafflestudio.siksha.form.response;

import com.google.gson.annotations.SerializedName;
import com.wafflestudio.siksha.form.Food;

public class Menu {
    @SerializedName("time")
    public String time;

    @SerializedName("data")
    public Content[] data;

    public static class Content {
        @SerializedName("restaurant")
        public String restaurant;

        @SerializedName("foods")
        public Food[] foods;
    }
}
