package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;

public class NotifyWidgetDialog extends Dialog {
  private TextView title;
  private TextView message;
  private Button closeButton;

  public NotifyWidgetDialog(final Context context) {
    super(context);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.notify_widget_dialog);

    setUIComponents();

    closeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
  }

  private void setUIComponents() {
    title = (TextView) findViewById(R.id.notify_widget_dialog_title);
    message = (TextView) findViewById(R.id.notify_widget_dialog_message);
    closeButton = (Button) findViewById(R.id.notify_widget_dialog_close_button);

    title.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    message.setTypeface(FontUtil.fontAPAritaDotumMedium);
    closeButton.setTypeface(FontUtil.fontAPAritaDotumMedium);
  }
}
