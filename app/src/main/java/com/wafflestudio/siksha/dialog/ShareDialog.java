package com.wafflestudio.siksha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.form.Food;
import com.wafflestudio.siksha.rate.RatingViewManager;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.UnitConverter;

import java.util.List;

public class ShareDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private String restaurant;
    private List<Food> foods;
    private int index;

    public ShareDialog(Context context, String restaurant, List<Food> foods, int index) {
        super(context, R.style.ShareDialog);
        setContentView(R.layout.share_dialog);

        this.context = context;
        this.restaurant = restaurant;
        this.foods = foods;
        this.index = index;

        LinearLayout titleViewWrapper = (LinearLayout) findViewById(R.id.share_dialog_title_view_wrapper);
        TextView titleView = (TextView) findViewById(R.id.share_dialog_title_view);
        FloatingActionButton kakaotalkButton = (FloatingActionButton) findViewById(R.id.share_dialog_kakaotalk_button);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            float radius = UnitConverter.convertDpToPx(15.0f);
            float[] radii = {radius, radius, radius, radius, 0.0f, 0.0f, 0.0f, 0.0f};
            changeBackgroundDrawable(titleViewWrapper, R.color.color_primary, radii);
        }

        titleView.setTypeface(Fonts.fontBMJua);

        kakaotalkButton.setOnClickListener(this);
    }

    private void changeBackgroundDrawable(View view, int colorResourceID, float[] radii) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(colorResourceID));
        gradientDrawable.setCornerRadii(radii);

        view.setBackgroundDrawable(gradientDrawable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_dialog_kakaotalk_button:
                try {
                    final KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
                    final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                    kakaoTalkLinkMessageBuilder.addText(buildMessage());
                    kakaoTalkLinkMessageBuilder.addAppButton("식샤하러 가기!",
                            new AppActionBuilder()
                                    .addActionInfo(AppActionInfoBuilder
                                            .createAndroidActionInfoBuilder()
                                            .setExecuteParam("execparamkey1=1111")
                                            .setMarketParam("referrer=kakaotalklink")
                                            .build())
                                    .addActionInfo(AppActionInfoBuilder
                                            .createiOSActionInfoBuilder()
                                            .setExecuteParam("execparamkey1=1111")
                                            .build())
                                    .build());
                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), context);
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private String buildMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Date.getPrimaryTimestamp(Date.TYPE_NORMAL) + " " + Date.getTimeSlot(index)).append("\n");
        stringBuilder.append("[" + restaurant + "]").append("\n");

        int size = foods.size();
        if (size == 0)
            stringBuilder.append("\n").append(context.getString(R.string.empty_menu));
        else {
            for (int i = 0; i < size; i++) {
                Food food = foods.get(i);
                String rating = RatingViewManager.buildString(food.rating);
                stringBuilder.append("\n").append(food.price + "원 " + food.name + rating);
            }
        }

        return stringBuilder.toString();
    }
}
