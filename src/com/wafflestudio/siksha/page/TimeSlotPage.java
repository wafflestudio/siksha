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

/**
 * Created by Gyu Kang on 2015-09-11.
 */

public class TimeSlotPage extends Fragment {
    private GroupRecyclerViewAdapter groupRecyclerViewAdapter;

    public TimeSlotPage(GroupRecyclerViewAdapter groupRecyclerViewAdapter) {
        this.groupRecyclerViewAdapter = groupRecyclerViewAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_slot_page, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.time_slot_page_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(groupRecyclerViewAdapter);

        return view;
    }
}
