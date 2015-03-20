package com.wafflestudio.siksha.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BookmarkUtil {
  public static BookmarkUtil bookmarkUtil;

  private Map<String, Boolean> bookmarkFlagMap;

  private BookmarkUtil() { }

  public static BookmarkUtil getInstance() {
    if (bookmarkUtil == null)
      bookmarkUtil = new BookmarkUtil();

    return bookmarkUtil;
  }

  public void initialize() {
    bookmarkFlagMap = new HashMap<String, Boolean>();
  }

  public void setBookmark(String key, boolean value) {
    if (value)
      bookmarkFlagMap.put(key, true);
    else {
      if (bookmarkFlagMap.get(key) != null)
        bookmarkFlagMap.remove(key);
    }
  }

  public boolean isBookMarked(String key) {
    return !(bookmarkFlagMap.get(key) == null || !bookmarkFlagMap.get(key));
  }

  public List<String> getBookmarkList() {
    List<String> bookmarkList = new ArrayList<String>();

    Iterator iterator = bookmarkFlagMap.keySet().iterator();
    while (iterator.hasNext()) {
      String restaurant = (String) iterator.next();
      bookmarkList.add(restaurant);
      Log.d("restuarant", restaurant + " / " + bookmarkFlagMap.get(restaurant));
    }

    return bookmarkList;
  }
}
