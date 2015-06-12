package com.wafflestudio.siksha.util;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.wafflestudio.siksha.page.BreakfastPage;
import com.wafflestudio.siksha.page.DinnerPage;
import com.wafflestudio.siksha.page.LunchPage;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sequencer {
  public static Sequencer sequencer;

  public List<String> originalSequence;
  public List<String> sequence;

  public List<MenuArrangedForm> breakfastMenuList;
  public List<MenuArrangedForm> lunchMenuList;
  public List<MenuArrangedForm> dinnerMenuList;

  private ViewPager viewPager;

  private Map<String, Boolean> bookmarkMap;
  private boolean bookmarkMode;

  private Sequencer() { }

  public static Sequencer getInstance() {
    if (sequencer == null)
      sequencer = new Sequencer();

    return sequencer;
  }

  public void initialize(Context context) {
    originalSequence = new ArrayList<String>();
    sequence = new ArrayList<String>();
    bookmarkMap = new HashMap<String, Boolean>();

    RestaurantInfoUtil restaurantInfoUtil = RestaurantInfoUtil.getInstance();
    String recordedOriginalSequence = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_ORIGINAL_SEQUENCE);
    String recordedSequence = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_SEQUENCE);
    String recordedBookmark = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK);

    setBookmarkMode(false);
    recordOriginalSequence(context, restaurantInfoUtil.restaurants);
    Collections.addAll(originalSequence, restaurantInfoUtil.restaurants);

    if (!recordedSequence.equals("")) {
      if (!recordedOriginalSequence.equals(getSequenceString(restaurantInfoUtil.restaurants)))
        Collections.addAll(sequence, restaurantInfoUtil.restaurants);
      else
        Collections.addAll(sequence, recordedSequence.split("/"));
    }
    else
      Collections.addAll(sequence, restaurantInfoUtil.restaurants);

    if (!recordedBookmark.equals("")) {
      for(String bookmark : recordedBookmark.split("/"))
        bookmarkMap.put(bookmark, true);
    }
  }

  public void setViewPager(ViewPager viewPager) {
    this.viewPager = viewPager;
  }

  public void modifySequence(String name) {
    List<String> arrangedList = new ArrayList<String>();

    if (!isBookMarked(name)) {
      for(int i = 0; i < sequence.size(); i++) {
        if (!isBookMarked(sequence.get(i)))
          arrangedList.add(sequence.get(i));
      }

      Collections.sort(arrangedList, new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
          return originalSequence.indexOf(lhs) > originalSequence.indexOf(rhs) ? 1 : -1;
        }
      });

      for(int i = sequence.size() - 1; i >= 0; i--) {
        if (isBookMarked(sequence.get(i)))
          arrangedList.add(0, sequence.get(i));
      }
    }
    else {
      for(int i = 0; i < sequence.size(); i++) {
        if (isBookMarked(sequence.get(i)))
          arrangedList.add(sequence.get(i));
      }

      for(int i = 0; i < sequence.size(); i++) {
        if (!isBookMarked(sequence.get(i)))
          arrangedList.add(sequence.get(i));
      }
    }

    sequence = arrangedList;
  }

  public void setMenuListOnSequence() {
    RestaurantInfoUtil restaurantInfoUtil = RestaurantInfoUtil.getInstance();

    breakfastMenuList = new ArrayList<MenuArrangedForm>();
    lunchMenuList = new ArrayList<MenuArrangedForm>();
    dinnerMenuList = new ArrayList<MenuArrangedForm>();

    for(String name : sequence) {
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

  public void expandAllBookmark() {
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

  public void cancelAllBookmark(Context context) {
    WeakReference<Context> weakReference = new WeakReference<Context>(context);

    String recordedSequence = SharedPreferenceUtil.loadValueOfString(weakReference.get(), SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_SEQUENCE);
    String recordedBookmark = SharedPreferenceUtil.loadValueOfString(weakReference.get(), SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK);

    if (!recordedSequence.equals("")) {
      List<String> previousSequence = new ArrayList<String>();
      Collections.addAll(previousSequence, recordedSequence.split("/"));
      sequence = previousSequence;
    }
    else
      sequence = originalSequence;

    for(int i = 0; i < sequence.size(); i++)
      setBookmark(sequence.get(i), false);

    if (!recordedBookmark.equals("")) {
      for(String bookmark : recordedBookmark.split("/"))
        setBookmark(bookmark, true);
    }
  }

  public boolean isBookmarkMode() {
    return bookmarkMode;
  }

  public void setBookmarkMode(boolean flag) {
    bookmarkMode = flag;
  }

  public void setBookmark(String key, boolean value) {
    if (bookmarkMap.get(key) != null)
      bookmarkMap.remove(key);

    bookmarkMap.put(key, value);
  }

  public boolean isBookMarked(String key) {
    return bookmarkMap.get(key) != null && bookmarkMap.get(key);
  }

  public List<String> getBookmarkList() {
    List<String> bookmarkList = new ArrayList<String>();

    for(String name : sequence) {
      if (isBookMarked(name))
        bookmarkList.add(name);
    }

    return bookmarkList;
  }

  public String getSequenceString(String[] restaurants) {
    StringBuilder sequence = new StringBuilder();

    for(int i = 0; i < restaurants.length; i++) {
      if (i == restaurants.length - 1)
        sequence.append(restaurants[i]);
      else
        sequence.append(restaurants[i]).append("/");
    }

    return sequence.toString();
  }

  public void recordOriginalSequence(Context context, String[] restaurants) {
    StringBuilder sequence = new StringBuilder();

    for(int i = 0; i < restaurants.length; i++) {
      if (i == restaurants.length - 1)
        sequence.append(restaurants[i]);
      else
        sequence.append(restaurants[i]).append("/");
    }

    SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_ORIGINAL_SEQUENCE, sequence.toString());
  }

  public void recordSequence(Context context) {
    StringBuilder sequence = new StringBuilder();

    for(int i = 0; i < this.sequence.size(); i++) {
      if (i == this.sequence.size() - 1)
        sequence.append(this.sequence.get(i));
      else
        sequence.append(this.sequence.get(i)).append("/");
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
