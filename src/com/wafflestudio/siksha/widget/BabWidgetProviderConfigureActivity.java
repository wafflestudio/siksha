package com.wafflestudio.siksha.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.RestaurantInfo;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

import java.util.HashSet;
import java.util.Set;

public class BabWidgetProviderConfigureActivity extends Activity {
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.bab_widget_provider_configure);
        listView = (ListView) findViewById(R.id.widget_configure_listview);
        Button addButton = (Button) findViewById(R.id.widget_configure_add_button);
        Button cancleButton = (Button) findViewById(R.id.widget_configure_cancle_button);
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

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addWidgetId(BabWidgetProviderConfigureActivity.this, appWidgetId);
                        saveTitlePref(BabWidgetProviderConfigureActivity.this, appWidgetId, listAdapter.getChecked());
                        startWidget();
                    }
                }
        );

        cancleButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    class ListAdapter extends BaseAdapter {
        private ViewHolder viewHolder;
        private LayoutInflater inflater;

        private boolean[] isChecked;

        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            this.isChecked = new boolean[RestaurantInfo.restaurants.length];
        }

        public String getChecked() {
            String result = "";
            for (int i = 0; i < isChecked.length; i++) {
                if (isChecked[i])
                    result += "1";
                else
                    result += "0";
            }
            return result;
        }

        @Override
        public int getCount() {
            return isChecked.length;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public String getItem(int position) {
            return RestaurantInfo.restaurants[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                final String restaurantName = RestaurantInfo.restaurants[position];
                convertView = inflater.inflate(R.layout.bab_widget_configure_list_row, null);

                viewHolder = new ViewHolder();
                viewHolder.box = (CheckBox) convertView.findViewById(R.id.widget_configure_checkbox);
                convertView.setTag(viewHolder);
                TextView textView = (TextView) convertView.findViewById(R.id.widget_configure_row_restaurant);
                textView.setText(restaurantName);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.box.setClickable(false);
            viewHolder.box.setFocusable(false);
            viewHolder.box.setChecked(isChecked[position]);

            return convertView;
        }

        public void setChecked(int position) {
            isChecked[position] = !isChecked[position];
        }

        private class ViewHolder {
            private CheckBox box;
        }
    }

    private void startWidget() {
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, intent);

        intent.setAction(BabWidgetProvider.CONFIGURATION_FINISHED);
        sendBroadcast(intent);

        finish();
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

        if (!titleValue.equals(""))
            return titleValue;
        else
            return "1";
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferenceUtil.removeValue(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_KEY + appWidgetId);
    }
}