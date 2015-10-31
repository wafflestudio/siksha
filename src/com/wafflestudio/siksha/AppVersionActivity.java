package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.Preference;

public class AppVersionActivity extends Activity {
    private CardView feedbackWrapper;

    private TextView messageView;
    private TextView feedbackMessageView;
    private ImageView feedbackImageView;

    private String currentAppVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_version);

        TextView titleView = (TextView) findViewById(R.id.app_version_activity_title_view);
        messageView = (TextView) findViewById(R.id.app_version_activity_message_view);

        feedbackWrapper = (CardView) findViewById(R.id.feedback_wrapper);
        feedbackMessageView = (TextView) findViewById(R.id.feedback_message_view);
        feedbackImageView = (ImageView) findViewById(R.id.feedback_image_view);

        titleView.setTypeface(Fonts.fontBMJua);
        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        feedbackMessageView.setTypeface(Fonts.fontAPAritaDotumMedium);

        currentAppVersion = Preference.loadStringValue(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_CURRENT_APP_VERSION);
        messageView.setText("현재 버전 : " + currentAppVersion);
        showFeedback();
    }

    private void showFeedback() {
        String latestAppVersion = Preference.loadStringValue(this, Preference.PREF_APP_NAME, Preference.PREF_KEY_LATEST_APP_VERSION);

        if (latestAppVersion != null) {
            if (currentAppVersion.compareTo(latestAppVersion) > 0)
                feedbackMessageView.setText(R.string.development_version);
            else if (currentAppVersion.compareTo(latestAppVersion) == 0)
                feedbackMessageView.setText(R.string.now_latest);
            else if (currentAppVersion.compareTo(latestAppVersion) < 0) {
                feedbackMessageView.setText(R.string.not_latest);
                feedbackImageView.setImageResource(R.drawable.ic_link);
                feedbackWrapper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=com.wafflestudio.siksha"));
                        startActivity(intent);
                    }
                });
            }

            feedbackWrapper.setVisibility(View.VISIBLE);
        }
    }
}
