package com.wafflestudio.siksha;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.dialog.DownloadAlertDialog;
import com.wafflestudio.siksha.dialog.ProgressDialog;
import com.wafflestudio.siksha.dialog.WidgetAlertDialog;
import com.wafflestudio.siksha.form.InformationJSON;
import com.wafflestudio.siksha.form.MenuJSON;
import com.wafflestudio.siksha.page.SwipeDisabledViewPager;
import com.wafflestudio.siksha.page.SwipeDisabledViewPagerAdapter;
import com.wafflestudio.siksha.page.bookmark.BookmarkFragment;
import com.wafflestudio.siksha.page.menu.MenuFragment;
import com.wafflestudio.siksha.page.settings.SettingsFragment;
import com.wafflestudio.siksha.service.DownloadAlarm;
import com.wafflestudio.siksha.service.JSONDownloadReceiver;
import com.wafflestudio.siksha.service.JSONDownloader;
import com.wafflestudio.siksha.util.AppData;
import com.wafflestudio.siksha.util.DeviceNetwork;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.JSONParser;
import com.wafflestudio.siksha.util.Preference;

public class MainActivity extends AppCompatActivity implements com.wafflestudio.siksha.service.JSONDownloadReceiver.OnDownloadListener {
    private TabLayout tabLayout;
    private SwipeDisabledViewPager swipeDisabledViewPager;
    private ProgressDialog progressDialog;

    private SwipeDisabledViewPagerAdapter adapter;
    private JSONDownloadReceiver JSONDownloadReceiver;

    private boolean isBackPressedTwice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadAlarm.registerAlarm(this);
        DeviceNetwork.getInstance().initialize(this);
        Fonts.getInstance().initialize(this);
        AppData.getInstance().setSequences(this);
        AppData.getInstance().setInformationDictionary(JSONParser.parseJSONFile(this, InformationJSON.class).data);

        JSONDownloadReceiver = new JSONDownloadReceiver();
        JSONDownloadReceiver.setOnDownloadListener(this);

