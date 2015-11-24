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
import com.wafflestudio.siksha.form.Menu;

import java.io.Serializable;
import java.util.List;

public class TimeSlotPage extends Fragment {
    private static final String KEY_DATA = "data";
    private static final String KEY_IS_BOOKMARK_TAB = "is_bookmark_tab";
    private static final String KEY_INDEX = "index";

    private GroupRecyclerViewAdapter adapter;

    public static TimeSlotPage newInstance(List<Menu> data, boolean isBookmarkTab, int index) {
        TimeSlotPage fragment = new TimeSlotPage();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATA, (Serializable) data);
        bundle.putSerializable(KEY_IS_BOOKMARK_TAB, isBookmarkTab);
        bundle.putSerializable(KEY_INDEX, index);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_slot_page, container, false);

        List<Menu> data = (List<Menu>) getArguments().getSerializable(KEY_DATA);
        boolean isBookmarkTab = (boolean) getArguments().getSerializable(KEY_IS_BOOKMARK_TAB);
        int index = (int) getArguments().getSerializable(KEY_INDEX);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.time_slot_page_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupRecyclerViewAdapter(getContext(), data, isBookmarkTab, index);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public GroupRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
