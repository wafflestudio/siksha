package com.wafflestudio.siksha.rate;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.page.ChildRecyclerViewAdapter;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.NetworkChecker;
import com.wafflestudio.siksha.util.Preference;
import com.wafflestudio.siksha.util.UnitConverter;

/**
 * Created by Jooh on 2016-11-02.
 */
public class RatingDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private String restaurant;
    private String meal;

    private RatingBar ratingbar;
    private int numOfRatingToday;

    private ChildRecyclerViewAdapter.RefreshListener refreshListener;

    public RatingDialog(Context context, String restaurant, String meal, ChildRecyclerViewAdapter.RefreshListener refreshListener) {
        super(context, R.style.RatingDialog);
        setContentView(R.layout.rating_dialog);

        this.context = context;
        this.refreshListener = refreshListener;
        this.restaurant = restaurant;
        this.meal = meal;
        numOfRatingToday = Preference.loadIntValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_NUMBER_OF_RATING_TODAY);

        setViews();
        setRatingbar();
    }

    private void setViews() {
        LinearLayout headerView = (LinearLayout) findViewById(R.id.rating_dialog_header_view);
        ImageButton cancelButton = (ImageButton) findViewById(R.id.rating_no);
        ImageButton ratingButton = (ImageButton) findViewById(R.id.rating_yes);
        cancelButton.setOnClickListener(this);
        ratingButton.setOnClickListener(this);

        TextView titleView = (TextView)  findViewById(R.id.rating_dialog_title_view);
        TextView restaurantView = (TextView) findViewById(R.id.restaurant_textview);
        TextView restaurantNameView = (TextView) findViewById(R.id.restaurant_name_textview);
        TextView mealView = (TextView) findViewById(R.id.food_textview);
        TextView mealNameView = (TextView) findViewById(R.id.food_name_textview);
        TextView alertView = (TextView) findViewById(R.id.rating_alert);

        restaurantNameView.setText(restaurant);
        mealNameView.setText(meal);

        String temp = alertView.getText().toString();
        alertView.setText( temp + "(" + (3 - numOfRatingToday) + "회 남음)");

        titleView.setTypeface(Fonts.fontBMJua);
        restaurantView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        restaurantNameView.setTypeface(Fonts.fontAPAritaDotumMedium);
        mealView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        mealNameView.setTypeface(Fonts.fontAPAritaDotumMedium);
        alertView.setTypeface(Fonts.fontAPAritaDotumMedium);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            float radius = UnitConverter.convertDpToPx(15.0f);
            float[] radii = {radius, radius, radius, radius, 0.0f, 0.0f, 0.0f, 0.0f};
            changeBackgroundDrawable(headerView, R.color.color_primary, radii);
        }
    }

    private void setRatingbar() {
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        ratingbar.setRating((float) 0.5); // initial Value = 0.5

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override public void onRatingChanged(RatingBar ratingBar, float rating,
                                                  boolean fromUser) {
                if (rating < 0.5) {
                    ratingbar.setRating((float) 0.5);  // set minimum value -> 0.5
                }
            }
        });
    }

    private void changeBackgroundDrawable(View view, int colorResourceID, float[] radii) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(colorResourceID));
        gradientDrawable.setCornerRadii(radii);

        view.setBackgroundDrawable(gradientDrawable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rating_yes:
                if (!checkDuplication()) {
                    Toast.makeText(context, R.string.already_rated_three_time, Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!NetworkChecker.isOnline(context)) {
                    Toast.makeText(context, R.string.check_network_state, Toast.LENGTH_SHORT).show();
                }
                else {
                    float rating = ratingbar.getRating();
                    RatingRequestManager requestManager = new RatingRequestManager(context);
                    requestManager.ratingPost(restaurant, meal, rating, this);
                }

                break;
            case R.id.rating_no:

                dismiss();
                break;
        }
    }

    public void ratingSuccess() {
        Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_LAST_RATING_TIMESTAMP, Date.getDayOfYear());
        Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_NUMBER_OF_RATING_TODAY, numOfRatingToday +1);

        refreshListener.refresh(ratingbar.getRating());
        this.dismiss();
        new RatingFinishedDialog(context, 2-numOfRatingToday).show();
    }

    private boolean checkDuplication() {

        if (numOfRatingToday < 3) {
            return true;
        }
        else {
            return false;
        }
    }
}
