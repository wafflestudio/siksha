package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.UnitConverter;

import org.w3c.dom.Text;

/**
 * Created by Jooh on 2016-11-07.
 */
public class RatingFinishedDialog extends Dialog {
    private Context context;

    private int ratingRemain;

    public RatingFinishedDialog(Dialog predialog, Context context,int ratingRemain) {
        super(context, R.style.RatingFinishedDialog);
        setContentView(R.layout.rating_finished_dialog);

        this.context= context;

        this.ratingRemain = ratingRemain;
        predialog.dismiss();

        LinearLayout headerView = (LinearLayout) findViewById(R.id.rating_finished_dialog_header_view);

        TextView remainTextView = (TextView) findViewById(R.id.rating_remain_textview);
        TextView alertRefreshTextView = (TextView) findViewById(R.id.rating_refresh_alert);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            float radius = UnitConverter.convertDpToPx(15.0f);
            float[] radii = {radius, radius, radius, radius, 0.0f, 0.0f, 0.0f, 0.0f};
            changeBackgroundDrawable(headerView, R.color.color_primary, radii);
        }

        remainTextView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        alertRefreshTextView.setTypeface(Fonts.fontAPAritaDotumMedium);
        String temp = remainTextView.getText().toString();
        remainTextView.setText(temp + " " + ratingRemain);
    }

    private void changeBackgroundDrawable(View view, int colorResourceID, float[] radii) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(colorResourceID));
        gradientDrawable.setCornerRadii(radii);

        view.setBackgroundDrawable(gradientDrawable);
    }
}
