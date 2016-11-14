package com.wafflestudio.siksha.rate;

import android.widget.TextView;

/**
 * Created by Jooh on 2016-11-13.
 */
public class RatingViewManager {

    public static void buildView (String rating, TextView ratingView) {
        if(rating != null) {
            float rate = Float.parseFloat(rating);
            ratingView.setText("★ "+String.format("%.1f", rate));
        }

        else {
            ratingView.setText("☆ - -");
        }
    }

    public static void buildView (Float rating, TextView ratingView) {
        if(rating != null) {
            ratingView.setText("★ "+String.format("%.1f", rating));
        }

        else {
            ratingView.setText("☆ - -");
        }
    }

    public static String buildString(String rating) {
        if(rating != null) {
            Float rate = Float.parseFloat(rating);
            return " ★ "+String.format("%.1f", rate);
        }

        else {
            return " ☆ - -";
        }
    }
}
