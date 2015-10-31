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
import com.wafflestudio.siksha.form.MenuJSON;
import com.wafflestudio.siksha.service.JSONDownloadReceiver;
import com.wafflestudio.siksha.service.JSONDownloader;
import com.wafflestudio.siksha.util.AppData;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.JSONParser;

import java.util.Iterator;
import java.util.Set;

public class WidgetProvider extends AppWidgetProvider {
    public static final String STATE_CONFIGURATION_FINISHED = "com.wafflestudio.siksha.widget.STATE_CONFIGURATION_FINISHED";
    public static final String STATE_DATA_ALREADY_UPDATED = "com.wafflestudio.siksha.widget.STATE_DATA_ALREADY_UPDATED";
    public static final String STATE_NEW_DATA_FETCHED = "com.wafflestudio.siksha.widget.STATE_NEW_DATA_FETCHED";
    public static final String STATE_WIDGET_REFRESHED = "com.wafflestudio.siksha.widget.STATE_WIDGET_REFRESHED";

    public static final int randomNumber = 50;

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (!JSONDownloader.isJSONUpdated(context)) {
            new JSONDownloader(context, JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD).start();
            return;
        }

        for (int appWidgetID : appWidgetIds) {
            if (WidgetConfigureActivity.isValidAppWidgetID(context, appWidgetID)) {
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetID, true);
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
            }
        }
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetID, boolean success) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
        Intent remoteIntent = new Intent(context, WidgetRemoteService.class);
        remoteIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        remoteIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetID + randomNumber), null));

        if (success) {
            remoteViews.setRemoteAdapter(R.id.widget_provider_list_view, remoteIntent);
            remoteViews.setEmptyView(R.id.widget_provider_list_view, R.id.widget_provider_empty_view);
            remoteViews.setTextViewText(R.id.widget_provider_date_view, Date.getDate() + Date.getTimeSlot(Date.getTimeSlotIndex()));
        } else {
            remoteViews.setEmptyView(R.id.widget_provider_list_view, R.id.widget_provider_fetch_failure_view);
            remoteViews.setTextViewText(R.id.widget_provider_date_view, Date.getDate() + Date.getTimeSlot(Date.getTimeSlotIndex()));
        }

        Intent refreshIntent = new Intent(context, WidgetProvider.class);
        refreshIntent.setAction(STATE_WIDGET_REFRESHED);
        refreshIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetID + randomNumber), null));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_provider_refresh_button, pendingIntent);

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(STATE_CONFIGURATION_FINISHED)) {
            Log.d(intent.getAction(), "Hi");
            int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (!JSONDownloader.isJSONUpdated(context)) {
                new JSONDownloader(context, JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD).start();
                return;
            }

            if (WidgetConfigureActivity.isValidAppWidgetID(context, appWidgetID)) {
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetID, true);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
            }
        } else if (intent.getAction().equals(STATE_DATA_ALREADY_UPDATED)) {
            AppData.getInstance().setMenuDictionaries(JSONParser.parseJSONFile(context, MenuJSON.class).data);

            Set<String> appWidgetIDSet = WidgetConfigureActivity.getAllAppWidgetIDs(context);
            Iterator<String> iterator = appWidgetIDSet.iterator();

            while (iterator.hasNext()) {
                int appWidgetID = Integer.valueOf(iterator.next());
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetID, false);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
            }
            Log.d(intent.getAction(), "Hi");
        } else if (intent.getAction().equals(STATE_NEW_DATA_FETCHED)) {
            AppData.getInstance().setMenuDictionaries(JSONParser.parseJSONFile(context, MenuJSON.class).data);

            Set<String> appWidgetIDSet = WidgetConfigureActivity.getAllAppWidgetIDs(context);
            Iterator<String> iterator = appWidgetIDSet.iterator();

            while (iterator.hasNext()) {
                int appWidgetID = Integer.valueOf(iterator.next());

                RemoteViews remoteViews = updateWidgetListView(context, appWidgetID, true);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
            }
            Log.d(intent.getAction(), "Hi");
        } else if (intent.getAction().equals(STATE_WIDGET_REFRESHED)) {
            new JSONDownloader(context, JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD).start();
            Log.d(intent.getAction(), "Hi");
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIDs) {
        super.onDeleted(context, appWidgetIDs);

        for (int appWidgetID : appWidgetIDs) {
            WidgetConfigureActivity.removeAppWidgetID(context, appWidgetID);
        }
    }
}