package com.wafflestudio.siksha.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WidgetProviderConfigureActivity extends AppCompatActivity {
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    String[] restaurants;

    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_provider_configure);
        setResult(RESULT_CANCELED);

        initialize();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null)
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.widget_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                addWidgetId(WidgetProviderConfigureActivity.this, appWidgetId);
                saveTitlePref(WidgetProviderConfigureActivity.this, appWidgetId, listAdapter.getChecked());
                startWidget();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initialize() {
        FontUtil.getInstance().setFontAsset(getAssets());
        restaurants = getResources().getStringArray(R.array.restaurants);

        Toolbar toolbar = (Toolbar) findViewById(R.id.config_activity_main_tool_bar);
        TextView title = (TextView) findViewById(R.id.config_activity_main_title);
        TextView appName = (TextView) findViewById(R.id.config_activity_main_app_name);

        title.setTypeface(FontUtil.fontBMJua);
        appName.setTypeface(FontUtil.fontBMJua);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listView = (ListView) findViewById(R.id.widget_configure_listview);
        listAdapter = new ListAdapter(this);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listAdapter.setChecked(position);
                        listAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    public boolean isEarlier(String r1, String r2) {
        int i, j;
        for (i = 0; i < restaurants.length; i++) {
            if (restaurants[i].equals(r1))
                break;
        }
        for (j = 0; j < restaurants.length; j++) {
            if (restaurants[j].equals(r2))
                break;
        }
        return i < j;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_MENU || super.onKeyDown(keyCode, event);
    }

    class ListAdapter extends BaseAdapter {
        private ViewHolder viewHolder;
        private LayoutInflater inflater;

        ArrayList<String> sequence;
        int isChecked;

        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            sequence = new ArrayList<String>();

            Collections.addAll(sequence, restaurants);
            isChecked = 0;
        }

        public String getChecked() {
            if (isChecked <= 0)
                return "";

            String checkedSequence = sequence.get(0);
            for (int i = 1; i < isChecked; i++) {
                checkedSequence += "#";
                checkedSequence += sequence.get(i);
            }

            return checkedSequence;
        }

        @Override
        public int getCount() {
            return sequence.size();
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public String getItem(int position) {
            return sequence.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.widget_configure_list_row, null);

                viewHolder = new ViewHolder();
                viewHolder.box = (CheckBox) convertView.findViewById(R.id.widget_configure_checkbox);

                convertView.setTag(viewHolder);
            }
            else
                viewHolder = (ViewHolder) convertView.getTag();

            final String restaurantName = sequence.get(position);
            TextView textView = (TextView) convertView.findViewById(R.id.widget_configure_row_restaurant);
            textView.setText(restaurantName);
            textView.setTypeface(FontUtil.fontAPAritaDotumMedium);

            viewHolder.box.setClickable(false);
            viewHolder.box.setFocusable(false);
            viewHolder.box.setChecked(position < isChecked);

            return convertView;
        }

        public void setChecked(int position) {
            String checkedRestaurant = sequence.get(position);
            if (position < isChecked) {
                int i;
                for (i = isChecked; i < sequence.size(); i++) {
                    if (isEarlier(checkedRestaurant, sequence.get(i)))
                        break;
                }
                sequence.add(i, checkedRestaurant);
                sequence.remove(position);
                isChecked--;
            }
            else {
                sequence.remove(position);
                sequence.add(isChecked, checkedRestaurant);
                isChecked++;
            }
        }

        private class ViewHolder {
            private CheckBox box;
        }
    }

    private void startWidget() {
        new WidgetBreakfastCheckDialog(this, appWidgetId).show();
    }

    static void addWidgetId(Context context, int appWidgetId) {
        Set<String> idSet = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID);

        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        idSet.add(Integer.toString(appWidgetId));
        SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID, idSet);
    }

    static boolean isValidId(Context context, int appWidgetId) {
        Set<String> idSet = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID);

        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        return idSet.contains(Integer.toString(appWidgetId));
    }

    static Set<String> getAllWidgetIds(Context context) {
        Set<String> idSet = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID);

        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        return idSet;
    }

    static void removeWidgetId(Context context, int appWidgetId) {
        Set<String> idSet = SharedPreferenceUtil.loadValueOfStringSet(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID);

        if (idSet == null) {
            return;
        }

        idSet.remove(Integer.toString(appWidgetId));
        SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_WIDGET_ID, idSet);
    }

    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_KEY + appWidgetId, text);
    }

    static String loadTitlePref(Context context, int appWidgetId) {
        String titleValue = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_KEY + appWidgetId);
        if (titleValue == null) {
            titleValue = "";
        }
        return titleValue;
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferenceUtil.removeValue(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_KEY + appWidgetId);
    }
}