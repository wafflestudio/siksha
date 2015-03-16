package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wafflestudio.siksha.page.BreakfastPage;
import com.wafflestudio.siksha.page.DinnerPage;
import com.wafflestudio.siksha.page.LunchPage;
import com.wafflestudio.siksha.util.AlarmUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.NetworkUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;

public class MainActivity extends Activity {
  private TextView title;
  private TextView appName;
  private ViewPager mPager;

  private LoadingMenuFromJson.DownloadingJsonReceiver downloadingJsonReceiver;

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

    mPager = (ViewPager)findViewById(R.id.view_pager);
    mPager.setAdapter(new PagerAdapterClass(this));
  }

  ////////////////////viewPager////////////////////

  private class PagerAdapterClass extends PagerAdapter {
    private Context context;

    public PagerAdapterClass(Context context) {
      super();
      this.context = context;
    }

    @Override
    public int getCount() {
      return 3;
    }

    ////////////// make view //////////////
    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
      View view = null;

      if (position == 0)
        view = new BreakfastPage(context);
      else if (position == 1)
        view = new LunchPage(context);
      else
        view = new DinnerPage(context);

      pager.addView(view, 0);

      return view;
    }

    @Override
    public void destroyItem(ViewGroup pager, int position, Object view) {
      pager.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View pager, Object obj) {
      return pager == obj;
    }
  }

  ////////////////////viewPager////////////////////

  private void registerReceiver() {
    Log.d("register_receiver", "DownloadingJsonReceiver");
    downloadingJsonReceiver = new LoadingMenuFromJson.DownloadingJsonReceiver();

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(LoadingMenuFromJson.DownloadingJsonReceiver.ACTION_PRE_DOWNLOAD);
    intentFilter.addAction(LoadingMenuFromJson.DownloadingJsonReceiver.ACTION_CURRENT_DOWNLOAD);
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
