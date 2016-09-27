package com.wafflestudio.siksha.page.settings;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.SequenceActivity;
import com.wafflestudio.siksha.util.Fonts;

import java.util.Collections;
import java.util.List;

public class SequenceRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> data;
    private OnStartDragListener mDragStartListener;
    public SequenceRecyclerViewAdapter(List<String> data, OnStartDragListener mDragStartListener) {
        this.data = data;
        this.mDragStartListener = mDragStartListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sequence_item, parent, false);
        return new SequenceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        SequenceItemViewHolder sequenceItemViewHolder = (SequenceItemViewHolder) viewHolder;
        sequenceItemViewHolder.nameView.setText(data.get(position));

        ((SequenceItemViewHolder) viewHolder).handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

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
        private ImageView handleView;

        public SequenceItemViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.sequence_item_name_view);
            nameView.setTypeface(Fonts.fontAPAritaDotumMedium);
            handleView = (ImageView) itemView.findViewById(R.id.handle);

        }
    }

    public interface OnStartDragListener {

        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }
}
