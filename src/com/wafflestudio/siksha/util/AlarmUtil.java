package com.wafflestudio.siksha.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wafflestudio.siksha.service.AlarmServiceReceiver;

import java.util.Calendar;

public class AlarmUtil {
    public static void registerAlarm(Context context) {
        boolean isSetAlarm = SharedPreferenceUtil.loadValueOfBoolean(context, SharedPreferenceUtil.PREF_ALARM_NAME, SharedPreferenceUtil.PREF_KEY_JSON);

        if (!isSetAlarm)
            setAlarm(context);
    }

    public static void setAlarm(Context context) {
        final long cycleTime = 24 * 60 * 60 * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmServiceReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 5);

        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), cycleTime, sender);
        SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_ALARM_NAME, SharedPreferenceUtil.PREF_KEY_JSON, true);
    }
}
