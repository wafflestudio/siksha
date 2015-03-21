package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.page.BreakfastPage;
import com.wafflestudio.siksha.page.DinnerPage;
import com.wafflestudio.siksha.page.LunchPage;
import com.wafflestudio.siksha.service.DownloadingJsonReceiver;
import com.wafflestudio.siksha.util.AlarmUtil;
import com.wafflestudio.siksha.util.BookmarkUtil;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.NetworkUtil;
import com.wafflestudio.siksha.util.RestaurantInfo;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener {
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
    BookmarkUtil.getInstance().initialize();
    RestaurantInfo.getInstance().loading(this);

    setLayout();
  }

  private void setLayout() {
    topBar = (RelativeLayout) findViewById(R.id.activity_main_top_bar);
    title = (TextView) findViewById(R.id.activity_main_title);
    appName = (TextView) findViewById(R.id.activity_main_app_name);
    bookmarkButton = (ImageButton) findViewById(R.id.bookmark_button);
    bookmarkButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        BookmarkUtil bookmarkUtil = BookmarkUtil.getInstance();
        int pageIndex = viewPager.getCurrentItem();

        if (!bookmarkUtil.isBookmarkMode()) {
          bookmarkButton.setImageResource(R.drawable.ic_action_star_brighted);
          bookmarkUtil.setBookmarkMode(true);

          if (pageIndex == 0) {
            BreakfastPage page = (BreakfastPage) viewPager.findViewWithTag("page" + pageIndex);
            page.expandableListAdapter.notifyDataSetChanged();
          }
          else if (pageIndex == 1) {
            LunchPage page = (LunchPage) viewPager.findViewWithTag("page" + pageIndex);
            page.expandableListAdapter.notifyDataSetChanged();
          }
          else {
            DinnerPage page = (DinnerPage) viewPager.findViewWithTag("page" + pageIndex);
            page.expandableListAdapter.notifyDataSetChanged();
          }
        }
        else {
          bookmarkButton.setImageResource(R.drawable.ic_action_star);
          bookmarkUtil.setBookmarkMode(false);

          if (pageIndex == 0) {
            BreakfastPage page = (BreakfastPage) viewPager.findViewWithTag("page" + pageIndex);
            page.expandableListAdapter.notifyDataSetChanged();
          }
          else if (pageIndex == 1) {
            LunchPage page = (LunchPage) viewPager.findViewWithTag("page" + pageIndex);
            page.expandableListAdapter.notifyDataSetChanged();
          }
          else {
            DinnerPage page = (DinnerPage) viewPager.findViewWithTag("page" + pageIndex);
            page.expandableListAdapter.notifyDataSetChanged();
          }
        }
      }
    });

    viewPager = (ViewPager) findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(2);
    viewPager.setOnPageChangeListener(this);

    title.setTypeface(FontUtil.fontAPAritaDotumMedium);
    appName.setTypeface(FontUtil.fontAPAritaDotumMedium);

    setTopBarBackgroundColor(getPageIndexOnHour());

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
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

  @Override
  public void onPageSelected(int position) {
    setTopBarBackgroundColor(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) { }

  private int getPageIndexOnHour() {
    int time = CalendarUtil.getCurrentHour();

    if (time >= 0 && time <= 9)
      return 0;
    else if (time >= 10 && time <= 15)
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
