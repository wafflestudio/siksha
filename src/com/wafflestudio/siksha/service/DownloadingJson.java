package com.wafflestudio.siksha.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;
import com.wafflestudio.siksha.widget.BabWidgetProvider;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadingJson extends IntentService {
  public static final String SERVER_URL = "http://kanggyu94.fun25.co.kr:13203/restaurants";

  public DownloadingJson() {
    super("com.wafflestudio.siksha.DownloadingJson");
  }

  public static boolean isJsonUpdated(Context context) {
    String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON);
    Log.d("recorded_date", recordedDate);

    return recordedDate.equals(CalendarUtil.getCurrentDate());
  }

  public String fetchJsonFromServer() {
    StringBuilder stringBuilder = new StringBuilder();

    try {
      URL url;

      if (CalendarUtil.getCurrentHour() == 0 && CalendarUtil.getCurrentMin() < 5)
        url = new URL(SERVER_URL);
      else
        url = new URL(SERVER_URL + "?alarm=true");

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

  private void saveDateOnSharedPreference() {
    String currentDate = CalendarUtil.getCurrentDate();
    SharedPreferenceUtil.save(getApplicationContext(), SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON, currentDate);
    Log.d("save_date", currentDate);
  }

  public void sendSignalToWidget(boolean fromWidgetUser, boolean isSuccess) {
    Intent widgetUpdate = new Intent(BabWidgetProvider.DATA_FETCHED);
    widgetUpdate.putExtra("is_success", isSuccess);
    widgetUpdate.putExtra("from_widget_user", fromWidgetUser);
    sendBroadcast(widgetUpdate);
  }

  public void sendSignalToApp(String action, boolean isSuccess) {
    Intent intent = new Intent();
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.setAction(action);
    intent.putExtra("is_success", isSuccess);
    sendBroadcast(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    final String action = intent.getAction();
    final boolean isSuccess = writeJsonOnInternalStorage(fetchJsonFromServer());
    final boolean fromWidget = intent.getBooleanExtra("from_widget_user", false);
    Log.d("onHandleIntent", "isSuccess : " + isSuccess + " / " + "action : " + action);

    if (isSuccess)
      saveDateOnSharedPreference();

    sendSignalToApp(action, isSuccess);
    sendSignalToWidget(fromWidget, isSuccess);
  }
}