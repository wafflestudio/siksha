package com.wafflestudio.siksha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.NetworkUtil;

public class AlarmServiceReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.e("alarm_time", CalendarUtil.getCurrentDate());

    if (!DownloadingJson.isJsonUpdated(context) && NetworkUtil.getInstance().isOnline()) {
      Intent jsonDownload = new Intent(context, DownloadingJson.class);
      jsonDownload.setAction(DownloadingJsonReceiver.ACTION_PRE_DOWNLOAD);
      context.startService(jsonDownload);
    }
  }
}