        checkInformationData();
        checkMenuData();
        checkCurrentAppVersion();
        checkLatestAppVersion();
    }

    private void checkInformationData() {
        if (DeviceNetwork.getInstance().isOnline()) {
            new JSONDownloader(this, com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_INFORMATION_DOWNLOAD).start();
        }
    }

    private void checkMenuData() {
        if (JSONDownloader.isJSONUpdated(this)) {
            AppData.getInstance().setMenuDictionaries(JSONParser.parseJSONFile(getApplicationContext(), MenuJSON.class).data);
            setupViewPager();
            selectInitialTab();
            alertWidgetFeature();
        } else {
            if (!DeviceNetwork.getInstance().isOnline())
                new DownloadAlertDialog(this).show();
            else
                downloadMenuData();
        }
    }

    public void downloadMenuData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.start();

        new JSONDownloader(this, com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_MENU_FOREGROUND_DOWNLOAD).start();
    }

    private void checkCurrentAppVersion() {
        try {
            String currentAppVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Preference.save(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_APP_VERSION, currentAppVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkLatestAppVersion() {
        if (DeviceNetwork.getInstance().isOnline()) {
            new JSONDownloader(this, com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_LATEST_APP_VERSION_CHECK).start();
        }
    }

    private void setupViewPager() {
        swipeDisabledViewPager = (SwipeDisabledViewPager) findViewById(R.id.swipe_disabled_view_pager);
        swipeDisabledViewPager.setOffscreenPageLimit(2);
        swipeDisabledViewPager.setSwipeEnabled(false);

        adapter = new SwipeDisabledViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new BookmarkFragment(), "즐겨찾기");
        adapter.addPage(new MenuFragment(), "식단");
        adapter.addPage(new SettingsFragment(), "설정");
        swipeDisabledViewPager.setAdapter(adapter);
        swipeDisabledViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ((BookmarkFragment) adapter.getItem(position)).notifyToAdapters();
                } else if (position == 1) {
                    ((MenuFragment) adapter.getItem(position)).notifyToAdapters();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.activity_main_tab_layout);
        tabLayout.setupWithViewPager(swipeDisabledViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabLayout(tab.getPosition(), true);
                swipeDisabledViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabLayout(tab.getPosition(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            setTabLayout(i, false);
        }
    }

    private void alertWidgetFeature() {
        boolean isWidgetFeatureAlerted = Preference.loadBooleanValue(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_WIDGET_FEATURE_ALERTED);

        if (!isWidgetFeatureAlerted) {
            new WidgetAlertDialog(this).show();
            Preference.save(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_WIDGET_FEATURE_ALERTED, true);
        }
    }

    private void setTabLayout(int tabIndex, boolean isSelected) {
        tabLayout.getTabAt(tabIndex).setCustomView(null);

        View view = LayoutInflater.from(this).inflate(R.layout.tab, null);
        ImageView tabImage = (ImageView) view.findViewById(R.id.tab_image_view);
        TextView tabTitle = (TextView) view.findViewById(R.id.tab_text_view);
        tabTitle.setTypeface(Fonts.fontAPAritaDotumMedium);

        if (isSelected) {
            switch (tabIndex) {
                case 0:
                    tabImage.setImageResource(R.drawable.ic_tab_star_selected);
                    tabTitle.setText("즐겨찾기");
                    break;
                case 1:
                    tabImage.setImageResource(R.drawable.ic_meal_selected);
                    tabTitle.setText("식단");
                    break;
                case 2:
                    tabImage.setImageResource(R.drawable.ic_settings_selected);
                    tabTitle.setText("설정");
                    break;
            }

            tabImage.setAlpha(1.0f);
            tabTitle.setTextColor(getResources().getColor(R.color.color_accent));
        } else {
            switch (tabIndex) {
                case 0:
                    tabImage.setImageResource(R.drawable.ic_tab_star);
                    tabTitle.setText("즐겨찾기");
                    break;
                case 1:
                    tabImage.setImageResource(R.drawable.ic_meal);
                    tabTitle.setText("식단");
                    break;
                case 2:
                    tabImage.setImageResource(R.drawable.ic_settings);
                    tabTitle.setText("설정");
                    break;
            }

            tabImage.setAlpha(0.87f);
            tabTitle.setTextColor(getResources().getColor(R.color.text_color_primary));
        }

        tabLayout.getTabAt(tabIndex).setCustomView(view);
    }

    private void selectInitialTab() {
        if (Preference.loadStringValue(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS).equals(""))
            tabLayout.getTabAt(1).select();
        else
            tabLayout.getTabAt(0).select();
    }

    public SwipeDisabledViewPagerAdapter getSwipeDisabledViewPagerAdapter() {
        return adapter;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_MENU || super.onKeyDown(keyCode, event);
    }

    private void registerReceiver() {
        Log.d("register_receiver", "JSONDownloadReceiver");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_MENU_FOREGROUND_DOWNLOAD);
        intentFilter.addAction(com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD);
        intentFilter.addAction(com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_INFORMATION_DOWNLOAD);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        registerReceiver(JSONDownloadReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (JSONDownloadReceiver != null) {
            Log.d("unregister_receiver", "JSONDownloadReceiver");
            unregisterReceiver(JSONDownloadReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        if (isBackPressedTwice) {
            super.onBackPressed();
            return;
        }

        isBackPressedTwice = true;
        Toast.makeText(this, R.string.back_pressed_twice_quit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressedTwice = false;
            }
        }, 2000);
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
    public void onSuccess(String action) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.quit();

        /* After updating local JSON... */
        if (action.equals(com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_INFORMATION_DOWNLOAD)) {
            AppData.getInstance().setInformationDictionary(JSONParser.parseJSONFile(this, InformationJSON.class).data);
        } else if (action.equals(com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_MENU_FOREGROUND_DOWNLOAD)) {
            AppData.getInstance().setMenuDictionaries(JSONParser.parseJSONFile(getApplicationContext(), MenuJSON.class).data);
            setupViewPager();
            selectInitialTab();
            alertWidgetFeature();
        } else if (action.equals(com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD)) {
            AppData.getInstance().setMenuDictionaries(JSONParser.parseJSONFile(getApplicationContext(), MenuJSON.class).data);
        }
    }

    @Override
    public void onFailure(String action) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.quit();

        /* An error occurs when updating local JSON. */
        if (action.equals(com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_MENU_FOREGROUND_DOWNLOAD))
            new DownloadAlertDialog(getApplicationContext()).show();
    }
}
