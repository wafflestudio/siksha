package com.wafflestudio.siksha.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.form.Restaurant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gyu Kang on 2015-09-11.
 */

public class TimeSlotPage extends Fragment {
    private static final String KEY_DATA = "data";
    private static final String KEY_ON_BOOKMARK_TAB = "on_bookmark_tab";
    private static final String KEY_INDEX = "index";

    private GroupRecyclerViewAdapter adapter;

    public static TimeSlotPage newInstance(List<Restaurant> data, boolean onBookmarkTab, int index) {
        TimeSlotPage fragment = new TimeSlotPage();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA, (Serializable) data);
        bundle.putSerializable(KEY_ON_BOOKMARK_TAB, onBookmarkTab);
        bundle.putSerializable(KEY_INDEX, index);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_slot_page, container, false);

        List<Restaurant> data = (List<Restaurant>) getArguments().getSerializable(KEY_DATA);
        boolean onBookmarkTab = (boolean) getArguments().getSerializable(KEY_ON_BOOKMARK_TAB);
        int index = (int) getArguments().getSerializable(KEY_INDEX);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.time_slot_page_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupRecyclerViewAdapter(getContext(), data, onBookmarkTab, index);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public GroupRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
