package com.wafflestudio.siksha.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.form.InformationJSON;
import com.wafflestudio.siksha.service.DownloadAlarm;
import com.wafflestudio.siksha.util.AppData;
import com.wafflestudio.siksha.util.DeviceNetwork;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.JSONParser;
import com.wafflestudio.siksha.util.Preference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WidgetConfigureActivity extends AppCompatActivity implements View.OnClickListener {
    private int appWidgetID;

    private WidgetConfigureRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configure);

        Intent intent = getIntent();
        appWidgetID = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (appWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish();

        DownloadAlarm.registerAlarm(this);
        DeviceNetwork.getInstance().initialize(this);
        Fonts.getInstance().initialize(this);
        AppData.getInstance().setSequences(this);
        AppData.getInstance().setInformationDictionary(JSONParser.parseJSONFile(this, InformationJSON.class).data);

        TextView messageView = (TextView) findViewById(R.id.activity_widget_configure_message_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_widget_configure_recycler_view);
        FloatingActionButton confirmButton = (FloatingActionButton) findViewById(R.id.activity_widget_configure_confirm_button);

        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WidgetConfigureRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_widget_configure_confirm_button:
                addAppWidgetID(this, appWidgetID);
                saveCheckedList();
                new WidgetBreakfastCheckDialog(this, appWidgetID).show();
                break;
        }
    }

    private void saveCheckedList() {
        List<String> checkedList = adapter.getCheckedList();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < checkedList.size(); i++) {
            if (i == 0)
                stringBuilder.append(checkedList.get(i));
            else
                stringBuilder.append("/").append(checkedList.get(i));
        }

        Log.d("saveCheckedList()", stringBuilder.toString());
        Preference.save(this, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_RESTAURANTS_PREFIX + appWidgetID, stringBuilder.toString());
    }

    public static void addAppWidgetID(Context context, int appWidgetID) {
        Set<String> idSet = Preference.loadStringSetValue(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_IDS);

        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        idSet.add(Integer.toString(appWidgetID));
        Log.d("addAppWidgetID()", appWidgetID + "");
        Preference.save(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_IDS, idSet);
    }

    public static boolean isValidAppWidgetID(Context context, int appWidgetID) {
        Set<String> idSet = Preference.loadStringSetValue(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_IDS);
        return idSet != null && idSet.contains(Integer.toString(appWidgetID));
    }

    public static Set<String> getAllAppWidgetIDs(Context context) {
        Set<String> idSet = Preference.loadStringSetValue(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_IDS);

        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        return idSet;
    }

    public static void removeAppWidgetID(Context context, int appWidgetID) {
        Set<String> idSet = Preference.loadStringSetValue(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_IDS);

        if (idSet == null) {
            return;
        }

        idSet.remove(Integer.toString(appWidgetID));
        Preference.remove(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_BREAKFAST_PREFIX + appWidgetID);
        Preference.remove(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_RESTAURANTS_PREFIX + appWidgetID);

        Preference.save(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_WIDGET_IDS, idSet);
    }
}