package com.wafflestudio.siksha.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantSequencer {
  public static RestaurantSequencer restaurantSequencer;

  public ArrayList<String> originalSequence;
  public ArrayList<String> currentSequence;

  public List<RestaurantClassifiedForm> breakfastMenuList;
  public List<RestaurantClassifiedForm> lunchMenuList;
  public List<RestaurantClassifiedForm> dinnerMenuList;

  private Map<String, Boolean> bookmarkFlagMap;
  private boolean bookmarkMode;

  private RestaurantSequencer() { }

  public static RestaurantSequencer getInstance() {
    if (restaurantSequencer == null)
      restaurantSequencer = new RestaurantSequencer();

    return restaurantSequencer;
  }

  public void initialize(Context context) {
    originalSequence = new ArrayList<String>();
    currentSequence = new ArrayList<String>();
    bookmarkFlagMap = new HashMap<String, Boolean>();

    RestaurantInfoUtil restaurantInfoUtil = RestaurantInfoUtil.getInstance();
    Collections.addAll(originalSequence, restaurantInfoUtil.restaurants);

    String recordedSequence = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_SEQUENCE);

    if (!recordedSequence.equals(""))
      Collections.addAll(currentSequence, recordedSequence.split("/"));
    else
      Collections.addAll(currentSequence, restaurantInfoUtil.restaurants);
}

  public void modifySequence(String name) {
    int originalIndex = originalSequence.indexOf(name);

    if (!isBookMarked(name)) {
      currentSequence.remove(name);
      currentSequence.add(originalIndex, name);
    }
    else {
      currentSequence.remove(name);
      currentSequence.add(0, name);
    }
  }

  public void setMenuList(Map<String, RestaurantClassifiedForm> breakfast, Map<String, RestaurantClassifiedForm> lunch, Map<String, RestaurantClassifiedForm> dinner) {
    breakfastMenuList = new ArrayList<RestaurantClassifiedForm>();
    lunchMenuList = new ArrayList<RestaurantClassifiedForm>();
    dinnerMenuList = new ArrayList<RestaurantClassifiedForm>();

    for(String name : currentSequence) {
      breakfastMenuList.add(breakfast.get(name));
      lunchMenuList.add(lunch.get(name));
      dinnerMenuList.add(dinner.get(name));
    }
  }

  public boolean isBookmarkMode() {
    return bookmarkMode;
  }

  public void setBookmarkMode(boolean flag) {
    bookmarkMode = flag;
  }

  public void setBookmark(String key, boolean value) {
    bookmarkFlagMap.put(key, value);
  }

  public boolean isBookMarked(String key) {
    return bookmarkFlagMap.get(key) != null && bookmarkFlagMap.get(key);
  }

  public List<String> getBookmarkList() {
    List<String> bookmarkList = new ArrayList<String>();

    for(String name : currentSequence) {
      if (isBookMarked(name))
        bookmarkList.add(name);
    }

    return bookmarkList;
  }

  public void recordRestaurantSequence(Context context) {
    StringBuilder sequence = new StringBuilder();

    for(int i = 0; i < currentSequence.size(); i++) {
      if (i == currentSequence.size() - 1)
        sequence.append(currentSequence.get(i));
      else
        sequence.append(currentSequence.get(i)).append("/");
    }

    SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_SEQUENCE, sequence.toString());
  }

  public void recordBookmarkList(Context context) {
    StringBuilder stringBuilder = new StringBuilder();
    List<String> bookmarkList = getBookmarkList();

    if (bookmarkList.size() == 0) {
      SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK, stringBuilder.toString());
      return;
    }

    for(int i = 0; i < bookmarkList.size(); i++) {
      if (i == bookmarkList.size() - 1)
        stringBuilder.append(bookmarkList.get(i));
      else
        stringBuilder.append(bookmarkList.get(i)).append("/");
    }

    SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK, stringBuilder.toString());
  }
}
