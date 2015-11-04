package com.wafflestudio.siksha.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private List<String> checkList;

    public WidgetConfigureRecyclerViewAdapter(Context context) {
        this.restuarants = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_DEFAULT_SEQUENCE).split("/");
        checkList = new ArrayList<>();
    }

    public List<String> getCheckList() {
        return checkList;
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

        if (checkList.contains(restuarants[position])) {
            widgetConfigureItemViewHolder.checkBox.setChecked(true);
        } else {
            widgetConfigureItemViewHolder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return restuarants.length;
    }

    private class WidgetConfigureItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private CheckBox checkBox;
        private TextView nameView;

        public WidgetConfigureItemViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.widget_configure_item_check_box);
            nameView = (TextView) itemView.findViewById(R.id.widget_configure_item_name_view);
            nameView.setTypeface(Fonts.fontAPAritaDotumMedium);

            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            toggleCheckBox();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
            final String name = restuarants[getAdapterPosition()];

            if (checked) {
                if (!checkList.contains(name))
                    checkList.add(name);
            }
            else {
                if (checkList.contains(name))
                    checkList.remove(name);
            }

            Log.d("checkList", checkList.toString());
        }

        private void toggleCheckBox() {
            if (!checkBox.isChecked()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
    }
}
