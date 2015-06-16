package com.wafflestudio.siksha;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wafflestudio.siksha.util.AppVersion;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.NetworkUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppVersionActivity extends Activity {
  private LinearLayout updateFeedback;

  private TextView appVersionMessage;
  private TextView feedbackMessage;
  private ImageView feedbackIcon;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_version);

    TextView title = (TextView) findViewById(R.id.app_version);
    appVersionMessage = (TextView) findViewById(R.id.app_version_message);

    updateFeedback = (LinearLayout) findViewById(R.id.update_feedback_wrapper);
    feedbackMessage = (TextView) findViewById(R.id.feedback_message);
    feedbackIcon = (ImageView) findViewById(R.id.feedback_icon);

    title.setTypeface(FontUtil.fontBMJua);
    appVersionMessage.setTypeface(FontUtil.fontAPAritaDotumMedium);
    feedbackMessage.setTypeface(FontUtil.fontAPAritaDotumMedium);

    checkAppVersion();
  }

  private void checkAppVersion() {
    String versionName = "";

    try {
      versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
      appVersionMessage.setText("현재 버전 : " + versionName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    final String currentAppVersion = versionName;

    Thread thread = new Thread() {
      StringBuilder data = new StringBuilder();

      @Override
      public void run() {
        try {
          URL url = new URL("http://siksha.kr:3280/version");
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setConnectTimeout(5 * 1000);
          connection.setReadTimeout(5 * 1000);

          if (connection.getResponseCode() == 200) {
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
              data.append(line);
            }
            is.close();
            br.close();
          } else {
            connection.disconnect();
            data = null;
          }
        } catch (Exception e) {
          e.printStackTrace();
          data = null;
        }

        if (data != null) {
          Gson gson = new Gson();
          final AppVersion appVersion = gson.fromJson(data.toString(), AppVersion.class);

          new android.os.Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
              if (appVersion != null) {
                if (currentAppVersion.compareTo(appVersion.latest) == 0)
                  feedbackMessage.setText(R.string.now_latest);
                else if (currentAppVersion.compareTo(appVersion.latest) < 0) {
                  feedbackMessage.setText(R.string.no_latest);
                  feedbackIcon.setImageResource(R.drawable.ic_link_black);
                  updateFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      Intent intent = new Intent(Intent.ACTION_VIEW);
                      intent.setData(Uri.parse("market://details?id=com.wafflestudio.siksha"));
                      startActivity(intent);
                    }
                  });
                }

                updateFeedback.setVisibility(View.VISIBLE);
              }
            }
          }.sendEmptyMessage(0);
        }
      }
    };

    if (NetworkUtil.getInstance().isOnline())
      thread.start();
    else
      Toast.makeText(this, R.string.get_latest_version_error, Toast.LENGTH_LONG).show();
  }
}
