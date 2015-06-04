package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {
  private RelativeLayout topBar;
  private ImageButton bookmarkButton;

  private DownloadingJsonReceiver downloadingJsonReceiver;

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

    setLayout();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.hidden_menu, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
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
    TextView title = (TextView) findViewById(R.id.activity_main_title);
    TextView appName = (TextView) findViewById(R.id.activity_main_app_name);
    bookmarkButton = (ImageButton) findViewById(R.id.bookmark_button);
    bookmarkButton.setOnClickListener(this);

    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(2);
    viewPager.setOnPageChangeListener(this);

    title.setTypeface(FontUtil.fontAPAritaDotumMedium);
    appName.setTypeface(FontUtil.fontAPAritaDotumMedium);

    Sequencer.getInstance().setViewPager(viewPager);
    setTopBarBackgroundColor(getPageIndexOnHour());
    setBookmarkButtonImage();

    downloadingJsonReceiver = new DownloadingJsonReceiver();
    new InitialLoadingTask(this, downloadingJsonReceiver, viewPager).initialize();
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
        Sequencer sequencer = Sequencer.getInstance();

        if (!sequencer.isBookmarkMode()) {
          sequencer.setBookmarkMode(true);
          bookmarkButton.setImageResource(R.drawable.ic_action_accept);

          sequencer.notifyChangeToAdapters(true);
          sequencer.collapseAll();
        }
        else {
          sequencer.setBookmarkMode(false);
          bookmarkButton.setImageResource(R.drawable.ic_action_star);

          sequencer.setMenuListOnSequence();
          sequencer.recordRestaurantSequence(MainActivity.this);
          sequencer.recordBookmarkList(MainActivity.this);
          sequencer.notifyChangeToAdapters(true);
          sequencer.expandBookmarkAll();
        }

        break;
    }
  }

  @Override
  public void onBackPressed() {
    Sequencer sequencer = Sequencer.getInstance();

    if (sequencer.isBookmarkMode()) {
      sequencer.setBookmarkMode(false);
      setBookmarkButtonImage();

      sequencer.cancelBookmarkAll(this);
      sequencer.setMenuListOnSequence();
      sequencer.notifyChangeToAdapters(true);
      sequencer.expandBookmarkAll();
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

  private int getPageIndexOnHour() {
    int hour = CalendarUtil.getCurrentHour();

    if (hour <= 9 || hour >= 21)
      return 0;
    else if (hour >= 10 && hour <= 14)
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
    if (Sequencer.getInstance().isBookmarkMode())
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
