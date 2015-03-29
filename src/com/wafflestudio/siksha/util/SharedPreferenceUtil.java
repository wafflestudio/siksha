package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

public class SharedPreferenceUtil {
  public static final String PREF_ALARM_NAME = "Alarm";
  public static final String PREF_APP_NAME = "Siksha";
  public static final String PREF_WIDGET_NAME = "com.wafflestudio.siksha.widget.WidgetProvider";

  public static final String PREF_KEY_JSON = "json_date";
  public static final String PREF_PREFIX_KEY = "widget_";
  public static final String PREF_PREFIX_BREAKFAST_KEY = "widget_breakfast";
  public static final String PREF_WIDGET_ID = "widget_ids";

  public static final String PREF_KEY_SEQUENCE = "restaurant_sequence";
  public static final String PREF_KEY_BOOKMARK = "bookmark_list";
  public static final String PREF_KEY_NOTIFY_WIDGET = "notify_widget";

  public static void save(Context context, String prefName, String key, String value) {
    SharedPreferences.Editor editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
    editor.putString(key, value);
    editor.commit();
  }

  public static void save(Context context, String prefName, String key, Set<String> value) {
    SharedPreferences.Editor editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
    editor.putStringSet(key, value);
    editor.commit();
  }

  public static void save(Context context, String prefName, String key, boolean value) {
    SharedPreferences.Editor editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
    editor.putBoolean(key, value);
    editor.commit();
  }

  public static String loadValueOfString(Context context, String prefName, String key) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    return sharedPreferences.getString(key, "");
  }

  public static Set<String> loadValueOfStringSet(Context context, String prefName, String key) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    return sharedPreferences.getStringSet(key, null);
  }

  public static boolean loadValueOfBoolean(Context context, String prefName, String key) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    return sharedPreferences.getBoolean(key, false);
  }

  public static void removeValue(Context context, String prefName, String key) {
    SharedPreferences.Editor editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
    editor.remove(key);
    editor.commit();
  }
}
