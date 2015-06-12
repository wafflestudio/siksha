package com.wafflestudio.siksha;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.service.DownloadingJsonReceiver;
import com.wafflestudio.siksha.util.AlarmUtil;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.InitialLoadingTask;
import com.wafflestudio.siksha.util.NetworkUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;
import com.wafflestudio.siksha.util.Sequencer;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
  private Toolbar toolbar;
  private MenuItem bookmark;
  public ViewPager viewPager;

  public DownloadingJsonReceiver downloadingJsonReceiver;

  private boolean isBackPressedTwice;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AlarmUtil.registerAlarm(this);
    NetworkUtil.getInstance().setConnectivityManager(this);
    FontUtil.getInstance().setFontAsset(getAssets());
    RestaurantInfoUtil.getInstance().initialize(this);
    Sequencer.getInstance().initialize(this);

    initialize();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.app_menu, menu);
    bookmark = menu.findItem(R.id.action_bookmark);
    setBookmarkButtonImage();

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh:
        new InitialLoadingTask(this, downloadingJsonReceiver, viewPager).reInitialize();
        break;
      case R.id.action_check_version:
        startActivity(new Intent(this, AppVersionActivity.class));
        break;
      case R.id.action_bookmark:
        Sequencer sequencer = Sequencer.getInstance();

        if (!sequencer.isBookmarkMode()) {
          sequencer.setBookmarkMode(true);
          bookmark.setIcon(R.drawable.ic_check_white);

          sequencer.notifyChangeToAdapters(true);
          sequencer.collapseAll();
        }
        else {
          sequencer.setBookmarkMode(false);
          bookmark.setIcon(R.drawable.ic_star_white);

          sequencer.setMenuListOnSequence();
          sequencer.recordSequence(MainActivity.this);
          sequencer.recordBookmarkList(MainActivity.this);
          sequencer.notifyChangeToAdapters(true);
          sequencer.expandAllBookmark();
        }
        break;
      case R.id.action_developer:
        startActivity(new Intent(this, DeveloperActivity.class));
        break;
    }

    return true;
  }

  private void initialize() {
    toolbar = (Toolbar) findViewById(R.id.activity_main_tool_bar);
    TextView title = (TextView) findViewById(R.id.activity_main_title);
    TextView appName = (TextView) findViewById(R.id.activity_main_app_name);

    title.setTypeface(FontUtil.fontBMJua);
    appName.setTypeface(FontUtil.fontBMJua);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    viewPager = (ViewPager) findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(2);
    viewPager.addOnPageChangeListener(this);
    Sequencer.getInstance().setViewPager(viewPager);

    int pageIndex = getPageIndexOnHour();
    setStatusBarBackgroundColor(pageIndex);
    setToolBarBackgroundColor(pageIndex);

    downloadingJsonReceiver = new DownloadingJsonReceiver();
    new InitialLoadingTask(this, downloadingJsonReceiver, viewPager).initialize();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return keyCode == KeyEvent.KEYCODE_MENU || super.onKeyDown(keyCode, event);
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
  public void onBackPressed() {
    Sequencer sequencer = Sequencer.getInstance();

    if (sequencer.isBookmarkMode()) {
      sequencer.setBookmarkMode(false);
      setBookmarkButtonImage();

      sequencer.cancelAllBookmark(this);
      sequencer.setMenuListOnSequence();
      sequencer.notifyChangeToAdapters(true);
      sequencer.expandAllBookmark();
    }
    else {
      if (isBackPressedTwice) {
        super.onBackPressed();
        return;
      }

      isBackPressedTwice = true;
      Toast.makeText(this, R.string.quit_back_pressed_twice, Toast.LENGTH_SHORT).show();

      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          isBackPressedTwice = false;
        }
      }, 2000);
    }
  }

  private void setBookmarkButtonImage() {
    if (Sequencer.getInstance().isBookmarkMode())
      bookmark.setIcon(R.drawable.ic_check_white);
    else
      bookmark.setIcon(R.drawable.ic_star_white);
  }

  public int getPageIndexOnHour() {
    int hour = CalendarUtil.getCurrentHour();

    if (hour <= 9 || hour >= 21)
      return 0;
    else if (hour >= 10 && hour <= 14)
      return 1;
    else
      return 2;
  }

  public void setStatusBarBackgroundColor(int position) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      switch (position) {
        case 0:
          getWindow().setStatusBarColor(getResources().getColor(R.color.color_primary_dark_breakfast));
          break;
        case 1:
          getWindow().setStatusBarColor(getResources().getColor(R.color.color_primary_dark_lunch));
          break;
        case 2:
          getWindow().setStatusBarColor(getResources().getColor(R.color.color_primary_dark_dinner));
          break;
      }
    }
  }

  public void setToolBarBackgroundColor(int position) {
    switch (position) {
      case 0:
        toolbar.setBackgroundResource(R.color.color_primary_breakfast);
        break;
      case 1:
        toolbar.setBackgroundResource(R.color.color_primary_lunch);
        break;
      case 2:
        toolbar.setBackgroundResource(R.color.color_primary_dinner);
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

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

  @Override
  public void onPageSelected(int position) {
    setStatusBarBackgroundColor(position);
    setToolBarBackgroundColor(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) { }
}
