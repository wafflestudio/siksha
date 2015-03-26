package com.wafflestudio.siksha.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.ParsingJson;
import com.wafflestudio.siksha.util.RestaurantCrawlingForm;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Set;

public class WidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;

    private ArrayList<String> cafeList;
    private ArrayList<RestaurantCrawlingForm> cafeMenuList;

    private int appWidgetId;
    private int hour;
    String[] restaurants;

    public WidgetListViewFactory(Context context, Intent intent) {
        this.context = context;

        appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - BabWidgetProvider.randomNumber;
        Set<String> input = BabWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId);

        restaurants = context.getResources().getStringArray(R.array.restaurants);

        cafeList = new ArrayList<String>();
        for (int i = 0; i < restaurants.length; i++) {
            if (input.contains(restaurants[i]))
                cafeList.add(restaurants[i]);
        }

        cafeMenuList = new ArrayList<RestaurantCrawlingForm>();
        RestaurantCrawlingForm[] forms = new ParsingJson(context).getParsedForms();

        if (forms != null) {
            for (int i = 0; i < cafeList.size(); i++) {
                for (int j = 0; j < forms.length; j++) {
                    if (cafeList.get(i).equals(forms[j].restaurant)) {
                        cafeMenuList.add(forms[j]);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        return cafeMenuList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.bab_widget_restaurant_list_row);
        RestaurantCrawlingForm item = cafeMenuList.get(position);

        remoteView.setTextViewText(R.id.restaurant_view_widget, item.restaurant);
        remoteView.removeAllViews(R.id.menu_list_widget);
        boolean isEmpty = true;
        remoteView.setViewVisibility(R.id.widget_restaurant_empty_view, View.GONE);

        String time;

        if (hour >= 0 && hour <= 9) {
            if (SharedPreferenceUtil.loadValueOfBoolean(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_BREAKFAST_KEY + appWidgetId))
                time = "breakfast";
            else
                time = "lunch";
        }
        else if (hour >= 10 && hour <= 14)
            time = "lunch";
        else
            time = "dinner";

        for (int i = 0; i < item.menus.length; i++) {
            if (item.menus[i].time.equals(time)) {
                RemoteViews child = new RemoteViews(context.getPackageName(), R.layout.bab_widget_menu_list_row);
                child.setTextViewText(R.id.menu_price_widget, item.menus[i].price);
                child.setTextViewText(R.id.menu_name_widget, item.menus[i].name);
                remoteView.addView(R.id.menu_list_widget, child);
                isEmpty = false;
            }
        }
        if (isEmpty)
            remoteView.setViewVisibility(R.id.widget_restaurant_empty_view, View.VISIBLE);
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
        hour = CalendarUtil.getCurrentHour();
        RestaurantCrawlingForm[] forms = new ParsingJson(context).getParsedForms();
        if (forms != null) {
            cafeMenuList = new ArrayList<RestaurantCrawlingForm>();
            for (int i = 0; i < cafeList.size(); i++) {
                for (int j = 0; j < forms.length; j++) {
                    if (cafeList.get(i).equals(forms[j].restaurant)) {
                        cafeMenuList.add(forms[j]);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() { }
}