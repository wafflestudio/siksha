package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;

public class TutorialDialog extends Dialog {
  private Context context;

  private String message;
  private int imageIndex;

  private TextView title;
  private Button dismiss;
  private Button next;

  public TutorialDialog(Context context, String message) {
    super(context, R.style.tutorial_dialog);
    setContentView(R.layout.tutorial_dialog);

    this.context = context;
    this.message = message;

    imageIndex = 0;

    initSetting();
  }

  private void initSetting() {
    setCanceledOnTouchOutside(false);

    title = (TextView) findViewById(R.id.tutorial_dialog_title);
    next = (Button) findViewById(R.id.tutorial_next_button);

    RelativeLayout dialog = (RelativeLayout) findViewById(R.id.tutorial_dialog);
    dialog.setBackgroundResource(R.drawable.s1);

    next.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                switch (imageIndex) {
                  case 0:
                    RelativeLayout dialog = (RelativeLayout) findViewById(R.id.tutorial_dialog);
                    dialog.setBackgroundResource(R.drawable.s2);
                    next.setText("finish");
                    break;

                  case 1:
                    quitShowing();
                    break;

                  default:
                    quitShowing();
                    break;
                }
                imageIndex++;
              }
            }
    );

    title.setText(message);
    title.setTypeface(FontUtil.fontAPAritaDotumMedium);
  }

  public void startShowing() {
    show();
  }

  public void quitShowing() {
    dismiss();
  }
}
