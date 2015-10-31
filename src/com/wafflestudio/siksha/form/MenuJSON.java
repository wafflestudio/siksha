package com.wafflestudio.siksha.form;

import com.google.gson.annotations.SerializedName;

public class MenuJSON {
    @SerializedName("time")
    public String time;

    @SerializedName("data")
    public Content[] data;

    public static class Content {
        @SerializedName("restaurant")
        public String restaurant;

        @SerializedName("menus")
        public Menu[] menus;
    }
}
