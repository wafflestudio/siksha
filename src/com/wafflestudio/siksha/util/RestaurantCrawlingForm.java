package com.wafflestudio.siksha.util;

import com.google.gson.annotations.SerializedName;

public class RestaurantCrawlingForm {

  @SerializedName("restaurant")
  public String restaurant;
  
  @SerializedName("menus")
  public RestaurantInfo[] menus;

  public class RestaurantInfo {

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public int price;

    @SerializedName("time")
    public String time;

  }

}
