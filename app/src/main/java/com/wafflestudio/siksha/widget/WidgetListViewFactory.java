package com.wafflestudio.siksha.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.form.Food;
import com.wafflestudio.siksha.form.response.Menu;
import com.wafflestudio.siksha.form.Restaurant;
import com.wafflestudio.siksha.util.AppData;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.JSONParser;

import java.util.List;

public class WidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appWidgetID;
    private List<Restaurant> items;

    public WidgetListViewFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        AppData.getInstance().setMenuDictionaries(JSONParser.parseJSONFile(context, Menu.class).data);
        populateItems();
    }

    @Override
    public void onDataSetChanged() {
        AppData.getInstance().setMenuDictionaries(JSONParser.parseJSONFile(context, Menu.class).data);
        populateItems();
    }

    private void populateItems() {
        int index = Date.getTimeSlotIndexForWidget(context, appWidgetID);

        switch (index) {
            case 0:
                items = AppData.getInstance().getWidgetMenuList(context, AppData.getInstance().breakfastMenuDictionary, appWidgetID);
                break;
            case 1:
                items = AppData.getInstance().getWidgetMenuList(context, AppData.getInstance().lunchMenuDictionary, appWidgetID);
                break;
            case 2:
                items = AppData.getInstance().getWidgetMenuList(context, AppData.getInstance().dinnerMenuDictionary, appWidgetID);
                break;
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Restaurant item = items.get(position);

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_view_item);
        remoteViews.setTextViewText(R.id.widget_list_view_name_view, item.name);
        remoteViews.removeAllViews(R.id.widget_not_empty_menu_view);

        if (item.foods.size() != 0) {
            for (int i = 0; i < item.foods.size(); i++) {
                Food food = item.foods.get(i);
                RemoteViews nestedView = new RemoteViews(context.getPackageName(), R.layout.widget_menu_item);
                nestedView.setTextViewText(R.id.widget_menu_price_view, food.price);
                nestedView.setTextViewText(R.id.widget_menu_name_view, food.name);
                remoteViews.addView(R.id.widget_not_empty_menu_view, nestedView);
            }

            remoteViews.setViewVisibility(R.id.widget_empty_menu_view, View.GONE);
            remoteViews.setViewVisibility(R.id.widget_not_empty_menu_view, View.VISIBLE);
        }
        else {
            remoteViews.setViewVisibility(R.id.widget_empty_menu_view, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_not_empty_menu_view, View.GONE);
        }

        return remoteViews;
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
    public void onDestroy() {
    }
}