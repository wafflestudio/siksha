package com.wafflestudio.siksha.dialog;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Animations;
import com.wafflestudio.siksha.util.Fonts;

public class SplashDialog extends Dialog {
    private ImageView progressView;

    public SplashDialog(Context context) {
        super(context, R.style.SplashDialog);
        setContentView(R.layout.splash_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        initialize();
    }

    private void initialize() {
        progressView = (ImageView) findViewById(R.id.progress_view);
        TextView explanationView = (TextView) findViewById(R.id.splash_dialog_explanation_view);
        TextView titleView = (TextView) findViewById(R.id.splash_dialog_title_view);
        TextView messageView = (TextView) findViewById(R.id.splash_dialog_message_view);

        explanationView.setTypeface(Fonts.fontBMJua);
        titleView.setTypeface(Fonts.fontBMJua);
        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
    }

    public void start() {
        ObjectAnimator animator = Animations.makeRotateAnimator(progressView, 0.0f, 360.0f, 1000, true);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        show();
    }

    public void quit() {
        Animations.clear(progressView);
        dismiss();
    }
}
