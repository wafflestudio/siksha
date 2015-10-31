package com.wafflestudio.siksha.widget;

import android.app.Activity;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.Preference;

/**
 * Created by Gyeongin on 2015-03-26.
 */

public class WidgetBreakfastCheckDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int appWidgetID;

    public WidgetBreakfastCheckDialog (Context context, int appWidgetID) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.widget_breakfast_check_dialog);
        setCanceledOnTouchOutside(false);

        this.context = context;
        this.appWidgetID = appWidgetID;

        TextView messageView = (TextView) findViewById(R.id.widget_breakfast_check_dialog_message_view);
        Button positiveButton = (Button) findViewById(R.id.widget_breakfast_check_dialog_positive_button);
        Button negativeButton = (Button) findViewById(R.id.widget_breakfast_check_dialog_negative_button);

        messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        positiveButton.setTypeface(Fonts.fontAPAritaDotumMedium);
        negativeButton.setTypeface(Fonts.fontAPAritaDotumMedium);

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.widget_breakfast_check_dialog_positive_button:
                Preference.save(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_BREAKFAST_PREFIX + appWidgetID, true);
                break;
            case R.id.widget_breakfast_check_dialog_negative_button:
                Preference.save(context, Preference.PREF_WIDGET_NAME, Preference.PREF_KEY_BREAKFAST_PREFIX + appWidgetID, false);
                break;
        }

        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
        intent.setAction(WidgetProvider.STATE_CONFIGURATION_FINISHED);
        context.sendBroadcast(intent);

        ((Activity) context).setResult(Activity.RESULT_OK, intent);
        ((Activity) context).finish();
    }
}
