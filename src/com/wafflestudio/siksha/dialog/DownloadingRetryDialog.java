package com.wafflestudio.siksha.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.InitialLoadingMenu;
import com.wafflestudio.siksha.util.NetworkUtil;

public class DownloadingRetryDialog extends Dialog {
  private Button positiveButton;
  private Button negativeButton;

  public DownloadingRetryDialog(final Context context, final InitialLoadingMenu initialLoadingMenu, final int option, final String downloadingDate) {
    super(context);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.downloading_retry_dialog);

    setCanceledOnTouchOutside(false);
    setCancelable(false);
    setUIComponents();

    positiveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (NetworkUtil.getInstance().isOnline()) {
          dismiss();
          initialLoadingMenu.startDownloadingService(context, option, downloadingDate);
        }
        else
          Toast.makeText(context, R.string.downloading_network_state, Toast.LENGTH_SHORT).show();
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

  private void setUIComponents() {
    TextView title = (TextView) findViewById(R.id.downloading_dialog_title);
    TextView message = (TextView) findViewById(R.id.downloading_dialog_message);
    positiveButton = (Button) findViewById(R.id.downloading_dialog_positive_button);
    negativeButton = (Button) findViewById(R.id.downloading_dialog_negative_button);

    title.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    message.setTypeface(FontUtil.fontAPAritaDotumMedium);
    positiveButton.setTypeface(FontUtil.fontAPAritaDotumMedium);
    negativeButton.setTypeface(FontUtil.fontAPAritaDotumMedium);
  }
}
