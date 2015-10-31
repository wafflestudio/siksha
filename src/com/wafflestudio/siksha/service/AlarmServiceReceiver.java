package com.wafflestudio.siksha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wafflestudio.siksha.util.Date;

public class AlarmServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        Log.d("alarm_firing_time", Date.getDate() + " " + Date.getHour() + "시 " + Date.getMinute() + "분");

        new JSONDownloader(context, com.wafflestudio.siksha.service.JSONDownloadReceiver.ACTION_MENU_BACKGROUND_DOWNLOAD).start();
    }
}