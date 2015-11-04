package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Fonts;

public class WidgetAlertDialog extends Dialog {
    public WidgetAlertDialog(final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.widget_alert_dialog);

        TextView titleView = (TextView) findViewById(R.id.widget_alert_dialog_title_view);
        TextView messageView = (TextView) findViewById(R.id.widget_alert_dialog_message_view);
        Button closeButton = (Button) findViewById(R.id.widget_alert_dialog_close_button);

        titleView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        closeButton.setTypeface(Fonts.fontAPAritaDotumMedium);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
