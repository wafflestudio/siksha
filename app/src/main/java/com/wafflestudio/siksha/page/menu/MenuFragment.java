package com.wafflestudio.siksha.page.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.page.GroupRecyclerViewAdapter;
import com.wafflestudio.siksha.page.TimeSlotPage;
import com.wafflestudio.siksha.page.ViewPagerAdapter;
import com.wafflestudio.siksha.util.AppDataManager;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.Preference;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    private TextView dateView;
    private ArrayList<ImageView> pageIndicatorDots;
    private ViewPager viewPager;

    private Context context;
    private ViewPagerAdapter viewPagerAdapter;

    private int selectedPosition = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setCurrentItem(getSelectedPosition());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        dateView = (TextView) view.findViewById(R.id.menu_date_view);
        dateView.setTypeface(Fonts.fontAPAritaDotumMedium);

        pageIndicatorDots = new ArrayList<>();
        pageIndicatorDots.add((ImageView) view.findViewById(R.id.menu_page_indicator_dot_1));
        pageIndicatorDots.add((ImageView) view.findViewById(R.id.menu_page_indicator_dot_2));
        pageIndicatorDots.add((ImageView) view.findViewById(R.id.menu_page_indicator_dot_3));
        refreshPageIndicators(Date.getTimeSlotIndex());

        viewPager = (ViewPager) view.findViewById(R.id.menu_view_pager);
        viewPagerAdapter = new ViewPagerAdapter(context, getChildFragmentManager(), false);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                refreshPageIndicators(position);
                setSelectedPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
        viewPager.setCurrentItem(Date.getTimeSlotIndex());

        return view;
    }

    public void refreshPageIndicators(int position) {
        dateView.setText(new StringBuilder().append(Date.getPrimaryTimestamp(Date.TYPE_NORMAL)).append(" ").append(Date.getTimeSlot(position)).toString());

        for (int i = 0; i < pageIndicatorDots.size(); i++) {
            if (i == position)
                pageIndicatorDots.get(i).setImageResource(R.drawable.dot_selected);
            else
                pageIndicatorDots.get(i).setImageResource(R.drawable.dot_unselected);
        }
    }

    private void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition == -1 ? Date.getTimeSlotIndex() : selectedPosition;
    }

    public void notifyToAdapters() {
        AppDataManager appDataManager = AppDataManager.getInstance();
        List<GroupRecyclerViewAdapter> adapters = new ArrayList<>();

        for (int i = 0; i < viewPagerAdapter.getCount(); i++)
            adapters.add(((TimeSlotPage) viewPagerAdapter.getItem(i)).getAdapter());

        if (adapters.size() == viewPagerAdapter.getCount()) {
            if (Preference.loadBooleanValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_EMPTY_MENU_INVISIBLE)) {
                adapters.get(0).replaceData(appDataManager.getNotEmptyMenuList(context, appDataManager.breakfastMenuDictionary));
                adapters.get(1).replaceData(appDataManager.getNotEmptyMenuList(context, appDataManager.lunchMenuDictionary));
                adapters.get(2).replaceData(appDataManager.getNotEmptyMenuList(context, appDataManager.dinnerMenuDictionary));
            } else {
                adapters.get(0).replaceData(appDataManager.getMenuList(context, appDataManager.breakfastMenuDictionary));
                adapters.get(1).replaceData(appDataManager.getMenuList(context, appDataManager.lunchMenuDictionary));
                adapters.get(2).replaceData(appDataManager.getMenuList(context, appDataManager.dinnerMenuDictionary));
            }
        }

        for (int i = 0; i < viewPagerAdapter.getCount(); i++)
            adapters.get(i).notifyDataSetChanged();
    }
}
