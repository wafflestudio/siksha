package com.wafflestudio.siksha.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.form.response.Menu;
import com.wafflestudio.siksha.service.JSONDownloadReceiver;
import com.wafflestudio.siksha.service.JSONDownloader;
import com.wafflestudio.siksha.util.AppDataManager;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.NetworkChecker;
import com.wafflestudio.siksha.util.JSONParser;

import java.util.Iterator;
import java.util.Set;

public class WidgetProvider extends AppWidgetProvider {
    public static final String STATE_CONFIGURATION_FINISHED = "com.wafflestudio.siksha.widget.STATE_CONFIGURATION_FINISHED";
    public static final String STATE_DATA_ALREADY_UPDATED = "com.wafflestudio.siksha.widget.STATE_DATA_ALREADY_UPDATED";
    public static final String STATE_NEW_DATA_FETCHED = "com.wafflestudio.siksha.widget.STATE_NEW_DATA_FETCHED";
    public static final String STATE_UPDATE_FAILURE = "com.wafflestudio.siksha.widget.STATE_UPDATE_FAILURE";
    public static final String STATE_WIDGET_REFRESH = "com.wafflestudio.siksha.widget.STATE_WIDGET_REFRESH";

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("widget", "onUpdate()");

        if (!JSONDownloader.isJSONUpdated(context)) {
            if (NetworkChecker.getInstance().isOnline())
                new JSONDownloader(context, JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD).start();
            else {
                for (int appWidgetID : appWidgetIds) {
                    if (WidgetConfigureActivity.isValidAppWidgetID(context, appWidgetID)) {
                        RemoteViews remoteViews = getExtendedRemoteViews(context, appWidgetID, false);
                        appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
                    }
                }
            }
        } else {
            for (int appWidgetID : appWidgetIds) {
                if (WidgetConfigureActivity.isValidAppWidgetID(context, appWidgetID)) {
                    RemoteViews remoteViews = getExtendedRemoteViews(context, appWidgetID, true);
                    appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
                }
            }
        }
    }

    private RemoteViews getExtendedRemoteViews(Context context, int appWidgetID, boolean isSuccess) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_provider);

        Intent remoteIntent = new Intent(context, WidgetRemoteService.class);
        remoteIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        remoteIntent.setData(Uri.parse(remoteIntent.toUri(Intent.URI_INTENT_SCHEME)));

        if (isSuccess) {
            remoteViews.setRemoteAdapter(R.id.widget_provider_list_view, remoteIntent);
            remoteViews.setEmptyView(R.id.widget_provider_list_view, R.id.widget_provider_empty_view);
            remoteViews.setTextViewText(R.id.widget_provider_date_view, Date.getPrimaryTimestamp(Date.TYPE_CONCISE) + " " + Date.getTimeSlot(Date.getTimeSlotIndexForWidget(context, appWidgetID)));
        } else {
            remoteViews.setEmptyView(R.id.widget_provider_list_view, R.id.widget_provider_fetch_failure_view);
            remoteViews.setTextViewText(R.id.widget_provider_date_view, Date.getPrimaryTimestamp(Date.TYPE_CONCISE) + " " + Date.getTimeSlot(Date.getTimeSlotIndexForWidget(context, appWidgetID)));
        }

        Intent refreshIntent = new Intent(context, WidgetProvider.class);
        refreshIntent.setAction(STATE_WIDGET_REFRESH);
        remoteIntent.setData(Uri.parse(remoteIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.widget_provider_header_view, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.widget_provider_refresh_button, pendingIntent);

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("widget", intent.getAction());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(STATE_CONFIGURATION_FINISHED)) {
            int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            NetworkChecker.getInstance().initialize(context);

            if (!JSONDownloader.isJSONUpdated(context)) {
                if (NetworkChecker.getInstance().isOnline())
                    new JSONDownloader(context, JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD).start();
                else {
                    if (WidgetConfigureActivity.isValidAppWidgetID(context, appWidgetID)) {
                        RemoteViews remoteViews = getExtendedRemoteViews(context, appWidgetID, false);
                        appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
                    }
                }
            } else {
                if (WidgetConfigureActivity.isValidAppWidgetID(context, appWidgetID)) {
                    RemoteViews remoteViews = getExtendedRemoteViews(context, appWidgetID, true);
                    appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
                }
            }
        } else if (intent.getAction().equals(STATE_DATA_ALREADY_UPDATED)) {
            AppDataManager.getInstance().setMenuDictionaries(JSONParser.parseJSONFile(context, Menu.class).data);

            Set<String> appWidgetIDSet = WidgetConfigureActivity.getAllAppWidgetIDs(context);
            Iterator<String> iterator = appWidgetIDSet.iterator();

            while (iterator.hasNext()) {
                int appWidgetID = Integer.valueOf(iterator.next());
                RemoteViews remoteViews = getExtendedRemoteViews(context, appWidgetID, true);
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
            }
        } else if (intent.getAction().equals(STATE_NEW_DATA_FETCHED)) {
            Set<String> appWidgetIDSet = WidgetConfigureActivity.getAllAppWidgetIDs(context);
            Iterator<String> iterator = appWidgetIDSet.iterator();

            while (iterator.hasNext()) {
                int appWidgetID = Integer.valueOf(iterator.next());

                RemoteViews remoteViews = getExtendedRemoteViews(context, appWidgetID, true);
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
            }
        } else if (intent.getAction().equals(STATE_UPDATE_FAILURE)) {
            Set<String> appWidgetIDSet = WidgetConfigureActivity.getAllAppWidgetIDs(context);
            Iterator<String> iterator = appWidgetIDSet.iterator();

            while (iterator.hasNext()) {
                int appWidgetID = Integer.valueOf(iterator.next());

                RemoteViews remoteViews = getExtendedRemoteViews(context, appWidgetID, false);
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
            }
        } else if (intent.getAction().equals(STATE_WIDGET_REFRESH)) {
            int appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            NetworkChecker.getInstance().initialize(context);

            if (NetworkChecker.getInstance().isOnline()) {
                Toast.makeText(context, R.string.now_refreshing, Toast.LENGTH_SHORT).show();
                new JSONDownloader(context, JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD).start();
            }
            else {
                Toast.makeText(context, R.string.check_network_state, Toast.LENGTH_SHORT).show();
                if (WidgetConfigureActivity.isValidAppWidgetID(context, appWidgetID)) {
                    RemoteViews remoteViews = getExtendedRemoteViews(context, appWidgetID, false);
                    appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID, R.id.widget_provider_list_view);
                }
            }
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