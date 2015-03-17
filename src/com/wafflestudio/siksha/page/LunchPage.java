package com.wafflestudio.siksha.page;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.RestaurantInfoDialog;
import com.wafflestudio.siksha.util.AdapterUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.RestaurantInfo;

public class LunchPage extends LinearLayout {
  private Context context;
  private LayoutInflater inflater;

  private ExpandableListView expandableListView;
  private AdapterUtil.ExpandableListAdapter expandableListAdapter;
  private TextView indicator;

  public LunchPage(Context context, AdapterUtil.ExpandableListAdapter expandableListAdapter) {
    super(context);

    this.context = context;
    this.expandableListAdapter = expandableListAdapter;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    initSetting();
  }

  private void initSetting() {
    View view = inflater.inflate(R.layout.lunch_page, this);

    indicator = (TextView) view.findViewById(R.id.lunch_indicator);
    indicator.setTypeface(FontUtil.fontAPAritaDotumMedium);

    expandableListView = (ExpandableListView) view.findViewById(R.id.lunch_expandable_list_view);
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
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
      expandableListView.setIndicatorBounds(width - getDpFromPixel(25), width - getDpFromPixel(10));
    else
      expandableListView.setIndicatorBoundsRelative(width - getDpFromPixel(25), width - getDpFromPixel(10));
  }

  public int getDpFromPixel(float pixels) {
    final float scale = getResources().getDisplayMetrics().density;

    return (int) (pixels * scale + 0.5f);
  }
}
