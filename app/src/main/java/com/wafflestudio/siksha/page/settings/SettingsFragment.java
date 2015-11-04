package com.wafflestudio.siksha.page.settings;

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
 * Created by Gyu Kang on 2015-09-17.
 */
public class SettingsFragment extends Fragment {
    private SettingsRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.settings_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SettingsRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void notifyToAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
