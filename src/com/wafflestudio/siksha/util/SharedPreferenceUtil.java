package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
  public static final String PREF_NAME = "SIKSHA_PREF";

  public static void save(Context context, String prefName, String key, String value) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, 0);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, value);
    editor.commit();
  }

  public static String load(Context context, String prefName, String key) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, 0);
    return sharedPreferences.getString(key, "");
  }
}
