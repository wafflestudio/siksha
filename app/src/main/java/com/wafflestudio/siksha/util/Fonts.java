package com.wafflestudio.siksha.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Fonts {
    private static Fonts instance;

    public static Typeface fontAPAritaDotumMedium; // 아모레퍼시픽 아리따돋움체 Medium
    public static Typeface fontAPAritaDotumSemiBold; // 아모레퍼시픽 아리따돋움체 SemiBold
    public static Typeface fontBMJua; // 배달의민족 주아체

    private Fonts() { }

    public static synchronized Fonts getInstance() {
        if (instance == null)
            instance = new Fonts();

        return instance;
    }

    public void initialize(Context context) {
        AssetManager assetManager = context.getAssets();

        if (fontAPAritaDotumMedium == null)
            fontAPAritaDotumMedium = Typeface.createFromAsset(assetManager, "Arita-Dotum-Medium.otf");
        if (fontAPAritaDotumSemiBold == null)
            fontAPAritaDotumSemiBold = Typeface.createFromAsset(assetManager, "Arita-Dotum-SemiBold.otf");
        if (fontBMJua == null)
            fontBMJua = Typeface.createFromAsset(assetManager, "BM-Jua.ttf");
    }
}
