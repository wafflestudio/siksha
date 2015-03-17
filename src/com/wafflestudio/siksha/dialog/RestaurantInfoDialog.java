package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.RestaurantInfo;

import java.util.HashMap;

public class RestaurantInfoDialog extends Dialog {
  private TextView operatingHoursTitle;
  private TextView locationTitle;
	private TextView operatingHoursDetails;
	private TextView locationDetails;

	public RestaurantInfoDialog(Context context, String restaurantName) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.restaurant_info_dialog);

    operatingHoursTitle = (TextView) findViewById(R.id.operating_hour);
    locationTitle = (TextView) findViewById(R.id.location);
    operatingHoursDetails = (TextView) findViewById(R.id.operating_hour_details);
		locationDetails = (TextView) findViewById(R.id.location_details);

    operatingHoursTitle.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    locationTitle.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    operatingHoursDetails.setTypeface(FontUtil.fontAPAritaDotumMedium);
    locationDetails.setTypeface(FontUtil.fontAPAritaDotumMedium);

    operatingHoursDetails.setText(RestaurantInfo.operatingHoursMap.get(restaurantName));
		locationDetails.setText(RestaurantInfo.locationsMap.get(restaurantName));
	}
}
