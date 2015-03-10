package com.wafflestudio.siksha.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.ParsingJson;
import com.wafflestudio.siksha.util.RestaurantCrawlingForm;

import java.util.ArrayList;

public class WidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {
  private Context context = null;

  private ArrayList<String> restaurantList;
  private ArrayList<RestaurantCrawlingForm> restaurantMenuList;

  private int appWidgetId;
  public final static String[] jikYoungRestaurants = { "학생회관식당", "3식당", "기숙사식당", "자하연식당", "302동식당", "동원관식당", "감골식당" };

  public WidgetListViewFactory(Context context, Intent intent) {
    this.context = context;
    appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - BabWidgetProvider.randomNumber;
    restaurantList = new ArrayList<String>();

    int input = Integer.valueOf(BabWidgetProviderConfigureActivity.loadTitlePref(context, appWidgetId));
    Log.e("WidgetListViewFactory", Integer.toString(input));

    if (input % 2 == 1) {
      restaurantList.add(jikYoungRestaurants[0]);
    }
    for (int i = 1; i < 7; i++) {
      input = input / 2;
      if (input % 2 == 1)
        restaurantList.add(jikYoungRestaurants[i]);
    }

    RestaurantCrawlingForm[] forms = new ParsingJson(context).getParsedForms();
    restaurantMenuList = new ArrayList<RestaurantCrawlingForm>();
    for (int i = 0; i < restaurantList.size(); i++) {
      for (int j = 0; j < forms.length; j++) {
        if (restaurantList.get(i).equals(forms[j].restaurant)) {
          restaurantMenuList.add(forms[j]);
          break;
        }
      }
    }
  }

  @Override
  public int getCount() {
    return restaurantMenuList.size();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public RemoteViews getViewAt(int position) {
    final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.bab_widget_list_row);
    RestaurantCrawlingForm item = restaurantMenuList.get(position);
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
  public void onCreate() { }

  @Override
  public void onDataSetChanged() { }

  @Override
  public void onDestroy() { }
}
