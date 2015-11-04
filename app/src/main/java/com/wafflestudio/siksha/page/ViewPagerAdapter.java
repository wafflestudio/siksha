package com.wafflestudio.siksha.page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyu Kang on 2015-09-18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager manager, List<GroupRecyclerViewAdapter> adapters) {
        super(manager);
        fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            fragments.add(new TimeSlotPage(adapters.get(i)));
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
