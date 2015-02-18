package com.wafflestudio.siksha.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

import com.wafflestudio.siksha.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BabWidgetProviderConfigureActivity BabWidgetProviderConfigureActivity}
 */
public class BabWidgetProvider extends AppWidgetProvider {
    public static final String DATA_FETCHED = "com.wafflestudio.babbabdirara.DATA_FETCHED";
    public static final String CAFE_LIST = "com.wafflestudio.babbabdirara.CAFE_LIST";
    public static final String CAFE_MENU_LIST = "com.wafflestudio.babbabdirara.CAFE_MENU_LIST";
    public static final int randomNumber = 50;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("Update", "aa");
        /*
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            Intent fetchIntent = new Intent(context, WidgetFetchService.class);
            fetchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            Log.e("Update", "WidgetId" + Integer.toString(appWidgetIds[i]));
            int input = Integer.valueOf(BabWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetIds[i]));
            Log.e("Update", "WidgetSetting" + Integer.toString(input));

            ArrayList<String> cafeList = new ArrayList<String>();
            if (input % 2 == 1) {
                cafeList.add(WidgetFetchService.jikYoungCafes[0]);
            }
            for (int j = 1; j < 7; j++) {
                input = input / 2;
                if (input % 2 == 1)
                    cafeList.add(WidgetFetchService.jikYoungCafes[j]);
            }
            for (int j = 0; j < cafeList.size(); j++)
                Log.e("Update", cafeList.get(j));

            fetchIntent.putStringArrayListExtra(BabWidgetProvider.CAFE_LIST, cafeList);
            context.startService(fetchIntent);
        }
        */

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, ArrayList<String> cafeMenuList, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.bab_widget_provider);
        Intent remoteIntent = new Intent(context, WidgetRemoteService.class);
        remoteIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteIntent.putStringArrayListExtra(BabWidgetProvider.CAFE_MENU_LIST, cafeMenuList);
        remoteIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + randomNumber), null));
        remoteViews.setRemoteAdapter(R.id.listViewWidget, remoteIntent);
        // setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.emptyViewWidget);
        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int input = Integer.valueOf(BabWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId));
            Log.e("REUpdate", Integer.toString(appWidgetId));

            ArrayList<String> cafeList = new ArrayList<String>();
            if (input % 2 == 1) {
                cafeList.add(WidgetFetchService.jikYoungCafes[0]);
            }
            for (int j = 1; j < 7; j++) {
                input = input / 2;
                if (input % 2 == 1)
                    cafeList.add(WidgetFetchService.jikYoungCafes[j]);
            }
            for (int j = 0; j < cafeList.size(); j++)
                Log.e("REUpdate", cafeList.get(j));

            Intent fetchIntent = new Intent(context, WidgetFetchService.class);
            fetchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            fetchIntent.putStringArrayListExtra(BabWidgetProvider.CAFE_LIST, cafeList);
            context.startService(fetchIntent);
        }
        if (intent.getAction().equals(DATA_FETCHED)) {
            Log.e("Receive", "Success");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.e("Receive", Integer.toString(appWidgetId));
            ArrayList<String> cafeMenuList = intent.getStringArrayListExtra(BabWidgetProvider.CAFE_MENU_LIST);
            Log.e("Receive", cafeMenuList.get(0));
            RemoteViews remoteViews = updateWidgetListView(context, cafeMenuList, appWidgetId);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(appWidgetId, null);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            BabWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    /*
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = BabWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bab_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    */
}


