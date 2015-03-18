package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.DownloadingRetryDialog;
import com.wafflestudio.siksha.dialog.ProgressDialog;
import com.wafflestudio.siksha.service.DownloadingJson;
import com.wafflestudio.siksha.service.DownloadingJsonReceiver;

import java.util.ArrayList;
import java.util.List;

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
      classifyMenus();
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

  private void setReceiverCallBack() {
    downloadingJsonReceiver.setOnCompleteDownloadListener(new DownloadingJsonReceiver.OnCompleteDownloadListener() {
      @Override
      public void onComplete() {
        forms = new ParsingJson(context).getParsedForms();
        classifyMenus();
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

  private void classifyMenus() {
    List<RestaurantClassifiedForm> breakfastForms = new ArrayList<RestaurantClassifiedForm>();
    List<RestaurantClassifiedForm> lunchForms = new ArrayList<RestaurantClassifiedForm>();
    List<RestaurantClassifiedForm> dinnerForms = new ArrayList<RestaurantClassifiedForm>();

    for (RestaurantCrawlingForm form : forms) {
      RestaurantClassifiedForm breakfastMenuForm = new RestaurantClassifiedForm();
      RestaurantClassifiedForm lunchMenuForm = new RestaurantClassifiedForm();
      RestaurantClassifiedForm dinnerMenuForm = new RestaurantClassifiedForm();

      List<RestaurantCrawlingForm.MenuInfo> breakfastMenus = new ArrayList<RestaurantCrawlingForm.MenuInfo>();
      List<RestaurantCrawlingForm.MenuInfo> lunchMenus = new ArrayList<RestaurantCrawlingForm.MenuInfo>();
      List<RestaurantCrawlingForm.MenuInfo> dinnerMenus = new ArrayList<RestaurantCrawlingForm.MenuInfo>();

      for (RestaurantCrawlingForm.MenuInfo menu : form.menus) {
        if (menu.time.equals("breakfast"))
          breakfastMenus.add(menu);
        else if (menu.time.equals("lunch"))
          lunchMenus.add(menu);
        else if (menu.time.equals("dinner"))
          dinnerMenus.add(menu);
      }

      breakfastMenuForm.restaurant = form.restaurant;
      lunchMenuForm.restaurant = form.restaurant;
      dinnerMenuForm.restaurant = form.restaurant;

      breakfastMenuForm.menus = breakfastMenus;
      lunchMenuForm.menus = lunchMenus;
      dinnerMenuForm.menus = dinnerMenus;

      breakfastForms.add(breakfastMenuForm);
      lunchForms.add(lunchMenuForm);
      dinnerForms.add(dinnerMenuForm);
    }

    List<AdapterUtil.ExpandableListAdapter> adapters = new ArrayList<AdapterUtil.ExpandableListAdapter>();
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, breakfastForms, 0));
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, lunchForms, 1));
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, dinnerForms, 2));

    viewPager.setAdapter(new AdapterUtil.ViewPagerAdapter(context, adapters));
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