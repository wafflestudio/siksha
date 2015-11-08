package com.wafflestudio.siksha.util;

import android.content.Context;

import com.wafflestudio.siksha.form.Food;
import com.wafflestudio.siksha.form.response.Information;
import com.wafflestudio.siksha.form.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppDataManager {
    private static AppDataManager instance;

    private Map<String, Information.Content> informationDictionary;
    public Map<String, Menu> breakfastMenuDictionary;
    public Map<String, Menu> lunchMenuDictionary;
    public Map<String, Menu> dinnerMenuDictionary;

    private AppDataManager() {
    }

    public static synchronized AppDataManager getInstance() {
        if (instance == null)
            instance = new AppDataManager();

        return instance;
    }

    public void setInformationDictionary(Information.Content[] data) {
        informationDictionary = new HashMap<>();

        for (Information.Content content : data) {
            informationDictionary.put(content.name, content);
        }
    }

    public Map<String, Information.Content> getInformationDictionary() {
        return informationDictionary;
    }

    public void setDefaultRestaurantSequence(Context context) {
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

    public void setMenuDictionaries(com.wafflestudio.siksha.form.response.Menu.Content[] data) {
        breakfastMenuDictionary = new HashMap<>();
        lunchMenuDictionary = new HashMap<>();
        dinnerMenuDictionary = new HashMap<>();

        for (com.wafflestudio.siksha.form.response.Menu.Content content : data) {
            Menu breakfast = new Menu();
            Menu lunch = new Menu();
            Menu dinner = new Menu();

            List<Food> breakfastFoodList = new ArrayList<Food>();
            List<Food> lunchFoodList = new ArrayList<Food>();
            List<Food> dinnerFoodList = new ArrayList<Food>();

            for (Food food : content.foods) {
                switch (food.time) {
                    case "breakfast":
                        breakfastFoodList.add(food);
                        break;
                    case "lunch":
                        lunchFoodList.add(food);
                        break;
                    case "dinner":
                        dinnerFoodList.add(food);
                        break;
                }
            }

            breakfast.restaurant = content.restaurant;
            lunch.restaurant = content.restaurant;
            dinner.restaurant = content.restaurant;

            breakfast.empty = breakfastFoodList.size() == 0;
            lunch.empty = lunchFoodList.size() == 0;
            dinner.empty = dinnerFoodList.size() == 0;

            breakfast.foods = breakfastFoodList;
            lunch.foods = lunchFoodList;
            dinner.foods = dinnerFoodList;

            breakfastMenuDictionary.put(breakfast.restaurant, breakfast);
            lunchMenuDictionary.put(lunch.restaurant, lunch);
            dinnerMenuDictionary.put(dinner.restaurant, dinner);
        }
    }

    public List<Menu> getBookmarkRestaurantMenuList(Context context, Map<String, Menu> menuDictionary) {
        String prefBookmarks = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS);
        List<Menu> list = new ArrayList<Menu>();

        if (prefBookmarks.equals(""))
            return list;

        String[] bookmarks = prefBookmarks.split("/");
        for (String restaurant : bookmarks)
            list.add(menuDictionary.get(restaurant));

        return list;
    }

    public List<Menu> getMenuList(Context context, Map<String, Menu> menuDictionary) {
        String prefCurrentSequence = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_SEQUENCE);
        List<Menu> list = new ArrayList<Menu>();

        if (prefCurrentSequence.equals(""))
            return list;

        String[] currentSequence = prefCurrentSequence.split("/");
        for (String restaurant : currentSequence) {
            list.add(menuDictionary.get(restaurant));
        }

        return list;
    }

    public List<Menu> getNotEmptyMenuList(Context context, Map<String, Menu> menuDictionary) {
        String prefCurrentSequence = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_SEQUENCE);
        List<Menu> list = new ArrayList<Menu>();

        if (prefCurrentSequence.equals(""))
            return list;

        String[] currentSequence = prefCurrentSequence.split("/");
        for (String restaurant : currentSequence) {
            if (!menuDictionary.get(restaurant).empty)
                list.add(menuDictionary.get(restaurant));
        }

        return list;
    }

    public List<Menu> getMenuListForWidget(Context context, Map<String, Menu> menuDictionary, int appWidgetID) {
        String prefSelectedRestaurants = Preference.loadStringValue(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_RESTAURANTS_PREFIX + appWidgetID);
        List<Menu> list = new ArrayList<Menu>();

        if (prefSelectedRestaurants.equals(""))
            return list;

        String[] selectedRestaurants = prefSelectedRestaurants.split("/");
        for (String restaurant : selectedRestaurants) {
            list.add(menuDictionary.get(restaurant));
        }

        return list;
    }
}