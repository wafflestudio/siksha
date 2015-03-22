package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.DownloadingRetryDialog;
import com.wafflestudio.siksha.dialog.ProgressDialog;
import com.wafflestudio.siksha.page.BreakfastPage;
import com.wafflestudio.siksha.page.DinnerPage;
import com.wafflestudio.siksha.page.LunchPage;
import com.wafflestudio.siksha.service.DownloadingJson;
import com.wafflestudio.siksha.service.DownloadingJsonReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadingMenuFromJson {
  private Context context;
  private DownloadingJsonReceiver downloadingJsonReceiver;

  private ViewPager viewPager;
  private ProgressDialog progressDialog;

  public RestaurantCrawlingForm[] forms;

  public LoadingMenuFromJson(Context context, DownloadingJsonReceiver downloadingJsonReceiver, ViewPager viewPager) {
    this.context = context;
    this.downloadingJsonReceiver = downloadingJsonReceiver;
    this.viewPager = viewPager;
  }

  public void initSetting() {
    if (DownloadingJson.isJsonUpdated(context)) {
      Log.d("is_json_updated", "true");

      forms = new ParsingJson(context).getParsedForms();

      RestaurantInfoUtil restaurantInfoUtil = RestaurantInfoUtil.getInstance();
      RestaurantSequencer restaurantSequencer = RestaurantSequencer.getInstance();
      restaurantInfoUtil.setMenuMap(forms);
      restaurantSequencer.setMenuList(restaurantInfoUtil.breakfastMenuMap, restaurantInfoUtil.lunchMenuMap, restaurantInfoUtil.dinnerMenuMap);

      setAdapters();
      setInitialPage();
    }
    else {
      Log.d("is_json_updated", "false");

      if (!NetworkUtil.getInstance().isOnline())
        new DownloadingRetryDialog(context, this).show();
      else
        startDownloadingService(context);
    }
  }

  private void setAdapters() {
    RestaurantSequencer restaurantSequencer = RestaurantSequencer.getInstance();

    List<AdapterUtil.ExpandableListAdapter> adapters = new ArrayList<AdapterUtil.ExpandableListAdapter>();
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, restaurantSequencer.breakfastMenuList, 0));
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, restaurantSequencer.lunchMenuList, 1));
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, restaurantSequencer.dinnerMenuList, 2));

    viewPager.setAdapter(new AdapterUtil.ViewPagerAdapter(context, adapters));
  }

  private void setReceiverCallBack() {
    downloadingJsonReceiver.setOnCompleteDownloadListener(new DownloadingJsonReceiver.OnCompleteDownloadListener() {
      @Override
      public void onComplete() {
        forms = new ParsingJson(context).getParsedForms();

        RestaurantInfoUtil restaurantInfoUtil = RestaurantInfoUtil.getInstance();
        restaurantInfoUtil.setMenuMap(forms);
        RestaurantSequencer.getInstance().setMenuList(restaurantInfoUtil.breakfastMenuMap, restaurantInfoUtil.lunchMenuMap, restaurantInfoUtil.dinnerMenuMap);

        setAdapters();
        setInitialPage();

        if (progressDialog != null && progressDialog.isShowing())
          progressDialog.quitShowing();
      }

      @Override
      public void onFail() {
        new DownloadingRetryDialog(context, LoadingMenuFromJson.this).show();
      }
    });
  }

  public void startDownloadingService(final Context context) {
    setReceiverCallBack();

    progressDialog = new ProgressDialog(context, context.getString(R.string.downloading_message));
    progressDialog.setCancelable(false);
    progressDialog.startShowing();

    Intent intent = new Intent(context, DownloadingJson.class);
    intent.setAction(DownloadingJsonReceiver.ACTION_CURRENT_DOWNLOAD);
    context.startService(intent);
  }

  private void setInitialPage() {
    int time = CalendarUtil.getCurrentHour();

    if (time >= 0 && time <= 9)
      viewPager.setCurrentItem(0);
    else if (time >= 10 && time <= 15)
      viewPager.setCurrentItem(1);
    else
      viewPager.setCurrentItem(2);
  }
}