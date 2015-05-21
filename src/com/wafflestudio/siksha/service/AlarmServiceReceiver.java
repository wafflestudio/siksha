package com.wafflestudio.siksha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wafflestudio.siksha.util.CalendarUtil;

public class AlarmServiceReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d("alarm_time", CalendarUtil.getTodayDate() + " " + CalendarUtil.getCurrentHour() + "h " + CalendarUtil.getCurrentMinute() + "m");

    int option = DownloadingJson.getDownloadOption();
    String downloadDate = DownloadingJson.getDownloadDate(option);

    if (!DownloadingJson.isJsonUpdated(context, downloadDate)) {
      Intent jsonDownload = new Intent(context, DownloadingJson.class);
      jsonDownload.putExtra(DownloadingJson.KEY_OPTION, option);
      jsonDownload.putExtra(DownloadingJson.KEY_DATE, downloadDate);
      jsonDownload.setAction(DownloadingJsonReceiver.ACTION_PRE_DOWNLOAD);
      context.startService(jsonDownload);
    }
  }
}