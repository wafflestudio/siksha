package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Fonts;

public class ProgressDialog extends Dialog {
    private ImageView progressView;

    private Animation loadingAnimation;

    public ProgressDialog(Context context) {
        super(context, R.style.ProgressDialog);
        setContentView(R.layout.progress_dialog);
        setCanceledOnTouchOutside(false);

        progressView = (ImageView) findViewById(R.id.progress_view);
        TextView explanationView = (TextView) findViewById(R.id.progress_dialog_explanation_view);
        TextView titleView = (TextView) findViewById(R.id.progress_dialog_title_view);
        TextView messageView = (TextView) findViewById(R.id.progress_dialog_message_view);

        loadingAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_infinite);

        explanationView.setTypeface(Fonts.fontBMJua);
        titleView.setTypeface(Fonts.fontBMJua);
        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
    }

    public void start() {
        progressView.startAnimation(loadingAnimation);
        show();
    }

    public void quit() {
        progressView.clearAnimation();
        dismiss();
    }
}