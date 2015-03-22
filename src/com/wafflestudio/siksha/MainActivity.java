package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.dialog.TutorialDialog;
import com.wafflestudio.siksha.page.BreakfastPage;
import com.wafflestudio.siksha.page.DinnerPage;
import com.wafflestudio.siksha.page.LunchPage;
import com.wafflestudio.siksha.service.DownloadingJsonReceiver;
import com.wafflestudio.siksha.util.AlarmUtil;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.NetworkUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;
import com.wafflestudio.siksha.util.RestaurantSequencer;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

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

    checkTutorial();
    setLayout();
  }

  private void checkTutorial() {
    boolean isTutorialDone = SharedPreferenceUtil.loadValueOfBoolean(this, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_TUTORIAL);

    if (!isTutorialDone) {
      TutorialDialog tutorialDialog = new TutorialDialog(this, "yes");
      tutorialDialog.setCancelable(false);
      tutorialDialog.startShowing();

      SharedPreferenceUtil.save(this, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_TUTORIAL, true);
    }
  }

  private void setLayout() {
    topBar = (RelativeLayout) findViewById(R.id.activity_main_top_bar);
    title = (TextView) findViewById(R.id.activity_main_title);
    appName = (TextView) findViewById(R.id.activity_main_app_name);
    bookmarkButton = (ImageButton) findViewById(R.id.bookmark_button);
    bookmarkButton.setOnClickListener(this);

    if (RestaurantSequencer.getInstance().isBookmarkMode()) {
      bookmarkButton.setImageResource(R.drawable.ic_action_accept);
    }
    else {
      bookmarkButton.setImageResource(R.drawable.ic_action_star);
    }

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

  @Override
  public void onClick(View v) {
    int id = v.getId();

    switch (id) {
      case R.id.bookmark_button:
        RestaurantSequencer restaurantSequencer = RestaurantSequencer.getInstance();

        if (!restaurantSequencer.isBookmarkMode()) {
          bookmarkButton.setImageResource(R.drawable.ic_action_accept);
          restaurantSequencer.setBookmarkMode(true);

          notifyImageChangeToPages();
        }
        else {
          bookmarkButton.setImageResource(R.drawable.ic_action_star);
          restaurantSequencer.setBookmarkMode(false);
          restaurantSequencer.recordRestaurantSequence(MainActivity.this);
          restaurantSequencer.recordBookmarkList(MainActivity.this);

          notifyDataChangeToPages();
        }

        break;
    }
  }

  private void notifyImageChangeToPages() {
    BreakfastPage breakfastPage = (BreakfastPage) viewPager.findViewWithTag("page" + 0);
    LunchPage lunchPage = (LunchPage) viewPager.findViewWithTag("page" + 1);
    DinnerPage dinnerPage = (DinnerPage) viewPager.findViewWithTag("page" + 2);

    breakfastPage.expandableListAdapter.notifyDataSetChanged();
    lunchPage.expandableListAdapter.notifyDataSetChanged();
    dinnerPage.expandableListAdapter.notifyDataSetChanged();
  }

  private void notifyDataChangeToPages() {
    BreakfastPage breakfastPage = (BreakfastPage) viewPager.findViewWithTag("page" + 0);
    LunchPage lunchPage = (LunchPage) viewPager.findViewWithTag("page" + 1);
    DinnerPage dinnerPage = (DinnerPage) viewPager.findViewWithTag("page" + 2);

    breakfastPage.expandableListAdapter.forms = RestaurantSequencer.getInstance().breakfastMenuList;
    lunchPage.expandableListAdapter.forms = RestaurantSequencer.getInstance().lunchMenuList;
    dinnerPage.expandableListAdapter.forms = RestaurantSequencer.getInstance().dinnerMenuList;

    breakfastPage.expandableListAdapter.notifyDataSetChanged();
    lunchPage.expandableListAdapter.notifyDataSetChanged();
    dinnerPage.expandableListAdapter.notifyDataSetChanged();
  }

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
