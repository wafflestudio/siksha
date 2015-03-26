package com.wafflestudio.siksha.util;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.wafflestudio.siksha.page.BreakfastPage;
import com.wafflestudio.siksha.page.DinnerPage;
import com.wafflestudio.siksha.page.LunchPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class RestaurantSequencer {
  public static RestaurantSequencer restaurantSequencer;

  public List<String> originalSequence;
  public List<String> currentSequence;

  public List<RestaurantClassifiedForm> breakfastMenuList;
  public List<RestaurantClassifiedForm> lunchMenuList;
  public List<RestaurantClassifiedForm> dinnerMenuList;

  private ViewPager viewPager;

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

    setBookmarkMode(false);

    String recordedSequence = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_SEQUENCE);
    String recordedBookmark = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK);
    RestaurantInfoUtil restaurantInfoUtil = RestaurantInfoUtil.getInstance();

    Collections.addAll(originalSequence, restaurantInfoUtil.restaurants);

    if (!recordedSequence.equals(""))
      Collections.addAll(currentSequence, recordedSequence.split("/"));
    else
      Collections.addAll(currentSequence, restaurantInfoUtil.restaurants);

    if (!recordedBookmark.equals("")) {
      for(String bookmark : recordedBookmark.split("/"))
        bookmarkFlagMap.put(bookmark, true);
    }
  }

  public void setViewPager(ViewPager viewPager) {
    this.viewPager = viewPager;
  }

  public void modifySequence(String name) {
    if (!isBookMarked(name)) {
      List<String> arrangedList = new ArrayList<String>();

      for(int i = 0; i < currentSequence.size(); i++) {
        if (!isBookMarked(currentSequence.get(i)))
          arrangedList.add(currentSequence.get(i));
      }

      Collections.sort(arrangedList, new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
          return originalSequence.indexOf(lhs) > originalSequence.indexOf(rhs) ? 1 : -1;
        }
      });

      for(int i = currentSequence.size() - 1; i >= 0; i--) {
        if (isBookMarked(currentSequence.get(i)))
          arrangedList.add(0, currentSequence.get(i));
      }

      currentSequence = arrangedList;
    }
    else {
      List<String> arrangedList = new ArrayList<String>();

      for(int i = 0; i < currentSequence.size(); i++) {
        if (isBookMarked(currentSequence.get(i)))
          arrangedList.add(currentSequence.get(i));
      }

      for(int i = 0; i < currentSequence.size(); i++) {
        if (!isBookMarked(currentSequence.get(i)))
          arrangedList.add(currentSequence.get(i));
      }

      currentSequence = arrangedList;
    }
  }

  public void setMenuListOnSequence() {
    RestaurantInfoUtil restaurantInfoUtil = RestaurantInfoUtil.getInstance();

    breakfastMenuList = new ArrayList<RestaurantClassifiedForm>();
    lunchMenuList = new ArrayList<RestaurantClassifiedForm>();
    dinnerMenuList = new ArrayList<RestaurantClassifiedForm>();

    for(String name : currentSequence) {
      breakfastMenuList.add(restaurantInfoUtil.breakfastMenuMap.get(name));
      lunchMenuList.add(restaurantInfoUtil.lunchMenuMap.get(name));
      dinnerMenuList.add(restaurantInfoUtil.dinnerMenuMap.get(name));
    }
  }

  public void notifyChangeToAdapters(boolean isInitialization) {
    BreakfastPage breakfastPage = (BreakfastPage) viewPager.findViewWithTag("page" + 0);
    LunchPage lunchPage = (LunchPage) viewPager.findViewWithTag("page" + 1);
    DinnerPage dinnerPage = (DinnerPage) viewPager.findViewWithTag("page" + 2);

    breakfastPage.expandableListAdapter.forms = breakfastMenuList;
    lunchPage.expandableListAdapter.forms = lunchMenuList;
    dinnerPage.expandableListAdapter.forms = dinnerMenuList;

    breakfastPage.expandableListAdapter.isInitialization = isInitialization;
    lunchPage.expandableListAdapter.isInitialization = isInitialization;
    dinnerPage.expandableListAdapter.isInitialization = isInitialization;

    breakfastPage.expandableListAdapter.notifyDataSetChanged();
    lunchPage.expandableListAdapter.notifyDataSetChanged();
    dinnerPage.expandableListAdapter.notifyDataSetChanged();
  }

  public void expandBookmarkAll() {
    BreakfastPage breakfastPage = (BreakfastPage) viewPager.findViewWithTag("page" + 0);
    LunchPage lunchPage = (LunchPage) viewPager.findViewWithTag("page" + 1);
    DinnerPage dinnerPage = (DinnerPage) viewPager.findViewWithTag("page" + 2);

    breakfastPage.expandBookmarks();
    lunchPage.expandBookmarks();
    dinnerPage.expandBookmarks();
  }

  public void collapseAll() {
    BreakfastPage breakfastPage = (BreakfastPage) viewPager.findViewWithTag("page" + 0);
    LunchPage lunchPage = (LunchPage) viewPager.findViewWithTag("page" + 1);
    DinnerPage dinnerPage = (DinnerPage) viewPager.findViewWithTag("page" + 2);

    breakfastPage.collapseAllGroup();
    lunchPage.collapseAllGroup();
    dinnerPage.collapseAllGroup();
  }

  public boolean isBookmarkMode() {
    return bookmarkMode;
  }

  public void setBookmarkMode(boolean flag) {
    bookmarkMode = flag;
  }

  public void setBookmark(String key, boolean value) {
    if (bookmarkFlagMap.get(key) != null)
      bookmarkFlagMap.remove(key);

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
