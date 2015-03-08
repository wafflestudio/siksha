package com.wafflestudio.siksha.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.RestaurantCrawlingForm;

public class WidgetRemoteService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.e("RemoteService", Integer.toString(appWidgetId));
        return (new WidgetListViewFactory(this.getApplicationContext(), intent));
    }
}

class WidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<String> cafeList;
    private ArrayList<RestaurantCrawlingForm> cafeMenuList;
    private Context context = null;
    private int appWidgetId;
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

        RestaurantCrawlingForm[] forms = new LoadingMenuFromJson.ParsingJson(context).getParsedForms();
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
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.bab_widget_list_row);
        RestaurantCrawlingForm item = cafeMenuList.get(position);
        String content = "";
        for (int i = 0; i < item.menus.length; i++) {
            content = content + item.menus[i].name + item.menus[i].time + item.menus[i].price;
        }
        remoteView.setTextViewText(R.id.heading, item.restaurant);
        remoteView.setTextViewText(R.id.content, content);

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
