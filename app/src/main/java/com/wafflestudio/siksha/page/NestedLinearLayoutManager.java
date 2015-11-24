package com.wafflestudio.siksha.page;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class NestedLinearLayoutManager extends LinearLayoutManager {
    private int[] measuredDimension = new int[2];

    public NestedLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

        int width = 0;
        int height = 0;
        int itemCount = getItemCount();

        for (int i = 0; i < itemCount; i++) {
            measureScrapChild(recycler, i, widthSpec, View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), measuredDimension);
            height = height + measuredDimension[1];

            if (i == 0) {
                width = measuredDimension[0];
            }
        }

        if (widthMode == View.MeasureSpec.EXACTLY)
            width = widthSize;

        if (heightMode == View.MeasureSpec.EXACTLY)
            height = heightSize;

        setMeasuredDimension(width, height);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        View view = recycler.getViewForPosition(position);
        recycler.bindViewToPosition(view, position);

        if (view != null) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), params.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), params.height);

            view.measure(childWidthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + params.bottomMargin + params.topMargin;

            recycler.recycleView(view);
        }
    }
}
