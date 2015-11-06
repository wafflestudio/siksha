package com.wafflestudio.siksha.page;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wafflestudio.siksha.util.AppData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyu Kang on 2015-09-18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public ViewPagerAdapter(Context context, FragmentManager manager, boolean onBookmarkTab) {
        super(manager);

        AppData appData = AppData.getInstance();
        fragments = new ArrayList<>();

        if (onBookmarkTab) {
            fragments.add(TimeSlotPage.newInstance(appData.getBookmarkMenuList(context, appData.breakfastMenuDictionary), onBookmarkTab, 0));
            fragments.add(TimeSlotPage.newInstance(appData.getBookmarkMenuList(context, appData.lunchMenuDictionary), onBookmarkTab, 1));
            fragments.add(TimeSlotPage.newInstance(appData.getBookmarkMenuList(context, appData.dinnerMenuDictionary), onBookmarkTab, 2));
        }
        else {
            fragments.add(TimeSlotPage.newInstance(appData.getMenuList(context, appData.breakfastMenuDictionary), onBookmarkTab, 0));
            fragments.add(TimeSlotPage.newInstance(appData.getMenuList(context, appData.lunchMenuDictionary), onBookmarkTab, 1));
            fragments.add(TimeSlotPage.newInstance(appData.getMenuList(context, appData.dinnerMenuDictionary), onBookmarkTab, 2));
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
