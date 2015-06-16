package com.wafflestudio.siksha.page;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.util.AdapterUtil;
import com.wafflestudio.siksha.util.FontUtil;
import com.wafflestudio.siksha.util.Sequencer;
import com.wafflestudio.siksha.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DinnerPage extends LinearLayout {
  public ExpandableListView expandableListView;
  public AdapterUtil.ExpandableListAdapter expandableListAdapter;
  private Context context;

  public DinnerPage(Context context, AdapterUtil.ExpandableListAdapter expandableListAdapter) {
    super(context);

    this.context = context;
    this.expandableListAdapter = expandableListAdapter;

    initialize();
  }

  private void initialize() {
    inflate(context, R.layout.dinner_page, this);

    TextView indicator = (TextView) findViewById(R.id.dinner_indicator);
    indicator.setTypeface(FontUtil.fontAPAritaDotumMedium);
    indicator.setText(SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_JSON).substring(5) + " " + context.getString(R.string.dinner));

    expandableListView = (ExpandableListView) findViewById(R.id.dinner_expandable_list_view);
    expandableListView.setAdapter(expandableListAdapter);
    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
      @Override
      public void onGroupExpand(int groupPosition) {
        collapseItems(groupPosition);
      }
    });
    expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
      @Override
      public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
        Sequencer sequencer = Sequencer.getInstance();

        if (!sequencer.isBookmarkMode())
          return false;
        else {
          String name = sequencer.sequence.get(groupPosition);

          if (!sequencer.isBookMarked(name))
            sequencer.setBookmark(name, true);
          else
            sequencer.setBookmark(name, false);

          sequencer.modifySequence(name);
          sequencer.setMenuListOnSequence();
          sequencer.notifyChangeToAdapters(false);

          return true;
        }
      }
    });

    expandBookmarks();
  }

  public void expandBookmarks() {
    String recordedBookmark = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK);

    if (recordedBookmark.equals(""))
      return;

    for (String name : recordedBookmark.split("/"))
      expandableListView.expandGroup(Sequencer.getInstance().sequence.indexOf(name));
  }

  public void collapseAllGroup() {
    for (int i = 0; i < Sequencer.getInstance().sequence.size(); i++)
      expandableListView.collapseGroup(i);
  }

  public void collapseItems(int groupPosition) {
    Sequencer sequencer = Sequencer.getInstance();
    String recordedBookmark = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK);

    if (recordedBookmark.equals("")) {
      for (int i = 0; i < sequencer.sequence.size(); i++) {
        if (i != groupPosition)
          expandableListView.collapseGroup(i);
      }
    } else {
      List<String> bookmarkList = new ArrayList<String>();
      Collections.addAll(bookmarkList, recordedBookmark.split("/"));

      for (int i = 0; i < sequencer.sequence.size(); i++) {
        if (!bookmarkList.contains(sequencer.sequence.get(i)) && i != groupPosition)
          expandableListView.collapseGroup(i);
      }
    }
  }
}
