package com.wafflestudio.siksha.util;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wafflestudio.siksha.widget.BabWidgetProvider;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadingJson extends IntentService {
  public static final String SERVER_URL = "http://kanggyu94.fun25.co.kr:13203/restaurants";
  private final String DATE = CalendarUtil.getCurrentDate();

  private static final int DOWNLOAD_START = 0;
  private static final int DOWNLOAD_END = 1;

  public DownloadingJson() {
    super("com.wafflestudio.siksha.DownloadingJson");
  }

  private Handler uiHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case DOWNLOAD_START :
          break;
        case DOWNLOAD_END :
          break;
      }
    }
  };

  public String fetchJsonFromServer() {
    StringBuilder stringBuilder = new StringBuilder();

    try {
      URL url = new URL(SERVER_URL);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(5 * 1000);
      connection.setReadTimeout(5 * 1000);

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
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    return stringBuilder.toString();
  }

  public void writeJsonOnInternalStorage(String data) {
    Context context = getApplicationContext();

    try {
      FileOutputStream fos = context.openFileOutput("restaurants.json", Context.MODE_PRIVATE);
      fos.write(data.getBytes("euc-kr"));
      fos.close();

      SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_NAME, "json_date", DATE);
      Log.d("saveDate", DATE);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendSignalToWidget() {
    Intent widgetUpdate = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
    widgetUpdate.setClass(getApplicationContext(), BabWidgetProvider.class);
    widgetUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    sendBroadcast(widgetUpdate);
  }

  public void sendSignalToApp() {
    Intent intent = new Intent(LoadingMenuFromJson.DownloadingJsonReceiver.ACTION_DOWNLOADING_JSON);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.putExtra("is_need_set_ui", true);
    sendBroadcast(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    boolean isNeedSetUIComponent = intent.getBooleanExtra("is_need_set_ui", false);

    String data = fetchJsonFromServer();
    writeJsonOnInternalStorage(data);
    sendSignalToWidget();

    if (isNeedSetUIComponent) {
      Log.d("isNeedSetUIComponent", isNeedSetUIComponent + "");
      sendSignalToApp();
    }
  }
}