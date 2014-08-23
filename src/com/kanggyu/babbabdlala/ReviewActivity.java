package com.kanggyu.babbabdlala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewActivity extends Activity implements OnClickListener
{
	private Intent intent;
	
	private Button submit;
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_review);
		
		intent = getIntent();
		
		submit = (Button)findViewById(R.id.submit);
		title = (TextView)findViewById(R.id.activity_review_title);	
	}
	
	public void onClick(View v)
	{
		int id = v.getId();
		
		switch(id)
		{
			case R.id.submit :
				Toast.makeText(this, "저장 완료.", Toast.LENGTH_SHORT).show();
		}
	}
}
