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
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

/**
 * Created by Gyeongin on 2015-03-26.
 */

public class WidgetBreakfastCheckDialog extends Dialog {
    private TextView title;
    private TextView message;
    private Button positiveButton;
    private Button negativeButton;

    public WidgetBreakfastCheckDialog (final Context context, final int appWidgetId) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bab_widget_breakfast_check_dialog);

        setCanceledOnTouchOutside(false);
        setUIComponents();

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_BREAKFAST_KEY + appWidgetId, true);
                Intent intent = new Intent();
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
                intent.setAction(BabWidgetProvider.CONFIGURATION_FINISHED);
                context.sendBroadcast(intent);

                ((Activity) context).finish();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                SharedPreferenceUtil.save(context, SharedPreferenceUtil.PREF_WIDGET_NAME, SharedPreferenceUtil.PREF_PREFIX_BREAKFAST_KEY + appWidgetId, false);
                Intent intent = new Intent();
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
                intent.setAction(BabWidgetProvider.CONFIGURATION_FINISHED);
                context.sendBroadcast(intent);

                ((Activity) context).finish();
            }
        });
    }

    private void setUIComponents() {
        //title = (TextView) findViewById(R.id.breakfast_check_dialog_title);
        message = (TextView) findViewById(R.id.breakfast_check_dialog_message);
        positiveButton = (Button) findViewById(R.id.breakfast_check_dialog_positive_button);
        negativeButton = (Button) findViewById(R.id.breakfast_check_dialog_negative_button);

        //title.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
        message.setTypeface(FontUtil.fontAPAritaDotumMedium);
        positiveButton.setTypeface(FontUtil.fontAPAritaDotumMedium);
        negativeButton.setTypeface(FontUtil.fontAPAritaDotumMedium);
    }
}
