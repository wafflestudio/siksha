package com.wafflestudio.siksha;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.wafflestudio.siksha.util.FontUtil;

public class MakerActivity extends Activity {
  private TextView maker;
  private TextView wafflestudio;
  private TextView shaidea;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maker);

    maker = (TextView) findViewById(R.id.maker);
    wafflestudio = (TextView) findViewById(R.id.wafflestudio_name);
    shaidea = (TextView) findViewById(R.id.shaidea_name);

    maker.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    wafflestudio.setTypeface(FontUtil.fontAPAritaDotumMedium);
    shaidea.setTypeface(FontUtil.fontAPAritaDotumMedium);
  }
}
