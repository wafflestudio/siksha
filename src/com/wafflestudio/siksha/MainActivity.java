package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.wafflestudio.siksha.dialog.RestaurantInfoDialog;
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
    title.setTypeface(FontUtil.fontAPAritaDotumSemiBold);

    setExpandableListView();
  }

  public void setExpandableListView() {
    expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
          RestaurantInfoDialog dialog = new RestaurantInfoDialog(MainActivity.this, RestaurantInfoUtil.restaurants[position]);
          dialog.setCanceledOnTouchOutside(true);
          dialog.show();

          return true;
        }

        return false;
      }
    });
    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
      @Override
      public void onGroupExpand(int groupPosition) {
        int groupSize = RestaurantInfoUtil.restaurants.length;

        for(int i = 0; i < groupSize; i++) {
          if (i != groupPosition)
            expandableListView.collapseGroup(i);
        }
      }
    });
    setGroupIndicatorPosition();

    new LoadingMenuFromJson(this, expandableListView).initSetting();
  }

  private void setGroupIndicatorPosition() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    int width = displayMetrics.widthPixels;
    expandableListView.setIndicatorBounds(width - getDpFromPixel(25), width - getDpFromPixel(10));
  }

  public int getDpFromPixel(float pixels) {
    final float scale = getResources().getDisplayMetrics().density;
    return (int) (pixels * scale + 0.5f);
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
