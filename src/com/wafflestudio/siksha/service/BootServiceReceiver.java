package com.wafflestudio.siksha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wafflestudio.siksha.util.AlarmUtil;

public class BootServiceReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
      Log.d("boot", intent.getAction());

      // register alarm when booting device is completed & alarm is not registered previously
      AlarmUtil.registerAlarm(context);
    }
  }
}
