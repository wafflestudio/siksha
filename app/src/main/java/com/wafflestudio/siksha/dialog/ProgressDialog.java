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

public class ProgressDialog extends Dialog {
    private ImageView progressView;

    public ProgressDialog(Context context) {
        super(context, R.style.ProgressDialog);
        setContentView(R.layout.progress_dialog);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        initialize();
    }

    private void initialize() {
        progressView = (ImageView) findViewById(R.id.progress_view);
        TextView messageView = (TextView) findViewById(R.id.progress_dialog_message_view);

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