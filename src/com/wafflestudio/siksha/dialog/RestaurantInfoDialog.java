package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
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

  private View topBar;
  private ImageView dialogIcon;

	public RestaurantInfoDialog(Context context, String restaurantName, int pageIndex) {
		super(context, R.style.restaurant_info_dialog);
    setContentView(R.layout.restaurant_info_dialog);

    restaurantView = (TextView) findViewById(R.id.restaurant_info_dialog_title);
    operatingHoursTitle = (TextView) findViewById(R.id.operating_hour);
    locationTitle = (TextView) findViewById(R.id.location);
    operatingHoursDetails = (TextView) findViewById(R.id.operating_hour_details);
		locationDetails = (TextView) findViewById(R.id.location_details);

    topBar = findViewById(R.id.restaurant_info_dialog_top_bar);
    dialogIcon = (ImageView) findViewById(R.id.restaurant_info_dialog_icon);

    if (pageIndex == 0) {
      topBar.setBackgroundResource(R.drawable.rounded_dialog_roof_breakfast);
      dialogIcon.setImageResource(R.drawable.ic_launcher_breakfast);
      restaurantView.setTextColor(Color.parseColor("#5abec6"));
    }
    else if (pageIndex == 1) {
      topBar.setBackgroundResource(R.drawable.rounded_dialog_roof_lunch);
      dialogIcon.setImageResource(R.drawable.ic_launcher);
      restaurantView.setTextColor(Color.parseColor("#f48a5b"));
    }
    else {
      topBar.setBackgroundResource(R.drawable.rounded_dialog_roof_dinner);
      dialogIcon.setImageResource(R.drawable.ic_launcher_dinner);
      restaurantView.setTextColor(Color.parseColor("#7171c9"));
    }

    restaurantView.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    operatingHoursTitle.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    locationTitle.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
    operatingHoursDetails.setTypeface(FontUtil.fontAPAritaDotumMedium);
    locationDetails.setTypeface(FontUtil.fontAPAritaDotumMedium);

    restaurantView.setText(restaurantName);
    operatingHoursDetails.setText(RestaurantInfoUtil.getInstance().operatingHourMap.get(restaurantName));
		locationDetails.setText(RestaurantInfoUtil.getInstance().locationMap.get(restaurantName));
	}
}
