package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.service.DownloadingJsonReceiver;
import com.wafflestudio.siksha.util.AlarmUtil;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.InitialLoadingMenu;
import com.wafflestudio.siksha.util.NetworkUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;
import com.wafflestudio.siksha.util.RestaurantSequencer;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
  private RelativeLayout topBar;
  private TextView title;
  private TextView appName;
  private ImageButton bookmarkButton;

  private ViewPager viewPager;

  private DownloadingJsonReceiver downloadingJsonReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AlarmUtil.registerAlarm(this);
    NetworkUtil.getInstance().setConnectivityManager(this);
    FontUtil.getInstance().setFontAsset(this);
    RestaurantInfoUtil.getInstance().initialize(this);
    RestaurantSequencer.getInstance().initialize(this);

    setLayout();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.hide_menu, menu);

    return super.onCreateOptionsMenu(menu);
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.maker_menu:
        Intent intent = new Intent(this, MakerActivity.class);
        startActivity(intent);
        break;
    }

    return true;
  }

  private void setLayout() {
    topBar = (RelativeLayout) findViewById(R.id.activity_main_top_bar);
    title = (TextView) findViewById(R.id.activity_main_title);
    appName = (TextView) findViewById(R.id.activity_main_app_name);
    bookmarkButton = (ImageButton) findViewById(R.id.bookmark_button);
    bookmarkButton.setOnClickListener(this);

    viewPager = (ViewPager) findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(2);
    viewPager.setOnPageChangeListener(this);

    title.setTypeface(FontUtil.fontAPAritaDotumMedium);
    appName.setTypeface(FontUtil.fontAPAritaDotumMedium);

    RestaurantSequencer.getInstance().setViewPager(viewPager);
    setTopBarBackgroundColor(getPageIndexOnHour());
    setBookmarkButtonImage();

    downloadingJsonReceiver = new DownloadingJsonReceiver();
    new InitialLoadingMenu(this, downloadingJsonReceiver, viewPager).initSetting();
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
  public void onClick(View v) {
    int id = v.getId();

    switch (id) {
      case R.id.bookmark_button:
        RestaurantSequencer restaurantSequencer = RestaurantSequencer.getInstance();

        if (!restaurantSequencer.isBookmarkMode()) {
          restaurantSequencer.setBookmarkMode(true);
          bookmarkButton.setImageResource(R.drawable.ic_action_accept);

          restaurantSequencer.notifyChangeToAdapters(true);
          restaurantSequencer.collapseAll();
        }
        else {
          restaurantSequencer.setBookmarkMode(false);
          bookmarkButton.setImageResource(R.drawable.ic_action_star);

          restaurantSequencer.setMenuListOnSequence();
          restaurantSequencer.recordRestaurantSequence(MainActivity.this);
          restaurantSequencer.recordBookmarkList(MainActivity.this);
          restaurantSequencer.notifyChangeToAdapters(true);
          restaurantSequencer.expandBookmarkAll();
        }

        break;
    }
  }

  private int getPageIndexOnHour() {
    int hour = CalendarUtil.getCurrentHour();

    if (hour >= 0 && hour <= 9)
      return 0;
    else if (hour >= 10 && hour <= 15)
      return 1;
    else
      return 2;
  }

  private void setTopBarBackgroundColor(int position) {
    switch (position) {
      case 0:
        topBar.setBackgroundResource(R.color.main_color_breakfast);
        break;
      case 1:
        topBar.setBackgroundResource(R.color.main_color_lunch);
        break;
      case 2:
        topBar.setBackgroundResource(R.color.main_color_dinner);
        break;
    }
  }

  private void setBookmarkButtonImage() {
    if (RestaurantSequencer.getInstance().isBookmarkMode())
      bookmarkButton.setImageResource(R.drawable.ic_action_accept);
    else
      bookmarkButton.setImageResource(R.drawable.ic_action_star);
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
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

  @Override
  public void onPageSelected(int position) {
    setTopBarBackgroundColor(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) { }
}
