package com.wafflestudio.siksha.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.ProgressDialog;
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

  public DownloadingJson() {
    super("com.wafflestudio.siksha.DownloadingJson");
  }

  public String fetchJsonFromServer() {
    StringBuilder stringBuilder = new StringBuilder();

    try {
      URL url = new URL(SERVER_URL);
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
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    return stringBuilder.toString();
  }

  public boolean writeJsonOnInternalStorage(String data) {
    Context context = getApplicationContext();

    if (data == null)
      return false;

    try {
      FileOutputStream fos = context.openFileOutput("restaurants.json", Context.MODE_PRIVATE);
      fos.write(data.getBytes("euc-kr"));
      fos.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private void onPostDownloading() {
    SharedPreferenceUtil.save(getApplicationContext(), SharedPreferenceUtil.PREF_NAME, "json_date", DATE);
    Log.d("saveDate", DATE);
  }

  public void sendSignalToWidget() {
    Intent widgetUpdate = new Intent(BabWidgetProvider.DATA_FETCHED);
    sendBroadcast(widgetUpdate);
  }

  public void sendSignalToApp() {
    Intent intent = new Intent(LoadingMenuFromJson.DownloadingJsonReceiver.ACTION_DOWNLOADING_JSON);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.putExtra("is_need_set_expandable_list_view", true);
    sendBroadcast(intent);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    boolean isNeedSetUIComponent = intent.getBooleanExtra("is_need_set_expandable_list_view", false);

    boolean isSuccess = writeJsonOnInternalStorage(fetchJsonFromServer());
    if (isSuccess) {
      onPostDownloading();

      if (isNeedSetUIComponent)
        sendSignalToApp();
      sendSignalToWidget();
    }
  }
}