package com.wafflestudio.siksha.util;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.RestaurantInfoDialog;
import com.wafflestudio.siksha.page.BreakfastPage;
import com.wafflestudio.siksha.page.DinnerPage;
import com.wafflestudio.siksha.page.LunchPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterUtil {
  public static class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;

    private int pageIndex;
    public List<RestaurantClassifiedForm> forms;

    private TextView restaurantNameView;
    private ImageButton groupButton;

    public boolean isInitialization;

    public ExpandableListAdapter(Context context, List<RestaurantClassifiedForm> forms, int pageIndex) {
      this.context = context;
      this.forms = forms;
      this.pageIndex = pageIndex;

      this.isInitialization = true;
    }

    public int getGroupCount() {
      return forms.size();
    }

    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    public RestaurantClassifiedForm getGroup(int groupPosition) {
      return forms.get(groupPosition);
    }

    public int getChildrenCount(int groupPosition) {
      if (forms.get(groupPosition).isEmpty)
        return 1;
      else
        return forms.get(groupPosition).menus.size();
    }

    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    public RestaurantCrawlingForm.MenuInfo getChild(int groupPosition, int childPosition) {
      return forms.get(groupPosition).menus.get(childPosition);
    }

    public boolean hasStableIds() {
      return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
    }

    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      final String name = forms.get(groupPosition).restaurant;

      if (convertView == null)
        convertView = LayoutInflater.from(context).inflate(R.layout.group_layout, parent, false);

      restaurantNameView = (TextView) convertView.findViewById(R.id.restaurant_name);
      groupButton = (ImageButton) convertView.findViewById(R.id.group_button);

      restaurantNameView.setTypeface(FontUtil.fontAPAritaDotumSemiBold);
      restaurantNameView.setText(name);

      if (isInitialization)
        setGroupButtonAttr(name);
      else
        modifyGroupButtonAttr(name);

      groupButton.setFocusable(false);
      groupButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          RestaurantSequencer restaurantSequencer = RestaurantSequencer.getInstance();

          if (!restaurantSequencer.isBookmarkMode()) {
            RestaurantInfoDialog dialog = new RestaurantInfoDialog(context, restaurantSequencer.currentSequence.get(groupPosition), pageIndex);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
          }
        }
      });

      return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      if (convertView == null)
        convertView = LayoutInflater.from(context).inflate(R.layout.child_layout, parent, false);

      TextView noMenuLayout = (TextView) convertView.findViewById(R.id.no_menu_view);
      LinearLayout menuLayout = (LinearLayout) convertView.findViewById(R.id.menu_layout);
      TextView price = (TextView) menuLayout.findViewById(R.id.menu_price);
      TextView name = (TextView) menuLayout.findViewById(R.id.menu_name);

      if (!forms.get(groupPosition).isEmpty) {
        name.setTypeface(FontUtil.fontAPAritaDotumMedium);

        RestaurantCrawlingForm.MenuInfo menu = forms.get(groupPosition).menus.get(childPosition);
        price.setText(menu.price);
        name.setText(menu.name);

        if (pageIndex == 0)
          price.setBackgroundResource(R.drawable.breakfast_price_style);
        else if (pageIndex == 1)
          price.setBackgroundResource(R.drawable.lunch_price_style);
        else
          price.setBackgroundResource(R.drawable.dinner_price_style);

        noMenuLayout.setVisibility(View.GONE);
        menuLayout.setVisibility(View.VISIBLE);
      }
      else {
        noMenuLayout.setTypeface(FontUtil.fontAPAritaDotumMedium);

        noMenuLayout.setVisibility(View.VISIBLE);
        menuLayout.setVisibility(View.GONE);
      }

      return convertView;
    }

    private void setGroupButtonAttr(String name) {
      if (!RestaurantSequencer.getInstance().isBookmarkMode()) {
        if (pageIndex == 0)
          groupButton.setImageResource(R.drawable.ic_action_info_breakfast);
        else if (pageIndex == 1)
          groupButton.setImageResource(R.drawable.ic_action_info_lunch);
        else
          groupButton.setImageResource(R.drawable.ic_action_info_dinner);

        groupButton.setBackgroundResource(R.drawable.item_pressed);
      }
      else {
        String recordedBookmark = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, SharedPreferenceUtil.PREF_KEY_BOOKMARK);

        if (recordedBookmark.equals(""))
          groupButton.setImageResource(R.drawable.ic_action_star);
        else {
          List<String> bookmarkList = new ArrayList<String>();
          Collections.addAll(bookmarkList, recordedBookmark.split("/"));

          if (bookmarkList.contains(name))
            groupButton.setImageResource(R.drawable.ic_action_star_brighted);
          else
            modifyGroupButtonAttr(name);
        }

        groupButton.setBackgroundColor(context.getResources().getColor(R.color.transparent));
      }
    }

    private void modifyGroupButtonAttr(String name) {
      if (RestaurantSequencer.getInstance().isBookMarked(name))
        groupButton.setImageResource(R.drawable.ic_action_star_brighted);
      else
        groupButton.setImageResource(R.drawable.ic_action_star);
    }
  }

  public static class ViewPagerAdapter extends PagerAdapter {
    private Context context;

    private ExpandableListAdapter breakfastListAdapter;
    private ExpandableListAdapter lunchListAdapter;
    private ExpandableListAdapter dinnerListAdapter;

    public ViewPagerAdapter(Context context, List<ExpandableListAdapter> adapters) {
      this.context = context;
      this.breakfastListAdapter = adapters.get(0);
      this.lunchListAdapter = adapters.get(1);
      this.dinnerListAdapter = adapters.get(2);
    }

    @Override
    public int getCount() {
      return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
      View view = null;

      if (position == 0)
        view = new BreakfastPage(context, breakfastListAdapter);
      else if (position == 1)
        view = new LunchPage(context, lunchListAdapter);
      else
        view = new DinnerPage(context, dinnerListAdapter);

      view.setTag("page" + position);
      pager.addView(view, 0);

      return view;
    }

    @Override
    public void destroyItem(ViewGroup pager, int position, Object obj) {
      pager.removeView((View) obj);
    }

    @Override
    public boolean isViewFromObject(View pager, Object obj) {
      return pager == obj;
    }
  }
}
