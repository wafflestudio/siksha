package com.wafflestudio.siksha.util;

import android.content.Context;
import android.util.Log;

import com.wafflestudio.siksha.form.InformationJSON;
import com.wafflestudio.siksha.form.Menu;
import com.wafflestudio.siksha.form.MenuJSON;
import com.wafflestudio.siksha.form.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData {
    private static AppData appData;

    private Map<String, InformationJSON.Content> informationDictionary;
    public Map<String, Restaurant> breakfastMenuDictionary;
    public Map<String, Restaurant> lunchMenuDictionary;
    public Map<String, Restaurant> dinnerMenuDictionary;

    private AppData() {
    }

    public static synchronized AppData getInstance() {
        if (appData == null)
            appData = new AppData();

        return appData;
    }

    public void setInformationDictionary(InformationJSON.Content[] data) {
        informationDictionary = new HashMap<String, InformationJSON.Content>();

        for (InformationJSON.Content content : data) {
            informationDictionary.put(content.name, content);
        }
    }

    public Map<String, InformationJSON.Content> getInformationDictionary() {
        return informationDictionary;
    }

    public void setSequences(Context context) {
        String[] restaurants = {"학생회관 식당", "농생대 3식당", "919동 기숙사 식당", "자하연 식당", "302동 식당",
                "솔밭 간이 식당", "동원관 식당", "감골 식당", "사범대 4식당", "두레미담",
                "301동 식당", "예술계복합연구동 식당", "공대 간이 식당", "상아회관 식당", "220동 식당",
                "대학원 기숙사 식당", "85동 수의대 식당"};
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < restaurants.length; i++) {
            if (i == 0)
                stringBuilder.append(restaurants[i]);
            else
                stringBuilder.append("/").append(restaurants[i]);
        }

        Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_DEFAULT_SEQUENCE, stringBuilder.toString());

        if (Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_SEQUENCE).equals("")) {
            Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_SEQUENCE, stringBuilder.toString());
        }
    }

    public void setMenuDictionaries(MenuJSON.Content[] data) {
        breakfastMenuDictionary = new HashMap<String, Restaurant>();
        lunchMenuDictionary = new HashMap<String, Restaurant>();
        dinnerMenuDictionary = new HashMap<String, Restaurant>();

        for (MenuJSON.Content content : data) {
            Restaurant breakfast = new Restaurant();
            Restaurant lunch = new Restaurant();
            Restaurant dinner = new Restaurant();

            List<Menu> breakfastMenuList = new ArrayList<Menu>();
            List<Menu> lunchMenuList = new ArrayList<Menu>();
            List<Menu> dinnerMenuList = new ArrayList<Menu>();

            for (Menu menu : content.menus) {
                if (menu.time.equals("breakfast"))
                    breakfastMenuList.add(menu);
                else if (menu.time.equals("lunch"))
                    lunchMenuList.add(menu);
                else if (menu.time.equals("dinner"))
                    dinnerMenuList.add(menu);
            }

            breakfast.name = content.restaurant;
            lunch.name = content.restaurant;
            dinner.name = content.restaurant;

            breakfast.isEmpty = breakfastMenuList.size() == 0;
            lunch.isEmpty = lunchMenuList.size() == 0;
            dinner.isEmpty = dinnerMenuList.size() == 0;

            breakfast.menus = breakfastMenuList;
            lunch.menus = lunchMenuList;
            dinner.menus = dinnerMenuList;

            breakfastMenuDictionary.put(breakfast.name, breakfast);
            lunchMenuDictionary.put(lunch.name, lunch);
            dinnerMenuDictionary.put(dinner.name, dinner);
        }
    }

    public List<Restaurant> getBookmarkRestaurantMenuList(Context context, Map<String, Restaurant> menuDictionary) {
        String prefBookmarks = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS);
        List<Restaurant> list = new ArrayList<Restaurant>();

        if (prefBookmarks.equals(""))
            return list;

        String[] bookmarks = prefBookmarks.split("/");
        for (String restaurant : bookmarks) {
            list.add(menuDictionary.get(restaurant));
        }

        return list;
    }

    public List<Restaurant> getMenuList(Context context, Map<String, Restaurant> menuDictionary) {
        String prefCurrentSequence = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_SEQUENCE);
        List<Restaurant> list = new ArrayList<Restaurant>();

        if (prefCurrentSequence.equals(""))
            return list;

        String[] currentSequence = prefCurrentSequence.split("/");
        for (String restaurant : currentSequence) {
            list.add(menuDictionary.get(restaurant));
        }

        return list;
    }

    public List<Restaurant> getNotEmptyMenuList(Context context, Map<String, Restaurant> menuDictionary) {
        String prefCurrentSequence = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_SEQUENCE);
        List<Restaurant> list = new ArrayList<Restaurant>();

        if (prefCurrentSequence.equals(""))
            return list;

        String[] currentSequence = prefCurrentSequence.split("/");
        for (String restaurant : currentSequence) {
            if (!menuDictionary.get(restaurant).isEmpty)
                list.add(menuDictionary.get(restaurant));
        }

        return list;
    }

    public List<Restaurant> getWidgetMenuList(Context context, Map<String, Restaurant> menuDictionary, int appWidgetID) {
        String prefWidgetRestaurants = Preference.loadStringValue(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_RESTAURANTS_PREFIX + appWidgetID);
        List<Restaurant> list = new ArrayList<Restaurant>();

        if (prefWidgetRestaurants.equals(""))
            return list;

        String[] widgetRestaurants = prefWidgetRestaurants.split("/");
        for (String restaurant : widgetRestaurants) {
            list.add(menuDictionary.get(restaurant));
        }

        return list;
    }
}