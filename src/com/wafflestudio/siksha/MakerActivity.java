package com.wafflestudio.siksha;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.wafflestudio.siksha.util.FontUtil;

public class MakerActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maker);

    TextView maker = (TextView) findViewById(R.id.maker);
    TextView wafflestudio = (TextView) findViewById(R.id.wafflestudio_name);
    TextView shaidea = (TextView) findViewById(R.id.shaidea_name);

    maker.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    wafflestudio.setTypeface(FontUtil.fontAPAritaDotumMedium);
    shaidea.setTypeface(FontUtil.fontAPAritaDotumMedium);
  }
}
