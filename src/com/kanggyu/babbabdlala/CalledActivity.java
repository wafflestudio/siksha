package com.kanggyu.babbabdlala;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CalledActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView tv = new TextView(this);
		tv.setText("호출된 Activity 입니다.");
		tv.setTextSize(20);
		setContentView(tv);
		
	}
}
