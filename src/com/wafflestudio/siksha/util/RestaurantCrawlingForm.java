package com.wafflestudio.siksha.util;

import com.google.gson.annotations.SerializedName;

public class RestaurantCrawlingForm {
  @SerializedName("restaurant")
  public String restaurant;

  @SerializedName("menus")
  public MenuInfo[] menus;

  public static class MenuInfo {
    @SerializedName("time")
    public String time;

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public String price;
  }
}
