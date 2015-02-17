package com.wafflestudio.siksha;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class BabWidgetProviderConfigureActivity extends Activity {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    private static final String PREFS_NAME = "com.wafflestudio.babbabdirara.BabWidgetProvider";
    private static final String PREF_PREFIX_KEY = "babwidget_";

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

        mAppWidgetText.setText(loadTitlePref(BabWidgetProviderConfigureActivity.this, appWidgetId));
    }

    private void startWidget(String widgetText) {
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, intent);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intent);

        /*
        Intent fetchIntent = new Intent(this, WidgetFetchService.class);
        fetchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Log.e("Configure", Integer.toString(appWidgetId));
        Log.e("Configure", widgetText);

        int input = Integer.valueOf(widgetText);
        ArrayList<String> cafeList = new ArrayList<String>();
        if (input % 2 == 1)
            cafeList.add(WidgetFetchService.jikYoungCafes[0]);
        for (int i = 1; i < 7; i++) {
            input = input / 2;
            if (input % 2 == 1)
                cafeList.add(WidgetFetchService.jikYoungCafes[i]);
        }

        for (int i = 0; i < cafeList.size(); i++) {
            Log.e("Configure", cafeList.get(i));
        }

        fetchIntent.putStringArrayListExtra(BabWidgetProvider.CAFE_LIST, cafeList);
        startService(fetchIntent);
        */
        finish();
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
            return "2";
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}



