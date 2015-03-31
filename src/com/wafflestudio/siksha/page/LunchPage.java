package com.wafflestudio.siksha.page;

import android.content.Context;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.AdapterUtil;
import com.wafflestudio.siksha.util.CalendarUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.RestaurantInfoUtil;
import com.wafflestudio.siksha.util.RestaurantSequencer;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class LunchPage extends LinearLayout {
  private Context context;

  public ExpandableListView expandableListView;
  public AdapterUtil.ExpandableListAdapter expandableListAdapter;

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
    indicator.setText(SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON).substring(5) + " " + context.getString(R.string.lunch));

    expandableListView = (ExpandableListView) findViewById(R.id.lunch_expandable_list_view);
    expandableListView.setAdapter(expandableListAdapter);
    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
      @Override
      public void onGroupExpand(int groupPosition) {
        collapseItems(groupPosition);
      }
    });

    expandBookmarks();
  }

  public void expandBookmarks() {
    String recordedBookmark = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK);

    if (recordedBookmark.equals(""))
      return;

    for (String name : recordedBookmark.split("/"))
      expandableListView.expandGroup(RestaurantSequencer.getInstance().currentSequence.indexOf(name));
  }

  public void collapseAllGroup() {
    for(int i = 0; i < RestaurantSequencer.getInstance().currentSequence.size(); i++)
      expandableListView.collapseGroup(i);
  }

  public void collapseItems(int groupPosition) {
    RestaurantSequencer restaurantSequencer = RestaurantSequencer.getInstance();
    String recordedBookmark = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK);

    if (recordedBookmark.equals("")) {
      for(int i = 0; i < restaurantSequencer.currentSequence.size(); i++) {
        if (i != groupPosition)
          expandableListView.collapseGroup(i);
      }
    }
    else {
      List<String> bookmarkList = new ArrayList<String>();
      for(String bookmark : recordedBookmark.split("/"))
        bookmarkList.add(bookmark);

      for (int i = 0; i < restaurantSequencer.currentSequence.size(); i++) {
        if (!bookmarkList.contains(restaurantSequencer.currentSequence.get(i)) && i != groupPosition)
          expandableListView.collapseGroup(i);
      }
    }
  }
}
