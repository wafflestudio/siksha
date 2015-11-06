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

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.page.GroupRecyclerViewAdapter;
import com.wafflestudio.siksha.page.ViewPagerAdapter;
import com.wafflestudio.siksha.util.AppData;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.Fonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyu Kang on 2015-09-17.
 */
public class BookmarkFragment extends Fragment {
    private TextView dateView;
    private ArrayList<ImageView> pageIndicatorDots;
    private ViewPager viewPager;

    private Context context;
    private AppData appData;
    private List<GroupRecyclerViewAdapter> adapters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        appData = AppData.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

        setPageIndicators(Date.getTimeSlotIndex());
        viewPager.setCurrentItem(Date.getTimeSlotIndex());
        notifyToAdapters();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_fragment, container, false);

        dateView = (TextView) view.findViewById(R.id.bookmark_date_view);
        dateView.setTypeface(Fonts.fontAPAritaDotumMedium);

        pageIndicatorDots = new ArrayList<>();
        pageIndicatorDots.add((ImageView) view.findViewById(R.id.bookmark_page_indicator_dot_1));
        pageIndicatorDots.add((ImageView) view.findViewById(R.id.bookmark_page_indicator_dot_2));
        pageIndicatorDots.add((ImageView) view.findViewById(R.id.bookmark_page_indicator_dot_3));
        setPageIndicators(Date.getTimeSlotIndex());

        viewPager = (ViewPager) view.findViewById(R.id.bookmark_view_pager);
        adapters = new ArrayList<>();
        adapters.add(new GroupRecyclerViewAdapter(context, appData.getBookmarkMenuList(context, appData.breakfastMenuDictionary), true, 0));
        adapters.add(new GroupRecyclerViewAdapter(context, appData.getBookmarkMenuList(context, appData.lunchMenuDictionary), true, 1));
        adapters.add(new GroupRecyclerViewAdapter(context, appData.getBookmarkMenuList(context, appData.dinnerMenuDictionary), true, 2));
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), adapters));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setPageIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(Date.getTimeSlotIndex());

        return view;
    }

    private void setPageIndicators(int position) {
        dateView.setText(new StringBuilder().append(Date.getPrimaryTimestamp(Date.TYPE_NORMAL)).append(" ").append(Date.getTimeSlot(position)).toString());

        for (int i = 0; i < pageIndicatorDots.size(); i++) {
            if (i == position)
                pageIndicatorDots.get(i).setImageResource(R.drawable.dot_selected);
            else
                pageIndicatorDots.get(i).setImageResource(R.drawable.dot_unselected);
        }
    }

    public void notifyToAdapters() {
        if (adapters != null && adapters.size() == 3) {
            adapters.get(0).replaceData(appData.getBookmarkMenuList(context, appData.breakfastMenuDictionary));
            adapters.get(1).replaceData(appData.getBookmarkMenuList(context, appData.lunchMenuDictionary));
            adapters.get(2).replaceData(appData.getBookmarkMenuList(context, appData.dinnerMenuDictionary));

            for (int i = 0; i < adapters.size(); i++)
                adapters.get(i).notifyDataSetChanged();
        }
    }
}
