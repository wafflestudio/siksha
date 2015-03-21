package com.wafflestudio.siksha.page;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.RestaurantInfoDialog;
import com.wafflestudio.siksha.util.AdapterUtil;
import com.wafflestudio.siksha.util.BookmarkUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.RestaurantInfo;

public class LunchPage extends LinearLayout {
  private Context context;

  private ExpandableListView expandableListView;
  private AdapterUtil.ExpandableListAdapter expandableListAdapter;

  private TextView indicator;

  public LunchPage(Context context, AdapterUtil.ExpandableListAdapter expandableListAdapter) {
    super(context);

    this.context = context;
    this.expandableListAdapter = expandableListAdapter;

    initSetting();
  }

  private void initSetting() {
    inflate(context, R.layout.lunch_page, this);

    indicator = (TextView) findViewById(R.id.lunch_indicator);
    indicator.setTypeface(FontUtil.fontAPAritaDotumMedium);

    expandableListView = (ExpandableListView) findViewById(R.id.lunch_expandable_list_view);
    setExpandableListView();
  }

  public void setExpandableListView() {
    expandableListView.setAdapter(expandableListAdapter);
    expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        long pos = ((ExpandableListView) parent).getExpandableListPosition(position);

        int itemType = ExpandableListView.getPackedPositionType(pos);
        int groupPosition = ExpandableListView.getPackedPositionGroup(pos);

        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
          RestaurantInfoDialog dialog = new RestaurantInfoDialog(context, RestaurantInfo.restaurants[groupPosition]);
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
}
