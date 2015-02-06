package com.wafflestudio.babbabdirara;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import java.util.HashMap;

public class CafeInfoDialog extends Dialog
{
	private TextView hourDetails;
	private TextView locationDetails;
	
	public CafeInfoDialog(Context context, String cafeName, HashMap<String, String> operatingHours, HashMap<String, String> locations)
	{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cafe_info);
		
		hourDetails = (TextView)findViewById(R.id.operating_hour_details);
		locationDetails = (TextView)findViewById(R.id.location_details);
		hourDetails.setText(operatingHours.get(cafeName));
		locationDetails.setText(locations.get(cafeName));
	}
}
