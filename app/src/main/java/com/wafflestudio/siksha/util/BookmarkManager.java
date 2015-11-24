package com.wafflestudio.siksha.util;

import android.content.Context;

public class BookmarkManager {
    public static boolean isBookmarked(Context context, String name) {
        String[] bookmarks = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS).split("/");

        for (String bookmark : bookmarks) {
            if (bookmark.equals(name))
                return true;
        }

        return false;
    }

    public static void setAsBookmark(Context context, String name) {
        StringBuilder stringBuilder = new StringBuilder(Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS));

        if (stringBuilder.toString().equals(""))
            Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS, name);
        else
            Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS, stringBuilder.append("/").append(name).toString());
    }

    public static void unsetFromBookmark(Context context, String name) {
        String[] bookmarks = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS).split("/");
        StringBuilder stringBuilder = new StringBuilder();

        for (String bookmark : bookmarks) {
            if (!bookmark.equals(name)) {
                if (stringBuilder.toString().equals(""))
                    stringBuilder.append(bookmark);
                else
                    stringBuilder.append("/").append(bookmark);
            }
        }

        Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS, stringBuilder.toString());
    }
}
