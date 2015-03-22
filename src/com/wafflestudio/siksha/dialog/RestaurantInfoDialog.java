package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;

public class RestaurantInfoDialog extends Dialog {
  private TextView restaurantView;

  private TextView operatingHoursTitle;
  private TextView locationTitle;
	private TextView operatingHoursDetails;
	private TextView locationDetails;

	public RestaurantInfoDialog(Context context, String restaurantName) {
		super(context, R.style.restaurant_info_dialog);
    setContentView(R.layout.restaurant_info_dialog);

    restaurantView = (TextView) findViewById(R.id.restaurant_info_dialog_title);
    operatingHoursTitle = (TextView) findViewById(R.id.operating_hour);
    locationTitle = (TextView) findViewById(R.id.location);
    operatingHoursDetails = (TextView) findViewById(R.id.operating_hour_details);
		locationDetails = (TextView) findViewById(R.id.location_details);

    restaurantView.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    operatingHoursTitle.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    locationTitle.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    operatingHoursDetails.setTypeface(FontUtil.fontAPAritaDotumMedium);
    locationDetails.setTypeface(FontUtil.fontAPAritaDotumMedium);

    restaurantView.setText(restaurantName);
    operatingHoursDetails.setText(RestaurantInfoUtil.getInstance().operatingHoursMap.get(restaurantName));
		locationDetails.setText(RestaurantInfoUtil.getInstance().locationsMap.get(restaurantName));
	}
}
