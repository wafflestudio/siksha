package com.kanggyu.babbabdlala;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

public class CafeInfoDialog extends Dialog
{
	private TextView hourTitle;
	private TextView hourDetails;
	private TextView locationTitle;
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
