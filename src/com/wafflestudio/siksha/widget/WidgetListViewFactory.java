package com.wafflestudio.siksha.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.ParsingJson;
import com.wafflestudio.siksha.util.RestaurantCrawlingForm;

import java.util.ArrayList;

public class WidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<String> cafeList;
    private ArrayList<RestaurantCrawlingForm> cafeMenuList;
    private Context context = null;
    private int appWidgetId;
    private int hour;
    public final static String[] jikYoungCafes = {"학생회관식당", "3식당", "기숙사식당", "자하연식당", "302동식당", "동원관식당", "감골식당"};

    public WidgetListViewFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - BabWidgetProvider.randomNumber;

        int input = Integer.valueOf(BabWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId));
        Log.e("WidgetListViewFactory", Integer.toString(input));
        cafeList = new ArrayList<String>();
        if (input % 2 == 1) {
            cafeList.add(jikYoungCafes[0]);
        }
        for (int j = 1; j < 7; j++) {
            input = input / 2;
            if (input % 2 == 1)
                cafeList.add(jikYoungCafes[j]);
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

        remoteView.setTextViewText(R.id.restaurantViewWidget, item.restaurant);
        for (int i = 0; i < item.menus.length; i++) {
            if (hour >= 1 && hour <= 9) {
                if (item.menus[i].time.equals("breakfast")) {
                    RemoteViews child = new RemoteViews(context.getPackageName(), R.layout.bab_widget_menu_list_row);
                    child.setTextViewText(R.id.menuPriceWidget, Integer.toString(item.menus[i].price));
                    child.setTextViewText(R.id.menuNameWidget, item.menus[i].name);
                    remoteView.addView(R.id.menuListWidget, child);
                }
            }
            else if (hour >= 10 && hour <= 15) {
                if (item.menus[i].time.equals("lunch")) {
                    RemoteViews child = new RemoteViews(context.getPackageName(), R.layout.bab_widget_menu_list_row);
                    child.setTextViewText(R.id.menuPriceWidget, Integer.toString(item.menus[i].price));
                    child.setTextViewText(R.id.menuNameWidget, item.menus[i].name);
                    remoteView.addView(R.id.menuListWidget, child);
                }
            }
            else {
                if (item.menus[i].time.equals("dinner")) {
                    RemoteViews child = new RemoteViews(context.getPackageName(), R.layout.bab_widget_menu_list_row);
                    child.setTextViewText(R.id.menuPriceWidget, Integer.toString(item.menus[i].price));
                    child.setTextViewText(R.id.menuNameWidget, item.menus[i].name);
                    remoteView.addView(R.id.menuListWidget, child);
                }
            }
        }
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
        Log.e("WidgetListViewFactory", "onDataSetChanged");
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
    public void onDestroy() {
    }
}