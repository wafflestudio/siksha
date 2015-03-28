package com.wafflestudio.siksha.util;

import android.content.Context;

import com.wafflestudio.siksha.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantInfoUtil {
  public static RestaurantInfoUtil restaurantInfoUtil;

  public String[] restaurants;
  public String[] operatingHours;
  public String[] locations;

  public Map<String, String> operatingHourMap;
  public Map<String, String> locationMap;

  public Map<String, RestaurantClassifiedForm> breakfastMenuMap;
  public Map<String, RestaurantClassifiedForm> lunchMenuMap;
  public Map<String, RestaurantClassifiedForm> dinnerMenuMap;

  private RestaurantInfoUtil() { }

  public static RestaurantInfoUtil getInstance() {
    if (restaurantInfoUtil == null)
      restaurantInfoUtil = new RestaurantInfoUtil();

    return restaurantInfoUtil;
  }

  public void initialize(Context context) {
    operatingHourMap = new HashMap<String, String>();
    locationMap = new HashMap<String, String>();

    breakfastMenuMap = new HashMap<String, RestaurantClassifiedForm>();
    lunchMenuMap = new HashMap<String, RestaurantClassifiedForm>();
    dinnerMenuMap = new HashMap<String, RestaurantClassifiedForm>();

    restaurants = context.getResources().getStringArray(R.array.restaurants);
    operatingHours = context.getResources().getStringArray(R.array.operating_hours);
    locations = context.getResources().getStringArray(R.array.locations);

    for (int i = 0; i < restaurants.length; i++) {
      operatingHourMap.put(restaurants[i], operatingHours[i]);
      locationMap.put(restaurants[i], locations[i]);
    }
  }

  public void setMenuMap(RestaurantCrawlingForm[] forms) {
    for (RestaurantCrawlingForm form : forms) {
      RestaurantClassifiedForm breakfastMenuForm = new RestaurantClassifiedForm();
      RestaurantClassifiedForm lunchMenuForm = new RestaurantClassifiedForm();
      RestaurantClassifiedForm dinnerMenuForm = new RestaurantClassifiedForm();

      List<RestaurantCrawlingForm.MenuInfo> breakfastMenus = new ArrayList<RestaurantCrawlingForm.MenuInfo>();
      List<RestaurantCrawlingForm.MenuInfo> lunchMenus = new ArrayList<RestaurantCrawlingForm.MenuInfo>();
      List<RestaurantCrawlingForm.MenuInfo> dinnerMenus = new ArrayList<RestaurantCrawlingForm.MenuInfo>();

      for (RestaurantCrawlingForm.MenuInfo menu : form.menus) {
        if (menu.time.equals("breakfast"))
          breakfastMenus.add(menu);
        else if (menu.time.equals("lunch"))
          lunchMenus.add(menu);
        else if (menu.time.equals("dinner"))
          dinnerMenus.add(menu);
      }

      breakfastMenuForm.restaurant = form.restaurant;
      lunchMenuForm.restaurant = form.restaurant;
      dinnerMenuForm.restaurant = form.restaurant;

      breakfastMenuForm.menus = breakfastMenus;
      lunchMenuForm.menus = lunchMenus;
      dinnerMenuForm.menus = dinnerMenus;

      breakfastMenuForm.isEmpty = breakfastMenus.size() == 0;
      lunchMenuForm.isEmpty = lunchMenus.size() == 0;
      dinnerMenuForm.isEmpty = dinnerMenus.size() == 0;

      breakfastMenuMap.put(breakfastMenuForm.restaurant, breakfastMenuForm);
      lunchMenuMap.put(lunchMenuForm.restaurant, lunchMenuForm);
      dinnerMenuMap.put(dinnerMenuForm.restaurant, dinnerMenuForm);
    }
  }
}