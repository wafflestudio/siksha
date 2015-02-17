package com.wafflestudio.siksha.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import com.wafflestudio.siksha.BabWidgetProvider;
import com.wafflestudio.siksha.R;

public class WidgetRemoteService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.e("RemoteService", Integer.toString(appWidgetId));
        return (new WidgetListViewFactory(this.getApplicationContext(), intent));
    }
}

class WidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<String> cafeIncludeMenus = new ArrayList<String>();
    private Context context = null;
    private int appWidgetId;

    public WidgetListViewFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - BabWidgetProvider.randomNumber;
        ArrayList<String> cafeMenuList = intent.getStringArrayListExtra(BabWidgetProvider.CAFE_MENU_LIST);
        if (cafeMenuList != null) {
            cafeIncludeMenus = cafeMenuList;
            Log.e("Factory", cafeMenuList.get(0));
        }
        else {
            cafeIncludeMenus = new ArrayList<String>();
        }
    }

    @Override
    public int getCount() {
        return cafeIncludeMenus.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.bab_widget_list_row);
        String item = cafeIncludeMenus.get(position);
        String[] itemList = item.split("!");
        remoteView.setTextViewText(R.id.heading, itemList[0]);
        remoteView.setTextViewText(R.id.content, itemList[1]);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }
}
