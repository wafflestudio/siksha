package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.DownloadingRetryDialog;
import com.wafflestudio.siksha.dialog.NotifyWidgetDialog;
import com.wafflestudio.siksha.dialog.ProgressDialog;
import com.wafflestudio.siksha.service.DownloadingJson;
import com.wafflestudio.siksha.service.DownloadingJsonReceiver;

import java.util.ArrayList;
import java.util.List;

public class InitialLoadingTask {
  public MenuCrawlingForm[] forms;
  private Context context;
  private DownloadingJsonReceiver downloadingJsonReceiver;
  private ViewPager viewPager;
  private ProgressDialog progressDialog;

  public InitialLoadingTask(Context context, DownloadingJsonReceiver downloadingJsonReceiver, ViewPager viewPager) {
    this.context = context;
    this.downloadingJsonReceiver = downloadingJsonReceiver;
    this.viewPager = viewPager;
  }

  public void initialize() {
    int option = DownloadingJson.getDownloadOption();
    String downloadDate = DownloadingJson.getDownloadDate(option);

    if (DownloadingJson.isJsonUpdated(context, downloadDate)) {
      Log.d("is_json_updated", "true");

      if (!DownloadingJson.isVetDataUpdated(context, downloadDate) && CalendarUtil.isVetDataUpdateTime()) {
        if (!NetworkUtil.getInstance().isOnline())
          new DownloadingRetryDialog(context, this, option, downloadDate).show();
        else
          startDownloadService(context, option, downloadDate);
      } else {
        forms = new ParsingJson(context).getParsedForms();
        RestaurantInfoUtil.getInstance().setMenuMap(forms);
        Sequencer.getInstance().setMenuListOnSequence();

        setAdapters();
        setInitialPage();

        notifyWidget();
      }
    } else {
      Log.d("is_json_updated", "false");

      if (!NetworkUtil.getInstance().isOnline())
        new DownloadingRetryDialog(context, this, option, downloadDate).show();
      else
        startDownloadService(context, option, downloadDate);
    }
  }

  public void reInitialize() {
    if (!NetworkUtil.getInstance().isOnline())
      Toast.makeText(context, context.getString(R.string.downloading_network_state), Toast.LENGTH_SHORT).show();
    else {
      int option = DownloadingJson.getDownloadOption();
      String downloadDate = DownloadingJson.getDownloadDate(option);
      startDownloadService(context, option, downloadDate);
    }
  }

  private void setAdapters() {
    Sequencer sequencer = Sequencer.getInstance();

    List<AdapterUtil.ExpandableListAdapter> adapters = new ArrayList<AdapterUtil.ExpandableListAdapter>();
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, sequencer.breakfastMenuList, 0));
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, sequencer.lunchMenuList, 1));
    adapters.add(new AdapterUtil.ExpandableListAdapter(context, sequencer.dinnerMenuList, 2));

    viewPager.setAdapter(new AdapterUtil.ViewPagerAdapter(context, adapters));
  }

  private void setReceiverCallBack() {
    downloadingJsonReceiver.setOnCompleteDownloadListener(new DownloadingJsonReceiver.OnCompleteDownloadListener() {
      @Override
      public void onComplete() {
        forms = new ParsingJson(context).getParsedForms();
        RestaurantInfoUtil.getInstance().setMenuMap(forms);
        Sequencer.getInstance().setMenuListOnSequence();

        setAdapters();
        setInitialPage();

        notifyWidget();

        if (progressDialog != null && progressDialog.isShowing())
          progressDialog.quitShowing();
      }

      @Override
      public void onFail(int option, String downloadDate) {
        if (progressDialog != null && progressDialog.isShowing())
          progressDialog.quitShowing();

        new DownloadingRetryDialog(context, InitialLoadingTask.this, option, downloadDate).show();
      }
    });
  }

  public void startDownloadService(final Context context, int option, String downloadDate) {
    setReceiverCallBack();

    progressDialog = new ProgressDialog(context, context.getString(R.string.downloading_message));
    progressDialog.setCancelable(false);
    progressDialog.startShowing();

    Intent intent = new Intent(context, DownloadingJson.class);
    intent.setAction(DownloadingJsonReceiver.ACTION_CURRENT_DOWNLOAD);
    intent.putExtra(DownloadingJson.KEY_OPTION, option);
    intent.putExtra(DownloadingJson.KEY_DATE, downloadDate);
    context.startService(intent);
  }

  private void setInitialPage() {
    int index = ((MainActivity) context).getPageIndexOnHour();

    ((MainActivity) context).setStatusBarBackgroundColor(index);
    ((MainActivity) context).setToolBarBackgroundColor(index);
    viewPager.setCurrentItem(index);
  }

  private void notifyWidget() {
    boolean isNoticed = SharedPreferenceUtil.loadValueOfBoolean(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_NOTIFY_WIDGET);

    if (!isNoticed) {
      new NotifyWidgetDialog(context).show();
      SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_NOTIFY_WIDGET, true);
    }
  }
}