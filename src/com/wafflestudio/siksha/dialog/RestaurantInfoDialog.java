package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;

public class RestaurantInfoDialog extends Dialog {
  public RestaurantInfoDialog(Context context, String restaurantName, int pageIndex) {
    super(context, R.style.RestaurantInfoDialog);
    setContentView(R.layout.restaurant_info_dialog);

    TextView restaurantView = (TextView) findViewById(R.id.restaurant_info_dialog_title);
    TextView operatingHoursTitle = (TextView) findViewById(R.id.operating_hour);
    TextView locationTitle = (TextView) findViewById(R.id.location);
    TextView operatingHoursDetails = (TextView) findViewById(R.id.operating_hour_details);
    TextView locationDetails = (TextView) findViewById(R.id.location_details);

    View topBar = findViewById(R.id.restaurant_info_dialog_top_bar);
    ImageView dialogIcon = (ImageView) findViewById(R.id.restaurant_info_dialog_icon);

    if (pageIndex == 0) {
      topBar.setBackgroundResource(R.drawable.rounded_dialog_roof_breakfast);
      dialogIcon.setImageResource(R.drawable.ic_launcher_breakfast);
      restaurantView.setTextColor(context.getResources().getColor(R.color.color_primary_breakfast));
    } else if (pageIndex == 1) {
      topBar.setBackgroundResource(R.drawable.rounded_dialog_roof_lunch);
      dialogIcon.setImageResource(R.drawable.ic_launcher);
      restaurantView.setTextColor(context.getResources().getColor(R.color.color_primary_lunch));
    } else {
      topBar.setBackgroundResource(R.drawable.rounded_dialog_roof_dinner);
      dialogIcon.setImageResource(R.drawable.ic_launcher_dinner);
      restaurantView.setTextColor(context.getResources().getColor(R.color.color_primary_dinner));
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
