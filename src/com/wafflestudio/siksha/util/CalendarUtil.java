package com.wafflestudio.siksha.util;

import java.util.Calendar;

public class CalendarUtil {
  public static String getCurrentDate() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.DAY_OF_MONTH);
  }
}
