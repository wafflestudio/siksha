package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.AppData;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.UnitConverter;

public class InformationDialog extends Dialog {
    private Context context;

    public InformationDialog(Context context, String name) {
        super(context, R.style.InformationDialog);
        setContentView(R.layout.information_dialog);

        this.context = context;

        LinearLayout headerView = (LinearLayout) findViewById(R.id.information_dialog_header_view);
        TextView nameView = (TextView) findViewById(R.id.information_dialog_title_view);
        TextView operatingHourTitleView = (TextView) findViewById(R.id.operating_hour_title_view);
        TextView locationTitleView = (TextView) findViewById(R.id.location_title_view);
        TextView operatingHourMessageView = (TextView) findViewById(R.id.operating_hour_message_view);
        TextView locationMessageView = (TextView) findViewById(R.id.location_message_view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            float radius = UnitConverter.convertDpToPx(15.0f);
            float[] radii = {radius, radius, radius, radius, 0.0f, 0.0f, 0.0f, 0.0f};
            changeBackgroundDrawable(headerView, R.color.color_primary, radii);
        }
        nameView.setTextColor(context.getResources().getColor(R.color.color_primary));

        nameView.setTypeface(Fonts.fontBMJua);
        operatingHourTitleView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        locationTitleView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        operatingHourMessageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        locationMessageView.setTypeface(Fonts.fontAPAritaDotumMedium);

        nameView.setText(name);
        operatingHourMessageView.setText(AppData.getInstance().getInformationDictionary().get(name).operatingHour);
        locationMessageView.setText(AppData.getInstance().getInformationDictionary().get(name).location);
    }

    private void changeBackgroundDrawable(View view, int colorResourceID, float[] radii) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(colorResourceID));
        gradientDrawable.setCornerRadii(radii);

        view.setBackgroundDrawable(gradientDrawable);
    }
}
