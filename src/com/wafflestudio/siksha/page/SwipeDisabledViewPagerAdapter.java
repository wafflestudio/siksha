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
    private List<Fragment> fragments;
    private List<String> pageTitles;

    public SwipeDisabledViewPagerAdapter(FragmentManager manager) {
        super(manager);

        fragments = new ArrayList<Fragment>();
        pageTitles = new ArrayList<String>();
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