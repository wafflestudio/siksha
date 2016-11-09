package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.Rating;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.service.JSONDownloadReceiver;
import com.wafflestudio.siksha.service.RatingRequestManager;
import com.wafflestudio.siksha.service.RatingRequestQueueManager;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.NetworkChecker;
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
        TextView restaurant_nameview = (TextView) findViewById(R.id.restaurant_name_textview);
        TextView food_view = (TextView) findViewById(R.id.food_textview);
        TextView food_nameview = (TextView) findViewById(R.id.food_name_textview);
        TextView alert_view = (TextView) findViewById(R.id.rating_alert);

        restaurant_nameview.setText(restaurant);
        food_nameview.setText(food);

        title_view.setTypeface(Fonts.fontBMJua);

        restaurant_view.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        restaurant_nameview.setTypeface(Fonts.fontAPAritaDotumMedium);
        food_view.setTypeface(Fonts.fontAPAritaDotumSemiBold);
        food_nameview.setTypeface(Fonts.fontAPAritaDotumMedium);
        alert_view.setTypeface(Fonts.fontAPAritaDotumMedium);

        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        ratingbar.setRating((float) 0.5); // initial Value = 0.5

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override public void onRatingChanged(RatingBar ratingBar, float rating,
                                                  boolean fromUser) {
                if(rating < 0.5) {
                    ratingbar.setRating((float) 0.5);  // set minimum value -> 0.5
                }
            }
        });

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

                // progress dialog
                if (!NetworkChecker.getInstance().isOnline(context)){
                    Toast.makeText(context, R.string.check_network_state, Toast.LENGTH_SHORT).show();
                }
                else {
                    float rating = ratingbar.getRating();
                    RatingRequestManager requestmanager = new RatingRequestManager(context);
                    requestmanager.postRating(restaurant,food,rating, new VolleyCallback() {

                        @Override
                        public void onSuccess(String response) {
                            RatingSuccess();
                        }

                        @Override
                        public void onFailure() {
                            sendSignalToApp("POST_RATING", JSONDownloadReceiver.TYPE_ON_FAILURE);
                        }

                    });
                }

                break;
            case R.id.rating_no:

                dismiss();
                break;
        }
    }

    private void sendSignalToApp(String action, int callbackType) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("callback_type", callbackType);
        context.sendBroadcast(intent);
    }

    public void RatingSuccess() {

        new RatingFinishedDialog(this,context).show();
    }

    public interface VolleyCallback {
        void onSuccess(String response);
        void onFailure();
    }
}
