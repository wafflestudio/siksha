package com.wafflestudio.siksha;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wafflestudio.siksha.dialog.RestaurantInfoDialog;
import com.wafflestudio.siksha.util.LoadingMenuFromJson;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;

/**
 * Created by hwangseongman on 2015. 3. 16..
 */
public class LunchPage extends LinearLayout {
  private Context context;
  private ExpandableListView expandableListView;

  public LunchPage(Context context) {
    super(context);
    this.context = context;

    initSetting();
  }

  private void initSetting() {
    inflate(context, R.layout.inflate_lunch, this);

    expandableListView = (ExpandableListView) findViewById(R.id.lunch_expandable_list_view);
    setExpandableListView();
  }

  public void setExpandableListView() {
    expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
          RestaurantInfoDialog dialog = new RestaurantInfoDialog(context, RestaurantInfoUtil.restaurants[position]);
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
        int groupSize = RestaurantInfoUtil.restaurants.length;

        for(int i = 0; i < groupSize; i++) {
          if (i != groupPosition)
            expandableListView.collapseGroup(i);
        }
      }
    });
    setGroupIndicatorPosition();

    new LoadingMenuFromJson(context, expandableListView).initSetting();
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
