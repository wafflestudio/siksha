package com.wafflestudio.siksha.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wafflestudio.siksha.service.AlarmServiceReceiver;

import java.security.acl.NotOwnerException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmUtil {
    public static void registerAlarm(Context context) {
        boolean isSetAlarm = SharedPreferenceUtil.loadValueOfBoolean(context, SharedPreferenceUtil.PREF_ALARM_NAME, SharedPreferenceUtil.PREF_KEY_JSON);

        if (!isSetAlarm)
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

        if (now.before(moment)) {
            //now is faster than alarm. initial alarm is on today.
        } else {
            moment.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, moment.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
        SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_ALARM_NAME, SharedPreferenceUtil.PREF_KEY_JSON, true);
    }
}