package com.wafflestudio.siksha.util;

import android.content.Context;

import com.wafflestudio.siksha.R;

import java.util.HashMap;
import java.util.Map;

public class RestaurantInfo {
  public static RestaurantInfo restaurantInfo;

  public static String[] restaurants;
  public static String[] operatingHours;
  public static String[] locations;

  public static Map<String, String> operatingHoursMap;
  public static Map<String, String> locationsMap;

  private RestaurantInfo() { }

  public static RestaurantInfo getInstance() {
    if (restaurantInfo == null)
      restaurantInfo = new RestaurantInfo();

    return restaurantInfo;
  }

  public void loading(Context context) {
    operatingHoursMap = new HashMap<String, String>();
    locationsMap = new HashMap<String, String>();

    restaurants = context.getResources().getStringArray(R.array.restaurants);
    operatingHours = context.getResources().getStringArray(R.array.operating_hours);
    locations = context.getResources().getStringArray(R.array.locations);

    for(int i = 0; i < restaurants.length; i++) {
      operatingHoursMap.put(restaurants[i], operatingHours[i]);
      locationsMap.put(restaurants[i], locations[i]);
    }
  }
}