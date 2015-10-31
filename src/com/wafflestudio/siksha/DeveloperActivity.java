package com.wafflestudio.siksha;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.wafflestudio.siksha.util.Fonts;

public class DeveloperActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        TextView titleView = (TextView) findViewById(R.id.activity_developer_title_view);
        titleView.setTypeface(Fonts.fontBMJua);
    }
}
