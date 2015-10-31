package com.wafflestudio.siksha.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Fonts;
import com.wafflestudio.siksha.util.Preference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyu Kang on 2015-10-30.
 */
public class WidgetConfigureRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] restuarants;

    private List<String> checkedList;

    public WidgetConfigureRecyclerViewAdapter(Context context) {
        this.restuarants = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_DEFAULT_SEQUENCE).split("/");

        checkedList = new ArrayList<String>();
    }

    public List<String> getCheckedList() {
        return checkedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_configure_item, parent, false);
        return new WidgetConfigureItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        WidgetConfigureItemViewHolder widgetConfigureItemViewHolder = (WidgetConfigureItemViewHolder) viewHolder;
        widgetConfigureItemViewHolder.nameView.setText(restuarants[position]);
    }

    @Override
    public int getItemCount() {
        return restuarants.length;
    }

    private class WidgetConfigureItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox checkBox;
        private TextView nameView;

        public WidgetConfigureItemViewHolder(View itemView) {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.widget_configure_item_check_box);
            nameView = (TextView) itemView.findViewById(R.id.widget_configure_item_name_view);

            nameView.setTypeface(Fonts.fontAPAritaDotumMedium);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            final String name = restuarants[position];

            if (!checkedList.contains(name)) {
                checkedList.add(name);
                checkBox.setChecked(true);
            }
            else {
                checkedList.remove(name);
                checkBox.setChecked(false);
            }

            Log.d("checkedList", checkedList.toString());
        }
    }
}
