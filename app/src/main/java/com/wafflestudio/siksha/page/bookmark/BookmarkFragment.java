package com.wafflestudio.siksha.page.bookmark;

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

import com.viewpagerindicator.CirclePageIndicator;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.page.GroupRecyclerViewAdapter;
import com.wafflestudio.siksha.page.TimeSlotPage;
import com.wafflestudio.siksha.page.ViewPagerAdapter;
import com.wafflestudio.siksha.util.AppDataManager;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.Fonts;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment {
    private TextView dateView;
    private ViewPager viewPager;

    private Context context;
    private ViewPagerAdapter viewPagerAdapter;

    private CirclePageIndicator mCirclePageIndicator;

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
        View view = inflater.inflate(R.layout.bookmark_fragment, container, false);

        dateView = (TextView) view.findViewById(R.id.bookmark_date_view);
        dateView.setTypeface(Fonts.fontAPAritaDotumMedium);

        viewPager = (ViewPager) view.findViewById(R.id.bookmark_view_pager);
        viewPagerAdapter = new ViewPagerAdapter(context, getChildFragmentManager(), true);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                refreshPageIndicators(position);
                setSelectedPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(Date.getTimeSlotIndex());

        mCirclePageIndicator = (CirclePageIndicator)view.findViewById(R.id.circle_page_indicator);
        mCirclePageIndicator.setViewPager(viewPager);
        refreshPageIndicators(Date.getTimeSlotIndex());
        return view;
    }

    public void refreshPageIndicators(int position) {
        dateView.setText(new StringBuilder().append(Date.getPrimaryTimestamp(Date.TYPE_NORMAL)).append(" ").append(Date.getTimeSlot(position)).toString());
        mCirclePageIndicator.setCurrentItem(position);
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
            adapters.get(0).replaceData(appDataManager.getBookmarkRestaurantMenuList(context, appDataManager.breakfastMenuDictionary));
            adapters.get(1).replaceData(appDataManager.getBookmarkRestaurantMenuList(context, appDataManager.lunchMenuDictionary));
            adapters.get(2).replaceData(appDataManager.getBookmarkRestaurantMenuList(context, appDataManager.dinnerMenuDictionary));
        }

        for (int i = 0; i < viewPagerAdapter.getCount(); i++)
            adapters.get(i).notifyDataSetChanged();
    }
}
