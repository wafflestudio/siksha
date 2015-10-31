package com.wafflestudio.siksha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wafflestudio.siksha.page.settings.LicenseRecyclerViewAdapter;
import com.wafflestudio.siksha.util.Fonts;

/**
 * Created by Gyu Kang on 2015-10-29.
 */
public class LicenseActivity extends AppCompatActivity {
    private TextView titleView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        titleView = (TextView) findViewById(R.id.activity_license_title_view);
        recyclerView = (RecyclerView) findViewById(R.id.activity_license_recycler_view);

        titleView.setTypeface(Fonts.fontBMJua);

        LicenseRecyclerViewAdapter adapter = new LicenseRecyclerViewAdapter(
                getResources().getStringArray(R.array.library),
                getResources().getStringArray(R.array.link),
                getResources().getStringArray(R.array.license));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
