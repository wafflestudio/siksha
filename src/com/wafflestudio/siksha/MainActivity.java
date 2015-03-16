package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.wafflestudio.siksha.service.DownloadingJsonReceiver;
import com.wafflestudio.siksha.util.AlarmUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.NetworkUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;

public class MainActivity extends Activity {
  private TextView title;
  private TextView appName;
  private ViewPager viewPager;

  private DownloadingJsonReceiver downloadingJsonReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AlarmUtil.registerAlarm(this);
    RestaurantInfoUtil.getInstance().loadingRestaurantInfos();
    NetworkUtil.getInstance().setConnectivityManager(this);
    FontUtil.getInstance().setFontAsset(getAssets());

    setLayout();
  }

  private void setLayout() {
    title = (TextView) findViewById(R.id.activity_main_title);
    title.setTypeface(FontUtil.fontAPAritaDotumMedium);
    appName = (TextView) findViewById(R.id.activity_main_app_name);
    appName.setTypeface(FontUtil.fontBMHanna);
    viewPager = (ViewPager) findViewById(R.id.view_pager);

    downloadingJsonReceiver = new DownloadingJsonReceiver();
    new LoadingMenuFromJson(this, downloadingJsonReceiver, viewPager).initSetting();
  }

  private void registerReceiver() {
    Log.d("register_receiver", "DownloadingJsonReceiver");

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(DownloadingJsonReceiver.ACTION_PRE_DOWNLOAD);
    intentFilter.addAction(DownloadingJsonReceiver.ACTION_CURRENT_DOWNLOAD);
    intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

    registerReceiver(downloadingJsonReceiver, intentFilter);
  }

  private void unregisterReceiver() {
    if (downloadingJsonReceiver != null) {
      Log.d("unregister_receiver", "DownloadingJsonReceiver");
      unregisterReceiver(downloadingJsonReceiver);
    }
  }

  @Override
  protected void onPause() {
    unregisterReceiver();
    super.onPause();
  }

  @Override
  protected void onResume() {
    registerReceiver();
    super.onResume();
  }
}
