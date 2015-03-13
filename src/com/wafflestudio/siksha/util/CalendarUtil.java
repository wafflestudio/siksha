package com.wafflestudio.siksha.util;

import java.util.Calendar;

public class CalendarUtil {

  public static String getCurrentAll() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월 " + calendar.get(Calendar.DAY_OF_MONTH) + "일 "
            + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초";
  }

  public static String getCurrentDate() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.YEAR) + "년 " + (calendar.get(Calendar.MONTH) + 1) + "월 " + calendar.get(Calendar.DAY_OF_MONTH) + "일";
  }

  public static String getCurrentTime() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초";
  }

  public static int getCurrentSecond() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.SECOND);
  }

  public static int getCurrentHour() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.HOUR_OF_DAY);
  }
}
