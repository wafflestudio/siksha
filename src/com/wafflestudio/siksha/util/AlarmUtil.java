package com.wafflestudio.siksha.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wafflestudio.siksha.service.AlarmServiceReceiver;

import java.util.Calendar;

public class AlarmUtil {
    public static void registerAlarm(Context context) {
        final long cycleTime = 24 * 60 * 60 * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmServiceReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 05);
        calendar.set(Calendar.SECOND, 00);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), cycleTime, sender);
    }
}
