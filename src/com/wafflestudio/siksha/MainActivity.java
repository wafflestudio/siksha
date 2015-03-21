package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.dialog.ProgressDialog;
import com.wafflestudio.siksha.dialog.TutorialDialog;
import com.wafflestudio.siksha.service.DownloadingJsonReceiver;
import com.wafflestudio.siksha.util.AlarmUtil;
import com.wafflestudio.siksha.util.BookmarkUtil;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.NetworkUtil;
import com.wafflestudio.siksha.util.RestaurantInfo;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener {
  private LinearLayout topBar;
  private TextView title;
  private TextView appName;

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

    checkTutorial();

    setLayout();
  }

  private void checkTutorial() {
    boolean isTutorialDone =
             SharedPreferenceUtil.loadValueOfBoolean(this, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_TUTORIAL);
    if (!isTutorialDone) {
      TutorialDialog tutorialDialog = new TutorialDialog(this, "yee");
      tutorialDialog.setCancelable(false);
      tutorialDialog.startShowing();
      SharedPreferenceUtil.save(this, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_TUTORIAL, true);
    }
  }

  private void setLayout() {
    topBar = (LinearLayout) findViewById(R.id.activity_main_top_bar);
    title = (TextView) findViewById(R.id.activity_main_title);
    appName = (TextView) findViewById(R.id.activity_main_app_name);

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