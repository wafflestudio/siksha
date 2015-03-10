package com.wafflestudio.siksha.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.service.DownloadingJson;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BabWidgetProviderConfigureActivity BabWidgetProviderConfigureActivity}
 */
public class BabWidgetProvider extends AppWidgetProvider {
    public static final String CONFIGURATION_FINISHED = "com.wafflestudio.siksha.CONFIGURATION_FINISHED";
    public static final String DATA_FETCHED = "com.wafflestudio.siksha.DATA_FETCHED";
    public static final int randomNumber = 50;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("WidgetOnUpdate", "aa");
        String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, "json_date");
        Log.e("recordedDate", recordedDate);

        if (recordedDate.equals(CalendarUtil.getCurrentDate())) {
            final int N = appWidgetIds.length;
            for (int i = 0; i < N; i++) {
                Log.e("WidgetOnUpdate", "WidgetId" + Integer.toString(appWidgetIds[i]) + Boolean.toString(BabWidgetProviderConfigureActivity.isValidId(context, appWidgetIds[i])));
                if (BabWidgetProviderConfigureActivity.isValidId(context, appWidgetIds[i])) {
                    RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
                    appWidgetManager.updateAppWidget(appWidgetIds[i], null);
                    appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
                }
            }
        }
        else {
            context.startService(new Intent(context, DownloadingJson.class));
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.bab_widget_provider);
        Intent remoteIntent = new Intent(context, WidgetRemoteService.class);
        remoteIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + randomNumber), null));
        remoteViews.setRemoteAdapter(R.id.widget_list_view, remoteIntent);
        // setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);
        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(CONFIGURATION_FINISHED)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.e("WidgetOnReceive_Config_Finished", Integer.toString(appWidgetId));

            String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, "json_date");
            Log.e("recordedDate", recordedDate);
            if (recordedDate.equals(CalendarUtil.getCurrentDate())) {
                Log.e("WidgetOnReceive_Config_Finished", "WidgetId" + Integer.toString(appWidgetId) + Boolean.toString(BabWidgetProviderConfigureActivity.isValidId(context, appWidgetId)));
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(appWidgetId, null);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }

            else {
                context.startService(new Intent(context, DownloadingJson.class));
            }
        }

        if (intent.getAction().equals(DATA_FETCHED)) {
            Log.e("WidgetOnReceive_DATA_FETCHED", "Broadcast_Received");
            Set<String> idSet = BabWidgetProviderConfigureActivity.getAllWidgetIds(context);
            Iterator<String> iterator = idSet.iterator();

            while (iterator.hasNext()) {
                int appWidgetId = Integer.valueOf(iterator.next());
                Log.e("WidgetOnReceive_DATA_FETCHED", Integer.toString(appWidgetId));
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(appWidgetId, null);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            BabWidgetProviderConfigureActivity.removeWidgetId(context, appWidgetIds[i]);
            BabWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    /*
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        CharSequence widgetText = BabWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bab_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    */
}


