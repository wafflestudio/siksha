package com.wafflestudio.siksha.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FontUtil {
  private static FontUtil fontUtil;

  public static Typeface fontAPAritaDotumMedium; // 아모레퍼시픽 아리따돋움체 Medium
  public static Typeface fontAPAritaDotumSemiBold; // 아모레퍼시픽 아리따돋움체 SemiBold
  public static Typeface fontBMJua; // 배달의민족 주아체

  private FontUtil() { }

  public static FontUtil getInstance() {
    if (fontUtil == null)
      fontUtil = new FontUtil();

    return fontUtil;
  }

  public void setFontAsset(AssetManager assetManager) {
    if (fontAPAritaDotumMedium == null)
      fontAPAritaDotumMedium = Typeface.createFromAsset(assetManager, "Arita-Dotum-Medium.otf");
    if (fontAPAritaDotumSemiBold == null)
      fontAPAritaDotumSemiBold = Typeface.createFromAsset(assetManager, "Arita-Dotum-SemiBold.otf");
    if (fontBMJua == null)
      fontBMJua = Typeface.createFromAsset(assetManager, "BM-Jua.ttf");
  }
}
