package com.wafflestudio.siksha.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

import java.util.HashSet;
import java.util.Set;

public class BabWidgetProviderConfigureActivity extends Activity {
    private EditText mAppWidgetText;
    private Button mAddButton;

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.bab_widget_provider_configure);

        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        mAddButton = (Button) findViewById(R.id.add_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              String widgetText = mAppWidgetText.getText().toString();
                                              addWidgetId(BabWidgetProviderConfigureActivity.this, appWidgetId);
                                              saveTitlePref(BabWidgetProviderConfigureActivity.this, appWidgetId, widgetText);
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