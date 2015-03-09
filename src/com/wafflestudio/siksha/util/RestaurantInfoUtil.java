package com.wafflestudio.siksha.util;

import java.util.HashMap;

public class RestaurantInfoUtil {
  private static RestaurantInfoUtil restaurantInfoUtil;

  public static HashMap<String, String> operatingHours = new HashMap<String, String>();
  public static HashMap<String, String> locations = new HashMap<String, String>();
  public static HashMap<String, String> matchings = new HashMap<String, String>();

  public static final String[] restaurants = {
          "학생회관식당", "3식당", "기숙사식당",
          "자하연식당", "302동식당", "동원관식당",
          "감골식당", "4식당", "두레미담",
          "301동식당", "공대간이식당", "상아회관",
          "220동식당", "대학원기숙사식당"
  };

  private RestaurantInfoUtil() { }

  public static RestaurantInfoUtil getInstance() {
    if (restaurantInfoUtil == null)
      restaurantInfoUtil = new RestaurantInfoUtil();
    return restaurantInfoUtil;
  }

  public void loadingRestaurantInfos() {
    operatingHours.put("학생회관 식당", "<A코너>\n오전 11:00 ~ 오후 2:00\n오후 4:30 ~ 오후 7:00\n\n<B코너>\n오전 8:00 ~ 오전 10:00\n오전 11:00 ~ 오후 2:00\n오후 5:00 ~ 오후 7:00\n\n<C코너>\n오전 10:00 ~ 오후 4:30\n오후 5:30 ~ 오후 9:00");
    operatingHours.put("농생대 3식당", "<3층>\n오전 11:00 ~ 오후 2:00\n\n<4층>\n오전 11:30 ~ 오후 1:30\n오후 5:00 ~ 오후 7:00");
    operatingHours.put("기숙사 919동 식당", "오전 7:30 ~ 오전 9:30\n오전 11:30 ~ 오후 1:30\n오후 5:30 ~ 오후 7:30");
    operatingHours.put("자하연 식당", "오전 11:00 ~ 오후 2:00 \n오후 5:00 ~ 오후 7:00");
    operatingHours.put("302동 식당", "오전 11:15 ~ 오후 2:00\n오후 5:00 ~ 오후 7:00");
    operatingHours.put("동원관 식당", "오전 11:00 ~ 오후 2:00 \n오후 5:00 ~ 오후 7:00");
    operatingHours.put("감골 식당", "오전 11:00 ~ 오후 2:00 \n오후 5:00 ~ 오후 6:30\n\n<채식 뷔페>\n오전 11:30 ~ 오후 1:30");
    operatingHours.put("사범대 4식당", "오전 11:00 ~ 오후 2:00\n오후 5:00 ~ 오후 6:30");
    operatingHours.put("두레미담", "오전 11:30 ~ 오후 9:00");
    operatingHours.put("301동 식당", "오전 11:00 ~ 오후 2:00\n오후 5:00 ~ 오후 7:00");
    operatingHours.put("공대 간이 식당", "오전 8:00 ~ 오후 8:30");
    operatingHours.put("상아회관 식당", "오전 11:00 ~ 오후 2:00");
    operatingHours.put("220동 식당", "오전 8:00 ~ 오후 5:00\n오전 11:00 ~ 오후 2:00\n오후 5:00 ~ 오후 7:00");
    operatingHours.put("대학원 기숙사 식당", "오전 7:30 ~ 오전 9:30\n오전 11:30 ~ 오후 1:30\n오후 5:30 ~ 오후 7:30");

    locations.put("학생회관 식당", "63동");
    locations.put("농생대 3식당", "75-1동 전망대");
    locations.put("기숙사 919동 식당", "919동 1층");
    locations.put("자하연 식당", "109동 (자하연 옆)");
    locations.put("302동 식당", "302동");
    locations.put("동원관 식당", "113동 (경영대 옆)");
    locations.put("감골 식당", "101동 (사회대 옆)");
    locations.put("사범대 4식당", "76동 (사범대 옆)");
    locations.put("두레미담", "75-1동 5층 (3식당 위)");
    locations.put("301동 식당", "301동");
    locations.put("공대 간이 식당", "30-2동 (중도에서 쭉)");
    locations.put("상아회관 식당", "연건캠퍼스 19동");
    locations.put("220동 식당", "220동 (생활과학대 옆)");
    locations.put("대학원 기숙사 식당", "901동 1층");

    matchings.put("학생회관식당", "학생회관 식당");
    matchings.put("3식당", "농생대 3식당");
    matchings.put("기숙사식당", "기숙사 919동 식당");
    matchings.put("자하연식당", "자하연 식당");
    matchings.put("302동식당", "302동 식당");
    matchings.put("동원관식당", "동원관 식당");
    matchings.put("감골식당", "감골 식당");
    matchings.put("4식당", "사범대 4식당");
    matchings.put("두레미담", "두레미담");
    matchings.put("301동식당", "301동 식당");
    matchings.put("공대간이식당", "공대 간이 식당");
    matchings.put("상아회관", "상아회관 식당");
    matchings.put("220동식당", "220동 식당");
    matchings.put("대학원기숙사식당", "대학원 기숙사 식당");
  }
}
