package com.wafflestudio.siksha.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Gyu Kang on 2015-11-06.
 */
public class UnitConverter {
    public static float STANDARD_DPI = 160.0f;

    public static float convertPxToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / (metrics.densityDpi / STANDARD_DPI);
    }

    public static float convertDpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / STANDARD_DPI);
    }
}
