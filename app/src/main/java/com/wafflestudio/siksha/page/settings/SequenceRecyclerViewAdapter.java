package com.wafflestudio.siksha.page.settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.Fonts;

import java.util.Collections;
import java.util.List;

/**
 * Created by Gyu Kang on 2015-10-14.
 */
public class SequenceRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SequenceItemTouchHelperCallback.ItemTouchHelperAdapter {
    private List<String> data;

    public SequenceRecyclerViewAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sequence_item, parent, false);
        return new SequenceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SequenceItemViewHolder sequenceItemViewHolder = (SequenceItemViewHolder) viewHolder;
        sequenceItemViewHolder.nameView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    public List<String> getData() {
        return data;
    }

    private class SequenceItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView;

        public SequenceItemViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.sequence_item_name_view);
            nameView.setTypeface(Fonts.fontAPAritaDotumMedium);
        }
    }
}
