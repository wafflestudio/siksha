package com.wafflestudio.siksha;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.wafflestudio.siksha.util.FontUtil;

public class DeveloperActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_developer);

    TextView developer = (TextView) findViewById(R.id.developer);
    TextView wafflestudio = (TextView) findViewById(R.id.wafflestudio_name);
    TextView shaidea = (TextView) findViewById(R.id.shaidea_name);

    developer.setTypeface(FontUtil.fontBMJua);
    wafflestudio.setTypeface(FontUtil.fontAPAritaDotumMedium);
    shaidea.setTypeface(FontUtil.fontAPAritaDotumMedium);
  }
}
