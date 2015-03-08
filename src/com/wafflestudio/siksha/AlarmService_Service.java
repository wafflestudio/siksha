package com.wafflestudio.siksha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by hwangseongman on 2015. 3. 7..
 */
public class AlarmService_Service extends BroadcastReceiver{
  @Override
  public void onReceive(Context context, Intent intent)
  {
    final Calendar c = Calendar.getInstance();
    int mHour = c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);
    int mSecond =c.get(Calendar.SECOND);

    Toast.makeText(context, "현재 시간" + mHour + ":" + mMinute + ":" + mSecond, Toast.LENGTH_SHORT).show();
  }

}
