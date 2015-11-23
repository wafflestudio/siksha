package com.wafflestudio.siksha.page;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.AnalyticsTrackers;
import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.InformationDialog;
import com.wafflestudio.siksha.dialog.ShareDialog;
import com.wafflestudio.siksha.form.Menu;
import com.wafflestudio.siksha.page.bookmark.BookmarkFragment;
import com.wafflestudio.siksha.util.Animations;
import com.wafflestudio.siksha.util.BookmarkManager;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.Preference;
import com.wafflestudio.siksha.util.UnitConverter;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.widget.RevealFrameLayout;

/**
 * Created by Gyu Kang on 2015-10-13.
 */
public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NOT_EMPTY = 0;
    private static final int TYPE_EMPTY = 1;

    private Context context;
    private List<Menu> data;
    private boolean isBookmarkTab;
    private int index;
    private List<String> drawerExpandedList;

    public GroupRecyclerViewAdapter(Context context, List<Menu> data, boolean isBookmarkTab, int index) {
        this.context = context;
        this.data = data;
        this.isBookmarkTab = isBookmarkTab;
        this.index = index;
        this.drawerExpandedList = new ArrayList<>();
    }

    public void replaceData(List<Menu> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isBookmarkTab)
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
                    final String restaurant = data.get(position).restaurant;
                    notEmptyRestaurantViewHolder.nameView.setText(restaurant);

                    ChildRecyclerViewAdapter adapter = new ChildRecyclerViewAdapter(data.get(position));
                    notEmptyRestaurantViewHolder.recyclerView.setAdapter(adapter);

                    if (BookmarkManager.isBookmarked(context, restaurant))
                        notEmptyRestaurantViewHolder.bookmarkButton.setImageResource(R.drawable.ic_star_selected);
                    else
                        notEmptyRestaurantViewHolder.bookmarkButton.setImageResource(R.drawable.ic_star);

                    if (!drawerExpandedList.contains(restaurant)) {
                        float radius = UnitConverter.convertDpToPx(5.0f);
                        float[] radii = {0.0f, 0.0f, 0.0f, 0.0f, radius, radius, radius, radius};
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            changeBackgroundDrawable(notEmptyRestaurantViewHolder.revealFrameLayout, R.color.white, radii);
                            changeBackgroundDrawable(notEmptyRestaurantViewHolder.recyclerView, R.color.white, radii);
                        }
                        changeBackgroundDrawable(notEmptyRestaurantViewHolder.actionContainer, R.color.color_primary, radii);

                        notEmptyRestaurantViewHolder.actionContainer.setVisibility(View.GONE);
                        ObjectAnimator animator = Animations.makeRotateAnimator(notEmptyRestaurantViewHolder.drawerButton, 0.0f, 0.0f, 200, false);
                        animator.setInterpolator(new LinearInterpolator());
                        animator.start();
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            float[] radii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
                            changeBackgroundDrawable(notEmptyRestaurantViewHolder.recyclerView, R.color.white, radii);
                        }

                        notEmptyRestaurantViewHolder.actionContainer.setVisibility(View.VISIBLE);
                        ObjectAnimator animator = Animations.makeRotateAnimator(notEmptyRestaurantViewHolder.drawerButton, -180.0f, -180.0f, 200, false);
                        animator.setInterpolator(new LinearInterpolator());
                        animator.start();
                    }
                }
                break;
            case TYPE_EMPTY:
                EmptyBookmarkViewHolder emptyBookmarkViewHolder = (EmptyBookmarkViewHolder) viewHolder;

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    float radius = UnitConverter.convertDpToPx(5.0f);
                    float[] radii = {0.0f, 0.0f, 0.0f, 0.0f, radius, radius, radius, radius};
                    changeBackgroundDrawable(emptyBookmarkViewHolder.messageViewWrapper, R.color.white, radii);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        int size = data.size();
        return size == 0 ? 1 : size;
    }

    private void changeBackgroundDrawable(View view, int colorResourceID, float[] radii) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(colorResourceID));
        gradientDrawable.setCornerRadii(radii);

        view.setBackgroundDrawable(gradientDrawable);
    }

    private class NotEmptyRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameView;
        private RecyclerView recyclerView;
        private RevealFrameLayout revealFrameLayout;
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
            revealFrameLayout = (RevealFrameLayout) itemView.findViewById(R.id.group_reveal_frame_layout);
            actionContainer = (LinearLayout) itemView.findViewById(R.id.group_action_button_container);
            drawerButton = (FloatingActionButton) itemView.findViewById(R.id.not_empty_restaurant_layout_drawer_button);
            infoPopupButton = (FloatingActionButton) itemView.findViewById(R.id.group_info_popup_button);
            bookmarkButton = (FloatingActionButton) itemView.findViewById(R.id.group_bookmark_button);
            kakaotalkButton = (FloatingActionButton) itemView.findViewById(R.id.group_share_button);
            infoPopupTextView = (TextView) itemView.findViewById(R.id.group_info_popup_text_view);
            bookmarkTextView = (TextView) itemView.findViewById(R.id.group_bookmark_text_view);
            kakaotalkTextView = (TextView) itemView.findViewById(R.id.group_share_text_view);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.not_empty_restaurant_layout_recycler_view);

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
            final String restaurant = data.get(position).restaurant;

            switch (view.getId()) {
                case R.id.not_empty_restaurant_layout_drawer_button:
                    if (!drawerExpandedList.contains(restaurant)) {
                        drawerExpandedList.add(restaurant);
                        expandDrawer();
                    } else {
                        drawerExpandedList.remove(restaurant);
                        collapseDrawer();
                    }
                    break;
                case R.id.group_share_button:
                    new ShareDialog(context, restaurant, data.get(position).foods, index).show();
                    break;
                case R.id.group_info_popup_button:
                    new InformationDialog(context, restaurant).show();
                    break;
                case R.id.group_bookmark_button:
                    if (isBookmarkTab) {
                        if (BookmarkManager.isBookmarked(context, restaurant)) {
                            BookmarkManager.unsetFromBookmark(context, restaurant);
                            bookmarkButton.setImageResource(R.drawable.ic_star);
                            drawerExpandedList.remove(restaurant);
                            data.remove(position);

                            ((BookmarkFragment) ((MainActivity) context).getSwipeDisabledViewPagerAdapter().getItem(0)).notifyToAdapters();
                        }
                    } else {
                        if (BookmarkManager.isBookmarked(context, restaurant)) {
                            BookmarkManager.unsetFromBookmark(context, restaurant);
                            bookmarkButton.setImageResource(R.drawable.ic_star);
                        } else {
                            BookmarkManager.setAsBookmark(context, restaurant);
                            bookmarkButton.setImageResource(R.drawable.ic_star_selected);
                            AnalyticsTrackers.getInstance().trackEvent("Restaurant", "Bookmark", restaurant);
                        }
                    }

                    Log.d("Bookmark", Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_BOOKMARKS));
                    break;
            }
        }

        private void expandDrawer() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                AnimatorSet animatorSet = new AnimatorSet();
                final ObjectAnimator rotateAnimator = Animations.makeRotateAnimator(drawerButton, 0.0f, -180.0f, 400, false);
                rotateAnimator.setInterpolator(new LinearInterpolator());
                Animator expandAnimator = Animations.makeExpandAnimator(actionContainer, 150);
                final com.nineoldandroids.animation.Animator revealAnimator = (com.nineoldandroids.animation.Animator) ((SupportAnimator) Animations.makeRevealAnimator(actionContainer, 250)).get();

                animatorSet.play(expandAnimator);
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        rotateAnimator.start();

                        float[] radii = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
                        changeBackgroundDrawable(recyclerView, R.color.white, radii);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        revealAnimator.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });

                animatorSet.start();
            } else {
                AnimatorSet animatorSet = new AnimatorSet();
                Animator expandAnimator = Animations.makeExpandAnimator(actionContainer, 150);
                Animator revealAnimator = (Animator) Animations.makeRevealAnimator(actionContainer, 250);
                ObjectAnimator rotateAnimator = Animations.makeRotateAnimator(drawerButton, 0.0f, -180.0f, 400, false);
                rotateAnimator.setInterpolator(new LinearInterpolator());

                animatorSet.play(rotateAnimator).with(expandAnimator);
                animatorSet.play(expandAnimator).before(revealAnimator);

                animatorSet.start();
            }
        }

        private void collapseDrawer() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                final AnimatorSet animatorSet = new AnimatorSet();
                com.nineoldandroids.animation.AnimatorSet supportAnimatorSet = new com.nineoldandroids.animation.AnimatorSet();
                final ObjectAnimator rotateAnimator = Animations.makeRotateAnimator(drawerButton, -180.0f, -360.0f, 400, false);
                rotateAnimator.setInterpolator(new LinearInterpolator());
                com.nineoldandroids.animation.Animator concealAnimator = (com.nineoldandroids.animation.Animator) ((SupportAnimator) Animations.makeConcealAnimator(actionContainer, 250)).get();
                final Animator collapseAnimator = Animations.makeCollapseAnimator(actionContainer, 150);

                animatorSet.play(collapseAnimator);
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        float radius = UnitConverter.convertDpToPx(5.0f);
                        float[] radii = {0.0f, 0.0f, 0.0f, 0.0f, radius, radius, radius, radius};
                        changeBackgroundDrawable(recyclerView, R.color.white, radii);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });

                supportAnimatorSet.play(concealAnimator);
                supportAnimatorSet.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                        rotateAnimator.start();
                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        animatorSet.start();
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {
                    }
                });

                supportAnimatorSet.start();
            } else {
                AnimatorSet animatorSet = new AnimatorSet();
                Animator collapseAnimator = Animations.makeCollapseAnimator(actionContainer, 150);
                Animator concealAnimator = (Animator) Animations.makeConcealAnimator(actionContainer, 250);
                ObjectAnimator rotateAnimator = Animations.makeRotateAnimator(drawerButton, -180.0f, -360.0f, 400, false);
                rotateAnimator.setInterpolator(new LinearInterpolator());

                animatorSet.play(rotateAnimator).with(concealAnimator);
                animatorSet.play(concealAnimator).before(collapseAnimator);

                animatorSet.start();
            }
        }
    }

    private class EmptyBookmarkViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private LinearLayout messageViewWrapper;
        private TextView messageView;

        public EmptyBookmarkViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.empty_bookmark_layout_title_view);
            messageViewWrapper = (LinearLayout) itemView.findViewById(R.id.empty_bookmark_layout_message_view_wrapper);
            messageView = (TextView) itemView.findViewById(R.id.empty_bookmark_layout_message_view);
            titleView.setTypeface(Fonts.fontBMJua);
            messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        }
    }
}