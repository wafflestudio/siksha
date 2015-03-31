package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;

public class ProgressDialog extends Dialog {
  private Context context;

  private Animation animation;
  private String message;

  private ImageView progress;

  public ProgressDialog(Context context, String message) {
    super(context, R.style.progress_dialog);
    setContentView(R.layout.progress_dialog);

    this.context = context;
    this.message = message;

    initSetting();
  }

  private void initSetting() {
    setCanceledOnTouchOutside(false);

    progress = (ImageView) findViewById(R.id.progress_image);
    TextView title = (TextView) findViewById(R.id.progress_dialog_title);

    title.setText(message);
    title.setTypeface(FontUtil.fontAPAritaDotumMedium);

    animation = AnimationUtils.loadAnimation(context, R.anim.loading);
  }

  public void startShowing() {
    progress.startAnimation(animation);
    show();
  }

  public void quitShowing() {
    progress.clearAnimation();
    dismiss();
  }
}