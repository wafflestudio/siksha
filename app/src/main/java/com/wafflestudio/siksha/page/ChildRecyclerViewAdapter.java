package com.wafflestudio.siksha.page;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.form.Menu;
import com.wafflestudio.siksha.util.Fonts;

/**
 * Created by Gyu Kang on 2015-10-13.
 */
public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Menu data;

    public ChildRecyclerViewAdapter(Menu data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!data.isEmpty) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
            return new MenuViewHolder(view);
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
        }
    }

    @Override
    public int getItemCount() {
        if (data.isEmpty)
            return 1;
        else
            return data.foods.size();
    }

    private class MenuViewHolder extends RecyclerView.ViewHolder {
        private TextView priceView;
        private TextView nameView;

        public MenuViewHolder(View itemView) {
            super(itemView);

            priceView = (TextView) itemView.findViewById(R.id.menu_price_view);
            nameView = (TextView) itemView.findViewById(R.id.menu_name_view);
            priceView.setTypeface(Fonts.fontAPAritaDotumSemiBold);
            nameView.setTypeface(Fonts.fontAPAritaDotumMedium);
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