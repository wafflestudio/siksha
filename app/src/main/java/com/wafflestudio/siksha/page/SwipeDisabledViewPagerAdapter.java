package com.wafflestudio.siksha.page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyu Kang on 2015-09-18.
 */

public class SwipeDisabledViewPagerAdapter extends FragmentPagerAdapter {
    public static final int INDEX_BOOKMARK_PAGE = 0;
    public static final int INDEX_MENU_PAGE = 1;
    public static final int INDEX_SETTINGS_PAGE = 2;

    private List<Fragment> fragments;
    private List<String> pageTitles;

    public SwipeDisabledViewPagerAdapter(FragmentManager manager) {
        super(manager);
        fragments = new ArrayList<>();
        pageTitles = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    public void addPage(Fragment fragment, String title) {
        fragments.add(fragment);
        pageTitles.add(title);
    }


}