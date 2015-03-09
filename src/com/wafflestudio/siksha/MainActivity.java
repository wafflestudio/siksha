package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.wafflestudio.siksha.dialog.DownloadingRetryDialog;
import com.wafflestudio.siksha.util.AlarmUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.NetworkUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;

public class MainActivity extends Activity {
  private TextView title;
  private ExpandableListView expandableListView;

  private LoadingMenuFromJson.DownloadingJsonReceiver downloadingJsonReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AlarmUtil.registerAlarm(this);
    RestaurantInfoUtil.getInstance().loadingRestaurantInfos();
    NetworkUtil.getInstance().setConnectivityManager(this);
    FontUtil.getInstance().setFontAsset(getAssets());

    title = (TextView) findViewById(R.id.activity_main_title);
    expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
    title.setTypeface(FontUtil.fontAPAritaDotumMedium);

    setExpandableListView();
  }

  public void setExpandableListView() {
    new LoadingMenuFromJson(this, expandableListView).initSetting();
  }

  private void registerReceiver() {
    downloadingJsonReceiver = new LoadingMenuFromJson.DownloadingJsonReceiver(expandableListView);
    IntentFilter intentFilter = new IntentFilter(LoadingMenuFromJson.DownloadingJsonReceiver.ACTION_DOWNLOADING_JSON);
    intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
    registerReceiver(downloadingJsonReceiver, intentFilter);
  }

  private void unregisterReceiver() {
    if (downloadingJsonReceiver != null)
      unregisterReceiver(downloadingJsonReceiver);
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
