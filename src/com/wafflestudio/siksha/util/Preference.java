package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

public class Preference {
    public static final String PREF_ALARM_NAME = "com.wafflestudio.siksha.alarm";
    public static final String PREF_APP_NAME = "com.wafflestudio.siksha";
    public static final String PREF_WIDGET_NAME = "com.wafflestudio.siksha.widget";

    public static final String PREF_KEY_LATEST_MENU_DATA = "latest_menu_data";
    public static final String PREF_KEY_LATEST_INFORMATION_DATA = "latest_information_data";

    public static final String PREF_KEY_ALARM_ALREADY_SET = "alarm_already_set";
    public static final String PREF_KEY_WIDGET_FEATURE_ALERTED = "widget_feature_alerted";
    public static final String PREF_KEY_EMPTY_MENU_INVISIBLE = "empty_menu_invisible";

    public static final String PREF_KEY_CURRENT_APP_VERSION = "current_app_version";
    public static final String PREF_KEY_LATEST_APP_VERSION = "latest_app_version";

    public static final String PREF_KEY_BOOKMARKS = "bookmarks";
    public static final String PREF_KEY_REFRESH_TIMESTAMP = "refresh_timestamp";
    public static final String PREF_KEY_DEFAULT_SEQUENCE = "default_sequence";
    public static final String PREF_KEY_CURRENT_SEQUENCE = "current_sequence";

    public static final String PREF_KEY_WIDGET_IDS = "widget_ids";
    public static final String PREF_KEY_WIDGET_RESTAURANTS_PREFIX = "widget_restaurants_";
    public static final String PREF_KEY_BREAKFAST_PREFIX = "widget_breakfast_";

    public static void save(Context context, String prefName, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void save(Context context, String prefName, String key, Set<String> value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static void save(Context context, String prefName, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String loadStringValue(Context context, String prefName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static Set<String> loadStringSetValue(Context context, String prefName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key, null);
    }

    public static boolean loadBooleanValue(Context context, String prefName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void remove(Context context, String prefName, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.apply();
    }
}
