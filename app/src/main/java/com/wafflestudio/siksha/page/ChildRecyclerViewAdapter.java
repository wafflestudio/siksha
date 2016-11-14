package com.wafflestudio.siksha.page;

import android.content.Context;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.form.Food;
import com.wafflestudio.siksha.page.menu.MenuFragment;
import com.wafflestudio.siksha.rate.RatingDialog;
import com.wafflestudio.siksha.form.Menu;
import com.wafflestudio.siksha.rate.RatingViewManager;
import com.wafflestudio.siksha.service.JSONDownloadReceiver;
import com.wafflestudio.siksha.util.Fonts;

public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Menu data;
    private Context context;


    public ChildRecyclerViewAdapter(Menu data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!data.isEmpty) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
            return new MenuViewHolder(view, context, data.restaurant);

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_menu_item, parent, false);
            return new EmptyMenuViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (!data.isEmpty) {
            MenuViewHolder menuViewHolder = (MenuViewHolder) viewHolder;
            menuViewHolder.priceView.setText(data.foods.get(position).price);
            menuViewHolder.nameView.setText(data.foods.get(position).name);
            menuViewHolder.setPosition(position);

            RatingViewManager.buildView(data.foods.get(position).rating, menuViewHolder.ratingView);
        }
    }


    @Override
    public int getItemCount() {
        if (data.isEmpty)
            return 1;
        else
            return data.foods.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceView;
        private TextView nameView;
        private TextView ratingView;
        private Context context;
        private String restaurant;
        private LinearLayout holderLayout;
        private int position;

        public MenuViewHolder(View itemView, Context context, String restaurant) {
            super(itemView);

            this.context = context;
            this.restaurant = restaurant;

            setViews();
        }

        private void setViews() {
            holderLayout = (LinearLayout) itemView.findViewById(R.id.holder_layout);
            holderLayout.setOnClickListener(this);

            priceView = (TextView) itemView.findViewById(R.id.menu_price_view);
            nameView = (TextView) itemView.findViewById(R.id.menu_name_view);
            ratingView = (TextView) itemView.findViewById(R.id.menu_rating_view);

            priceView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
            nameView.setTypeface(Fonts.fontAPAritaDotumMedium);
            ratingView.setTypeface(Fonts.fontBMJua);
        }

        @Override
        public void onClick(View v) {
            new RatingDialog(context, restaurant, nameView.getText().toString(), new RefreshListener(){
                public void refresh(float newRating) {
                    RatingViewManager.buildView(getRefreshedRating(newRating),ratingView);
                }
            }).show();
        }

        private void setPosition(int position) {
            this.position = position;
        }

        private Float getRefreshedRating(float newRating) {
            Food food = data.foods.get(position);
            Float rating;

            if(food.rating != null) {
                rating = Float.parseFloat(food.rating);
                int numberOfRatings = Integer.parseInt(food.numberOfRatings);
                rating = (rating * numberOfRatings + newRating) / (numberOfRatings+1);
                food.numberOfRatings = Integer.toString(numberOfRatings+1);
            }
            else {
                rating = newRating;
                food.numberOfRatings = "1";
            }
            food.rating = Float.toString(rating);
            return rating;
        }
    }

    private class EmptyMenuViewHolder extends RecyclerView.ViewHolder {
        private TextView messageView;

        public EmptyMenuViewHolder(View itemView) {
            super(itemView);

            messageView = (TextView) itemView.findViewById(R.id.empty_menu_message_view);
            messageView.setTypeface(Fonts.fontAPAritaDotumMedium);
        }
    }

    public interface RefreshListener {
        void refresh(float newRating);
    }
}