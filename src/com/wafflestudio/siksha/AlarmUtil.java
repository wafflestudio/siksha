package com.wafflestudio.siksha;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by hwangseongman on 2015. 3. 7..
 */
public class AlarmUtil {

  AlarmUtil(){

  }

  public static void setAlarm(Context context) {
    Log.i("ppp", "setAlarm()");

    Intent intent = new Intent(context, AlarmService_Service.class);
    PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

    long cycleTime = 1*60*1000;

    final Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 52);
    calendar.set(Calendar.SECOND, 00);

    AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), cycleTime, sender);

    //am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);

  }

}
