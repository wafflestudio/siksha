package com.wafflestudio.siksha.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.wafflestudio.siksha.R;

import java.util.HashSet;
import java.util.Set;

public class BabWidgetProviderConfigureActivity extends Activity {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    private static final String PREFS_NAME = "com.wafflestudio.siksha.BabWidgetProvider";
    private static final String PREF_PREFIX_KEY = "babwidget_";
    private static final String PREF_WIDGET_ID = "babwidgetids";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.bab_widget_provider_configure);

        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        final Context context = BabWidgetProviderConfigureActivity.this;

                        String widgetText = mAppWidgetText.getText().toString();
                        addWidgetId(context, appWidgetId);
                        saveTitlePref(context, appWidgetId, widgetText);
                        startWidget(widgetText);
                    }
                }
        );

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    private void startWidget(String widgetText) {
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, intent);
        intent.setAction(BabWidgetProvider.CONFIGURATION_FINISHED);
        sendBroadcast(intent);
        finish();
    }

    static void addWidgetId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Set<String> idSet = prefs.getStringSet(PREF_WIDGET_ID, null);
        if (idSet == null) {
            idSet = new HashSet<String>();
        }

        idSet.add(Integer.toString(appWidgetId));
        SharedPreferences.Editor editPrefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editPrefs.putStringSet(PREF_WIDGET_ID, idSet);
        editPrefs.commit();
    }

    static boolean isValidId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Set<String> idSet = prefs.getStringSet(PREF_WIDGET_ID, null);
        if (idSet == null) {
            idSet = new HashSet<String>();
        }
        return idSet.contains(Integer.toString(appWidgetId));
    }

    static Set<String> getAllWidgetIds(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Set<String> idSet = prefs.getStringSet(PREF_WIDGET_ID, null);
        if (idSet == null) {
            idSet = new HashSet<String>();
        }
        return idSet;
    }

    static void removeWidgetId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Set<String> idSet = prefs.getStringSet(PREF_WIDGET_ID, null);
        if (idSet == null) {
            return;
        }

        idSet.remove(Integer.toString(appWidgetId));
        SharedPreferences.Editor editPrefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editPrefs.putStringSet(PREF_WIDGET_ID, idSet);
        editPrefs.commit();
    }

    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.commit();
    }

    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return "1";
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}



