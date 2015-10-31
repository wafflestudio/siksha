package com.wafflestudio.siksha.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wafflestudio.siksha.util.Preference;

import java.util.Calendar;

public class DownloadAlarm {
    public static void registerAlarm(Context context) {
        boolean alarmAlreadySet = Preference.loadBooleanValue(context, Preference.PREF_ALARM_NAME, Preference.PREF_KEY_ALARM_ALREADY_SET);

        if (!alarmAlreadySet)
            setAlarm(context);
    }

    public static void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmServiceReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar moment = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        moment.setTimeInMillis(System.currentTimeMillis());
        now.setTimeInMillis(System.currentTimeMillis());

        moment.set(Calendar.HOUR_OF_DAY, 21);
        moment.set(Calendar.MINUTE, 0);
        moment.set(Calendar.SECOND, 0);
        now.set(Calendar.SECOND, 0);

        // now is faster than alarm. initial alarm is on today.
        if (!now.before(moment)) {
            moment.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, moment.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
        Preference.save(context, Preference.PREF_ALARM_NAME, Preference.PREF_KEY_ALARM_ALREADY_SET, true);
    }
}
