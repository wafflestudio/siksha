package com.wafflestudio.siksha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wafflestudio.siksha.util.CalendarUtil;

public class AlarmServiceReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Log.e("alarm_time", CalendarUtil.getCurrentAll());

    if (DownloadingJson.isJsonUpdated(context)) {
      Intent jsonDownload = new Intent(context, DownloadingJson.class);
      intent.putExtra("is_need_set_ui", false);
      context.startService(jsonDownload);
    }
  }
}
