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
      AdapterUtil.ExpandableListAdapter adapter = new AdapterUtil.ExpandableListAdapter(context, forms);
      viewPager.setAdapter(new AdapterUtil.ViewPagerAdapter(context, adapter));
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
        AdapterUtil.ExpandableListAdapter adapter = new AdapterUtil.ExpandableListAdapter(context, forms);
        viewPager.setAdapter(new AdapterUtil.ViewPagerAdapter(context, adapter));

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
}