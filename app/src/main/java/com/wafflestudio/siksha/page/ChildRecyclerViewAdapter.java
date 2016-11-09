package com.wafflestudio.siksha.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.RatingDialog;
import com.wafflestudio.siksha.form.Menu;
import com.wafflestudio.siksha.util.Fonts;

public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Menu data;
    private Context context;

    public ChildRecyclerViewAdapter(Menu data,Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!data.isEmpty) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
            return new MenuViewHolder(view,context,data.restaurant);
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


            if(data.foods.get(position).rating != null) {
                float rate = Float.parseFloat(data.foods.get(position).rating);
                menuViewHolder.ratingView.setText(String.format("%.1f", rate));
            }

            else
                menuViewHolder.ratingView.setText(""); // this food isn't rated yet. Show nothing in star.

        }
    }


    @Override
    public int getItemCount() {
        if (data.isEmpty)
            return 1;
        else
            return data.foods.size();
    }

    private class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView priceView;
        private TextView nameView;
        private Button ratingView;
        private Context context;
        private String restaurant;

        public MenuViewHolder(View itemView,Context context,String restaurant) {
            super(itemView);

            this.context = context;
            this.restaurant = restaurant;

            priceView = (TextView) itemView.findViewById(R.id.menu_price_view);
            nameView = (TextView) itemView.findViewById(R.id.menu_name_view);
            ratingView = (Button) itemView.findViewById(R.id.menu_rating_view);

            priceView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
            nameView.setTypeface(Fonts.fontAPAritaDotumMedium);
            ratingView.setTypeface(Fonts.fontAPAritaDotumSemiBold);

            ratingView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            new RatingDialog(context, restaurant, nameView.getText().toString()).show();
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
}