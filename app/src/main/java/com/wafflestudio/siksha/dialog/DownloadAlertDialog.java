package com.wafflestudio.siksha.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.service.JSONDownloadReceiver;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.NetworkChecker;

public class DownloadAlertDialog extends Dialog {
    public DownloadAlertDialog(final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.download_alert_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        TextView titleView = (TextView) findViewById(R.id.download_alert_dialog_title_view);
        TextView messageView = (TextView) findViewById(R.id.download_alert_dialog_message_view);
        Button positiveButton = (Button) findViewById(R.id.download_alert_dialog_positive_button);
        Button negativeButton = (Button) findViewById(R.id.download_alert_dialog_negative_button);

        titleView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        positiveButton.setTypeface(Fonts.fontAPAritaDotumMedium);
        negativeButton.setTypeface(Fonts.fontAPAritaDotumMedium);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkChecker.getInstance().isOnline(context)) {
                    dismiss();
                    ((MainActivity) context).downloadMenuData(JSONDownloadReceiver.ACTION_MENU_DOWNLOAD, true);
                }
                else {
                    Toast.makeText(context, R.string.check_network_state, Toast.LENGTH_SHORT).show();
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ((Activity) context).finish();
            }
        });
    }
}
