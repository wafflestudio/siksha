package com.wafflestudio.siksha.form.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyu Kang on 2015-10-08.
 */
public class Information {
    @SerializedName("time")
    public String time;

    @SerializedName("data")
    public Content[] data;

    public static class Content {
        @SerializedName("name")
        public String name;

        @SerializedName("operatingHour")
        public String operatingHour;

        @SerializedName("location")
        public String location;
    }
}
