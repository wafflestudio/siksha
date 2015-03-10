package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPreferenceUtil {
  public static final String PREF_APP_NAME = "Siksha";
  public static final String PREF_WIDGET_NAME = "com.wafflestudio.siksha.widget.BabWidgetProvider";

  public static final String PREF_PREFIX_KEY = "bab_widget_";
  public static final String PREF_WIDGET_ID = "bab_widget_ids";

  public static void saveValueOfString(Context context, String prefName, String key, String value) {
    SharedPreferences.Editor editor = context.getSharedPreferences(prefName, 0).edit();
    editor.putString(key, value);
    editor.commit();
  }

  public static String loadValueOfString(Context context, String prefName, String key) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, 0);
    return sharedPreferences.getString(key, "");
  }

  public static void saveValueOfStringSet(Context context, String prefName, String key, Set<String> value) {
    SharedPreferences.Editor editor = context.getSharedPreferences(prefName, 0).edit();
    editor.putStringSet(key, value);
    editor.commit();
  }

  public static Set<String> loadValueOfStringSet(Context context, String prefName, String key) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, 0);
    return sharedPreferences.getStringSet(key, null);
  }

  public static void removeValue(Context context, String prefName, String key) {
    SharedPreferences.Editor editor = context.getSharedPreferences(prefName, 0).edit();
    editor.remove(key);
    editor.commit();
  }
}
