package com.wafflestudio.siksha.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
        String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON);

        if (recordedDate.equals(CalendarUtil.getCurrentDate())) {
            final int N = appWidgetIds.length;
            for (int i = 0; i < N; i++) {
                if (BabWidgetProviderConfigureActivity.isValidId(context, appWidgetIds[i])) {
                    RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i], true);
                    appWidgetManager.updateAppWidget(appWidgetIds[i], null);
                    appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widget_list_view);
                }
            }
        }
        else {
            final int N = appWidgetIds.length;
            for (int i = 0; i < N; i++) {
                if (BabWidgetProviderConfigureActivity.isValidId(context, appWidgetIds[i])) {
                    context.startService(new Intent(context, DownloadingJson.class).putExtra("from_widget_user", false));
                    break;
                }
            }
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId, boolean isSuccess) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.bab_widget_provider);
        Intent remoteIntent = new Intent(context, WidgetRemoteService.class);
        remoteIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + randomNumber), null));

        int hour = CalendarUtil.getCurrentHour();
        String time;

        if (hour >= 0 && hour <= 9) {
            if (SharedPreferenceUtil.loadValueOfBoolean(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_BREAKFAST_KEY + appWidgetId))
                time = "아침";
            else
                time = "점심";
        }
        else if (hour >= 10 && hour <= 14)
            time = "점심";
        else
            time = "저녁";

        if (isSuccess) {
            remoteViews.setRemoteAdapter(R.id.widget_list_view, remoteIntent);
            remoteViews.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);
            remoteViews.setTextViewText(R.id.date_view_widget, SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON).substring(5) + " " + time);
        }
        else {
            remoteViews.setEmptyView(R.id.widget_list_view, R.id.widget_download_fail_view);
            remoteViews.setTextViewText(R.id.date_view_widget, CalendarUtil.getCurrentDate().substring(5) + " " + time);
        }

        Intent refreshIntent = new Intent(context, BabWidgetProvider.class);
        refreshIntent.setAction(WIDGET_REFRESH);
        refreshIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + randomNumber), null));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_title_bar, pendingIntent);

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(CONFIGURATION_FINISHED)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON);

            if (recordedDate.equals(CalendarUtil.getCurrentDate())) {
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId, true);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(appWidgetId, null);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            }
            else
                context.startService(new Intent(context, DownloadingJson.class).putExtra("from_widget_user", true));
        }

        if (intent.getAction().equals(DATA_FETCHED)) {
            boolean isSuccess = intent.getBooleanExtra("is_success", false);
            boolean fromWidgetUser = intent.getBooleanExtra("from_widget_user", false);

            if (isSuccess) {
                Set<String> idSet = BabWidgetProviderConfigureActivity.getAllWidgetIds(context);
                Iterator<String> iterator = idSet.iterator();

                while (iterator.hasNext()) {
                    int appWidgetId = Integer.valueOf(iterator.next());
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                    RemoteViews remoteViews = updateWidgetListView(context, appWidgetId, isSuccess);
                    appWidgetManager.updateAppWidget(appWidgetId, null);
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
                }
            }
            else {
                if (fromWidgetUser) {
                    Set<String> idSet = BabWidgetProviderConfigureActivity.getAllWidgetIds(context);
                    Iterator<String> iterator = idSet.iterator();

                    while (iterator.hasNext()) {
                        int appWidgetId = Integer.valueOf(iterator.next());
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                        RemoteViews remoteViews = updateWidgetListView(context, appWidgetId, isSuccess);
                        appWidgetManager.updateAppWidget(appWidgetId, null);
                        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
                    }
                }
            }
        }

        if (intent.getAction().equals(WIDGET_REFRESH)) {
            int appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - BabWidgetProvider.randomNumber;

            String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON);

            if (recordedDate.equals(CalendarUtil.getCurrentDate())) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                RemoteViews remoteViews = updateWidgetListView(context, appWidgetId, true);
                appWidgetManager.updateAppWidget(appWidgetId, null);
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
            }
            else
                context.startService(new Intent(context, DownloadingJson.class).putExtra("from_widget_user", true));
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
}