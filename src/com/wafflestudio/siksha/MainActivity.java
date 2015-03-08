package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.widget.ExpandableListView;

import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;

public class MainActivity extends Activity {
  private ExpandableListView expandableListView;

  private LoadingMenuFromJson.DownloadingJsonReceiver downloadingJsonReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE); // remove bar at the top
    setContentView(R.layout.activity_main);

    RestaurantInfoUtil.getInstance().loadingRestaurantInfos();

    expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
    new LoadingMenuFromJson(this, expandableListView);
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}