package com.wafflestudio.siksha.util;

import java.util.Calendar;

public class CalendarUtil {

  public static String getCurrentDate() {
    Calendar calendar = Calendar.getInstance();
    String dayOfWeek = "";

    switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case 1: dayOfWeek = "(일)";
          break;
        case 2: dayOfWeek = "(월)";
          break;
        case 3: dayOfWeek = "(화)";
          break;
        case 4: dayOfWeek = "(수)";
          break;
        case 5: dayOfWeek = "(목)";
          break;
        case 6: dayOfWeek = "(금)";
          break;
        case 7: dayOfWeek = "(토)";
          break;
    }

    return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " " + dayOfWeek;
  }

  public static String getTomorrowDate() {
    Calendar calendar = Calendar.getInstance();
    String dayOfWeek = "";
    calendar.add(Calendar.DAY_OF_MONTH, 1);

    switch (calendar.get(Calendar.DAY_OF_WEEK)) {
      case 1: dayOfWeek = "(일)";
        break;
      case 2: dayOfWeek = "(월)";
        break;
      case 3: dayOfWeek = "(화)";
        break;
      case 4: dayOfWeek = "(수)";
        break;
      case 5: dayOfWeek = "(목)";
        break;
      case 6: dayOfWeek = "(금)";
        break;
      case 7: dayOfWeek = "(토)";
        break;
    }
    return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " " + dayOfWeek;
  }

  public static int getCurrentHour() {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
  }

  public static int getCurrentMin() { return Calendar.getInstance().get(Calendar.MINUTE); }
}
