package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.Rating;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.service.RatingRequestManager;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.UnitConverter;

/**
 * Created by Jooh on 2016-11-02.
 */
public class RatingDialog extends Dialog implements View.OnClickListener{
    private Context context;

    private String restaurant;
    private String food;

    private RatingBar ratingbar;

    

    public RatingDialog(Context context, String restaurant, String food) {
        super(context, R.style.RatingDialog);
        setContentView(R.layout.rating_dialog);

        this.context = context;
        this.restaurant = restaurant;
        this.food = food;

    
        LinearLayout headerView = (LinearLayout) findViewById(R.id.rating_dialog_header_view);
        ImageButton cancelButton = (ImageButton) findViewById(R.id.rating_no);
        ImageButton ratingButton = (ImageButton) findViewById(R.id.rating_yes);
        cancelButton.setOnClickListener(this);
        ratingButton.setOnClickListener(this);

        TextView title_view = (TextView)  findViewById(R.id.rating_dialog_title_view);
        TextView restaurant_view = (TextView) findViewById(R.id.restaurant_textview);
        TextView food_view = (TextView) findViewById(R.id.food_textview);
        TextView alert_view = (TextView) findViewById(R.id.rating_alert);

        restaurant_view.setText("식당 : "+restaurant);
        food_view.setText("메뉴 : "+food);

        title_view.setTypeface(Fonts.fontBMJua);
        restaurant_view.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        food_view.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        alert_view.setTypeface(Fonts.fontAPAritaDotumSemiBold);

        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        ratingbar.setRating(0);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            float radius = UnitConverter.convertDpToPx(15.0f);
            float[] radii = {radius, radius, radius, radius, 0.0f, 0.0f, 0.0f, 0.0f};
            changeBackgroundDrawable(headerView, R.color.color_primary, radii);
        }
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

                float rating = ratingbar.getRating();
                RatingRequestManager requestmanager = new RatingRequestManager(context);
                requestmanager.postRating(restaurant,food,rating);

                dismiss();
                break;
            case R.id.rating_no:

                dismiss();
                break;
        }
    }
}
