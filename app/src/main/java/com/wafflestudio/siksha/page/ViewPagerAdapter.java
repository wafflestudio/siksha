package com.wafflestudio.siksha.page;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wafflestudio.siksha.util.AppDataManager;
import com.wafflestudio.siksha.util.Preference;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public ViewPagerAdapter(Context context, FragmentManager manager, boolean isBookmarkTab) {
        super(manager);

        AppDataManager appDataManager = AppDataManager.getInstance();
        fragments = new ArrayList<>();


        if (isBookmarkTab) {
            fragments.add(TimeSlotPage.newInstance(appDataManager.getBookmarkRestaurantMenuList(context, appDataManager.breakfastMenuDictionary), true, 0));
            fragments.add(TimeSlotPage.newInstance(appDataManager.getBookmarkRestaurantMenuList(context, appDataManager.lunchMenuDictionary), true, 1));
            fragments.add(TimeSlotPage.newInstance(appDataManager.getBookmarkRestaurantMenuList(context, appDataManager.dinnerMenuDictionary), true, 2));
        } else {
            if (Preference.loadBooleanValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_EMPTY_MENU_INVISIBLE)) {
                fragments.add(TimeSlotPage.newInstance(appDataManager.getNotEmptyMenuList(context, appDataManager.breakfastMenuDictionary), false, 0));
                fragments.add(TimeSlotPage.newInstance(appDataManager.getNotEmptyMenuList(context, appDataManager.lunchMenuDictionary), false, 1));
                fragments.add(TimeSlotPage.newInstance(appDataManager.getNotEmptyMenuList(context, appDataManager.dinnerMenuDictionary), false, 2));
            } else {
                fragments.add(TimeSlotPage.newInstance(appDataManager.getMenuList(context, appDataManager.breakfastMenuDictionary), false, 0));
                fragments.add(TimeSlotPage.newInstance(appDataManager.getMenuList(context, appDataManager.lunchMenuDictionary), false, 1));
                fragments.add(TimeSlotPage.newInstance(appDataManager.getMenuList(context, appDataManager.dinnerMenuDictionary), false, 2));
            }
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
