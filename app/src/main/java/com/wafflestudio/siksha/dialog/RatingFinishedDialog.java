package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.UnitConverter;

/**
 * Created by Jooh on 2016-11-07.
 */
public class RatingFinishedDialog extends Dialog {
    private Context context;

    public RatingFinishedDialog(Dialog predialog, Context context) {
        super(context, R.style.RatingFinishedDialog);
        setContentView(R.layout.rating_finished_dialog);
        predialog.dismiss();

        LinearLayout headerView = (LinearLayout) findViewById(R.id.rating_finished_dialog_header_view);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            float radius = UnitConverter.convertDpToPx(15.0f);
            float[] radii = {radius, radius, radius, radius, 0.0f, 0.0f, 0.0f, 0.0f};
            changeBackgroundDrawable(headerView, R.color.color_primary, radii);
        }
    }

    private void changeBackgroundDrawable(View view, int colorResourceID, float[] radii) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(colorResourceID));
        gradientDrawable.setCornerRadii(radii);

        view.setBackgroundDrawable(gradientDrawable);
    }
}
