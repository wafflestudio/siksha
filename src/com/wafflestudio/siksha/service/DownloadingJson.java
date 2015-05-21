package com.wafflestudio.siksha.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;
import com.wafflestudio.siksha.widget.WidgetProvider;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadingJson extends IntentService {
  public static final String SERVER_URL = "http://siksha.kr:3280/restaurants";
  public static final String QUERY_TODAY = "?date=today";
  public static final String QUERY_TOMORROW = "?date=tomorrow";
  public static final String KEY_OPTION = "download_option";
  public static final String KEY_DATE = "download_date";

  public static final int OPTION_CRAWLING_INSTANTLY = 0;
  public static final int OPTION_CACHED_TODAY = 1;
  public static final int OPTION_CACHED_TOMORROW = 2;

  public DownloadingJson() {
    super("com.wafflestudio.siksha.DownloadingJson");
  }

  public static boolean isJsonUpdated(Context context, String downloadDate) {
    String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON);
    Log.d("recorded_date", recordedDate);

    return recordedDate.equals(downloadDate);
  }

  public static boolean isVetDataUpdated(Context context, String downloadDate) {
    String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_VET_DATA);
    Log.d("recorded_date", recordedDate);

    return recordedDate.equals(downloadDate);
  }

  public static int getDownloadOption() {
    int hour = CalendarUtil.getCurrentHour();
    int min = CalendarUtil.getCurrentMinute();

    if (hour == 0 && min < 5)
      return OPTION_CRAWLING_INSTANTLY; // request server for crawling web page instantly
    else if (hour < 21)
      return OPTION_CACHED_TODAY; // request server for fetching cached json about today contents
    else
      return OPTION_CACHED_TOMORROW; // request server for fetching cached json about tomorrow contents
  }

  public static String getDownloadDate(int option) {
    if (option == OPTION_CACHED_TOMORROW)
      return CalendarUtil.getTomorrowDate();
    else
      return CalendarUtil.getTodayDate();
  }

  public String fetchJsonFromServer(int option) {
    StringBuilder stringBuilder = new StringBuilder();

    try {
      URL url;

      switch (option) {
        case OPTION_CACHED_TODAY :
          url = new URL(SERVER_URL + QUERY_TODAY);
          break;
        case OPTION_CACHED_TOMORROW :
          url = new URL(SERVER_URL + QUERY_TOMORROW);
          break;
        default :
          url = new URL(SERVER_URL);
          break;
      }

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(10 * 1000);
      connection.setReadTimeout(10 * 1000);

      if (connection.getResponseCode() == 200) {
        Log.d("connection", "success");
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
          stringBuilder.append(line);
        }
        is.close();
        br.close();
      }
      else {
        Log.d("connection", "fail");
        connection.disconnect();
        return null;
      }
    }
    catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    // Success to fetch json from server
    return stringBuilder.toString();
  }

  public boolean writeJsonOnInternalStorage(String data) {
    Context context = getApplicationContext();

    // Error checking about fetchJsonFromServer()
    if (data == null)
      return false;

    try {
      FileOutputStream fos = context.openFileOutput("restaurants.json", Context.MODE_PRIVATE);
      fos.write(data.getBytes("euc-kr"));
      fos.close();
    }
    catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    // Success to write json on internal storage
    return true;
  }

  private void saveDateOnSharedPreference(String downloadDate) {
    SharedPreferenceUtil.save(getApplicationContext(), SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON, downloadDate);
    Log.d("save_date", downloadDate);

    if (CalendarUtil.isVetDataUpdateTime()) {
      SharedPreferenceUtil.save(getApplicationContext(), SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_VET_DATA, downloadDate);
      Log.d("vet_data_update_date", downloadDate);
    }
  }

  public void sendSignalToWidget(boolean fromWidgetUser, boolean isSuccess) {
    Intent widgetUpdate = new Intent(WidgetProvider.DATA_FETCHED);
    widgetUpdate.putExtra("is_success", isSuccess);
    widgetUpdate.putExtra("from_widget_user", fromWidgetUser);
    sendBroadcast(widgetUpdate);
  }

  public void sendSignalToApp(String action, boolean isSuccess, int option, String downloadDate) {
    Intent intent = new Intent();
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.setAction(action);
    intent.putExtra("is_success", isSuccess);
    intent.putExtra(KEY_OPTION, option);
    intent.putExtra(KEY_DATE, downloadDate);
    sendBroadcast(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    final String action = intent.getAction();
    final int option = intent.getIntExtra(KEY_OPTION, OPTION_CRAWLING_INSTANTLY);
    final String downloadDate = intent.getStringExtra(KEY_DATE);
    final boolean isSuccess = writeJsonOnInternalStorage(fetchJsonFromServer(option));
    final boolean fromWidget = intent.getBooleanExtra("from_widget_user", false);

    Log.d("onHandleIntent", "isSuccess : " + isSuccess + " / " + "action : " + action);

    if (isSuccess)
      saveDateOnSharedPreference(downloadDate);

    sendSignalToApp(action, isSuccess, option, downloadDate);
    sendSignalToWidget(fromWidget, isSuccess);
  }
}