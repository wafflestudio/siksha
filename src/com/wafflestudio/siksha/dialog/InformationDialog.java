package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.AppData;

public class InformationDialog extends Dialog {
    public InformationDialog(Context context, String name) {
        super(context, R.style.InformationDialog);
        setContentView(R.layout.information_dialog);

        TextView nameView = (TextView) findViewById(R.id.information_dialog_title_view);
        TextView operatingHourTitleView = (TextView) findViewById(R.id.operating_hour_title_view);
        TextView locationTitleView = (TextView) findViewById(R.id.location_title_view);
        TextView operatingHourMessageView = (TextView) findViewById(R.id.operating_hour_message_view);
        TextView locationMessageView = (TextView) findViewById(R.id.location_message_view);

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
}
