package com.wafflestudio.siksha.page;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.RestaurantInfoDialog;
import com.wafflestudio.siksha.util.AdapterUtil;
import com.wafflestudio.siksha.util.RestaurantInfo;

public class DinnerPage extends LinearLayout {
  private Context context;
  private ExpandableListView expandableListView;
  private AdapterUtil.ExpandableListAdapter expandableListAdapter;

  public DinnerPage(Context context, AdapterUtil.ExpandableListAdapter expandableListAdapter) {
    super(context);

    this.context = context;
    this.expandableListAdapter = expandableListAdapter;

    initSetting();
  }

  private void initSetting() {
    inflate(context, R.layout.dinner_page, this);

    expandableListView = (ExpandableListView) findViewById(R.id.dinner_expandable_list_view);
    setExpandableListView();
  }

  public void setExpandableListView() {
    setGroupIndicatorPosition();

    expandableListView.setAdapter(expandableListAdapter);
    expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
          RestaurantInfoDialog dialog = new RestaurantInfoDialog(context, RestaurantInfo.restaurants[position]);
          dialog.setCanceledOnTouchOutside(true);
          dialog.show();

          return true;
        }

        return false;
      }
    });
    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
      @Override
      public void onGroupExpand(int groupPosition) {
        int groupSize = RestaurantInfo.restaurants.length;

        for(int i = 0; i < groupSize; i++) {
          if (i != groupPosition)
            expandableListView.collapseGroup(i);
        }
      }
    });
  }

  private void setGroupIndicatorPosition() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);

    int width = displayMetrics.widthPixels;
    expandableListView.setIndicatorBounds(width - getDpFromPixel(25), width - getDpFromPixel(10));
  }

  public int getDpFromPixel(float pixels) {
    final float scale = getResources().getDisplayMetrics().density;

    return (int) (pixels * scale + 0.5f);
  }
}
