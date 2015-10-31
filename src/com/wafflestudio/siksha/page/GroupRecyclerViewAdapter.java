package com.wafflestudio.siksha.page;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.InformationDialog;
import com.wafflestudio.siksha.form.Menu;
import com.wafflestudio.siksha.form.Restaurant;
import com.wafflestudio.siksha.page.bookmark.BookmarkFragment;
import com.wafflestudio.siksha.util.Animations;
import com.wafflestudio.siksha.util.Bookmark;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.Preference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyu Kang on 2015-10-13.
 */
public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NOT_EMPTY = 0;
    private static final int TYPE_EMPTY = 1;

    private Context context;
    private List<Restaurant> data;
    private boolean onBookmarkTab;
    private int index;

    private List<String> drawerExpandedList;

    public GroupRecyclerViewAdapter(Context context, List<Restaurant> data, boolean onBookmarkTab, int index) {
        this.context = context;
        this.data = data;
        this.onBookmarkTab = onBookmarkTab;
        this.index = index;

        drawerExpandedList = new ArrayList<String>();
    }

    public void replaceData(List<Restaurant> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (!onBookmarkTab)
            return TYPE_NOT_EMPTY;
        else {
            if (data.size() == 0)
                return TYPE_EMPTY;
            else
                return TYPE_NOT_EMPTY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NOT_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.not_empty_restaurant_layout, parent, false);
            return new NotEmptyRestaurantViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_bookmark_layout, parent, false);
            return new EmptyBookmarkViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TYPE_NOT_EMPTY:
                NotEmptyRestaurantViewHolder notEmptyRestaurantViewHolder = (NotEmptyRestaurantViewHolder) viewHolder;

                if (data.size() != 0) {
                    final String name = data.get(position).name;
                    notEmptyRestaurantViewHolder.nameView.setText(name);

                    ChildRecyclerViewAdapter adapter = new ChildRecyclerViewAdapter(context, data.get(position));
                    notEmptyRestaurantViewHolder.recyclerView.setAdapter(adapter);

                    if (Bookmark.isBookmarked(context, name))
                        notEmptyRestaurantViewHolder.bookmarkButton.setImageResource(R.drawable.ic_star_selected);
                    else
                        notEmptyRestaurantViewHolder.bookmarkButton.setImageResource(R.drawable.ic_star);

                    if (!drawerExpandedList.contains(name))
                        notEmptyRestaurantViewHolder.actionContainer.setVisibility(View.GONE);
                    else {
                        notEmptyRestaurantViewHolder.actionContainer.setVisibility(View.VISIBLE);
                        Animations.rotate(notEmptyRestaurantViewHolder.drawerButton, 0, -180f);
                    }
                }
                break;
            case TYPE_EMPTY:
                break;
        }
    }

    @Override
    public int getItemCount() {
        int size = data.size();
        return size == 0 ? 1 : size;
    }

    private class NotEmptyRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameView;
        private RecyclerView recyclerView;
        private LinearLayout actionContainer;
        private FloatingActionButton drawerButton;
        private FloatingActionButton infoPopupButton;
        private FloatingActionButton bookmarkButton;
        private FloatingActionButton kakaotalkButton;
        private TextView infoPopupTextView;
        private TextView bookmarkTextView;
        private TextView kakaotalkTextView;

        public NotEmptyRestaurantViewHolder(View itemView) {
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.group_name_view);
            actionContainer = (LinearLayout) itemView.findViewById(R.id.group_action_button_container);
            drawerButton = (FloatingActionButton) itemView.findViewById(R.id.widget_provider_refresh_button);
            infoPopupButton = (FloatingActionButton) itemView.findViewById(R.id.group_info_popup_button);
            bookmarkButton = (FloatingActionButton) itemView.findViewById(R.id.group_bookmark_button);
            kakaotalkButton = (FloatingActionButton) itemView.findViewById(R.id.group_kakaotalk_button);
            infoPopupTextView = (TextView) itemView.findViewById(R.id.group_info_popup_text_view);
            bookmarkTextView = (TextView) itemView.findViewById(R.id.group_bookmark_text_view);
            kakaotalkTextView = (TextView) itemView.findViewById(R.id.group_kakaotalk_text_view);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.card_view_holder_recycler_view);

            nameView.setTypeface(Fonts.fontBMJua);
            kakaotalkTextView.setTypeface(Fonts.fontAPAritaDotumMedium);
            infoPopupTextView.setTypeface(Fonts.fontAPAritaDotumMedium);
            bookmarkTextView.setTypeface(Fonts.fontAPAritaDotumMedium);

            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new NestedLinearLayoutManager(context));

            drawerButton.setOnClickListener(this);
            kakaotalkButton.setOnClickListener(this);
            infoPopupButton.setOnClickListener(this);
            bookmarkButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            final String name = data.get(position).name;

            switch (view.getId()) {
                case R.id.widget_provider_refresh_button:
                    if (!drawerExpandedList.contains(name)) {
                        drawerExpandedList.add(name);
                        Animations.rotate(drawerButton, 0f, -180f);
                        Animations.expand(actionContainer, true);
                    } else {
                        drawerExpandedList.remove(name);
                        Animations.rotate(drawerButton, -180f, -360f);
                        Animations.collapse(actionContainer, true);
                    }
                    break;
                case R.id.group_kakaotalk_button:
                    try {
                        final KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
                        final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(Date.getDate() + " " + Date.getTimeSlot(index) + "\n");
                        stringBuilder.append("[" + name + "]" + "\n");
                        for (int i = 0; i < data.get(position).menus.size(); i++) {
                            Menu menu = data.get(position).menus.get(i);
                            stringBuilder.append("\n" + menu.price + "원 " + menu.name);
                        }
                        /*
                         * 10월 29일 (목) 저녁
                         * [학생회관 식당]
                         *
                         * 3000원 데리치킨
                         * 1700원 무쇠고기국백반
                         * 3000원 돌솥제육콩나물볶음
                         */

                        kakaoTalkLinkMessageBuilder.addText(stringBuilder.toString());
                        kakaoTalkLinkMessageBuilder.addAppLink(context.getString(R.string.app_name),
                                new AppActionBuilder()
                                        .addActionInfo(AppActionInfoBuilder
                                                .createAndroidActionInfoBuilder()
                                                .setExecuteParam("execparamkey1=1111")
                                                .setMarketParam("referrer=kakaotalklink")
                                                .build())
                                        .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder()
                                                .setExecuteParam("execparamkey1=1111")
                                                .build())
                                        .build());
                        kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), context);
                    } catch (KakaoParameterException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.group_info_popup_button:
                    new InformationDialog(context, name).show();
                    break;
                case R.id.group_bookmark_button:
                    if (onBookmarkTab) {
                        if (Bookmark.isBookmarked(context, name)) {
                            Bookmark.unsetFromBookmark(context, name);
                            bookmarkButton.setImageResource(R.drawable.ic_star);
                            drawerExpandedList.remove(name);
                            data.remove(position);

                            ((BookmarkFragment) ((MainActivity) context).getSwipeDisabledViewPagerAdapter().getItem(0)).notifyToAdapters();
                        }
                    } else {
                        if (Bookmark.isBookmarked(context, name)) {
                            Bookmark.unsetFromBookmark(context, name);
                            bookmarkButton.setImageResource(R.drawable.ic_star);
                        } else {
                            Bookmark.setAsBookmark(context, name);
                            bookmarkButton.setImageResource(R.drawable.ic_star_selected);
                        }
                    }

                    Log.d("Bookmark", Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS));
                    break;
            }
        }
    }

    private class EmptyBookmarkViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView messageView;

        public EmptyBookmarkViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.empty_bookmark_card_view_holder_title_view);
            messageView = (TextView) itemView.findViewById(R.id.empty_bookmark_card_view_holder_message_view);

            titleView.setTypeface(Fonts.fontBMJua);
            messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        }
    }
}