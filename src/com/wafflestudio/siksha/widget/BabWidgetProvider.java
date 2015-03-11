package com.wafflestudio.siksha.widget;

import android.app.PendingIntent;
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
    public static final String WIDGET_REFRESH = "com.wafflestudio.siksha.WIDGET_REFRESH";
    public static final int randomNumber = 50;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("WidgetOnUpdate", "aa");
        String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, "json_date");
        Log.d("recordedDate", recordedDate);

        if (recordedDate.equals(CalendarUtil.getCurrentDate())) {
            final int N = appWidgetIds.length;
            for (int i = 0; i < N; i++) {
                Log.d("WidgetOnUpdate", "WidgetId" + Integer.toString(appWidgetIds[i]) + Boolean.toString(BabWidgetProviderConfigureActivity.isValidId(context, appWidgetIds[i])));
                if (BabWidgetProviderConfigureActivity.isValidId(context, appWidgetIds[i])) {
                    RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
                    appWidgetManager.updateAppWidget(appWidgetIds[i], null);
                    appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.listViewWidget);
                }
            }
        }
        else {
            context.startService(new Intent(context, DownloadingJson.class));
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        Log.d("UpdateWidgetListView", Integer.toString(appWidgetId));
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.bab_widget_provider);
        Intent remoteIntent = new Intent(context, WidgetRemoteService.class);
        remoteIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + randomNumber), null));
        remoteViews.setRemoteAdapter(R.id.widget_list_view, remoteIntent);
        // setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.emptyViewWidget);

        int hour = CalendarUtil.getCurrentHour();
        String time;
        if (hour >= 1 && hour <= 9) {
            time = "아침";
        }
        else if (hour >= 10 && hour <= 15) {
            time = "점심";
        }
        else {
            time = "저녁";
        }
        remoteViews.setTextViewText(R.id.dateViewWidget, SharedPreferenceUtil.load(context, SharedPreferenceUtil.PREF_NAME, "json_date") + time);

        Intent refreshIntent = new Intent(context, BabWidgetProvider.class);
        refreshIntent.setAction(WIDGET_REFRESH);
        refreshIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + randomNumber), null));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.dateViewWidget, pendingIntent);

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(CONFIGURATION_FINISHED)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d("WidgetOnReceive_Config_Finished", Integer.toString(appWidgetId));

            String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, "json_date");
            Log.d("recordedDate", recordedDate);
            if (recordedDate.equals(CalendarUtil.getCurrentDate())) {
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(appWidgetId, null);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }

            else {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
                appWidgetManager.updateAppWidget(appWidgetId, null);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                context.startService(new Intent(context, DownloadingJson.class));
            }
        }

        if (intent.getAction().equals(DATA_FETCHED)) {
            Log.d("WidgetOnReceive_DATA_FETCHED", "Broadcast_Received");
            Set<String> idSet = BabWidgetProviderConfigureActivity.getAllWidgetIds(context);
            Iterator<String> iterator = idSet.iterator();

            while (iterator.hasNext()) {
                int appWidgetId = Integer.valueOf(iterator.next());
                Log.d("WidgetOnReceive_DATA_FETCHED", Integer.toString(appWidgetId));
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
                appWidgetManager.updateAppWidget(appWidgetId, null);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listViewWidget);
            }
        }

        if (intent.getAction().equals(WIDGET_REFRESH)) {
            int appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - BabWidgetProvider.randomNumber;
            Log.d("WidgetOnReceive_Widget_Refresh", Integer.toString(appWidgetId));

<<<<<<< HEAD
            String recordedDate = SharedPreferenceUtil.load(context, SharedPreferenceUtil.PREF_NAME, "json_date");
            Log.e("WidgetOnReceive_Widget_Refresh", recordedDate);
            Log.e("WidgetOnReceive_Widget_Refresh", CalendarUtil.getCurrentDate());
=======
            String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, "json_date");
            Log.d("WidgetOnReceive_Widget_Refresh", recordedDate);
            Log.d("WidgetOnReceive_Widget_Refresh", CalendarUtil.getCurrentDate());
>>>>>>> 2df9c4c... Log change
            if (recordedDate.equals(CalendarUtil.getCurrentDate())) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
                appWidgetManager.updateAppWidget(appWidgetId, null);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listViewWidget);
            }

            else {
                context.startService(new Intent(context, DownloadingJson.class));
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            Log.d("WidgetOnDeleted", Integer.toString(appWidgetIds[i]));
            BabWidgetProviderConfigureActivity.removeWidgetId(context, appWidgetIds[i]);
            BabWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }
}


