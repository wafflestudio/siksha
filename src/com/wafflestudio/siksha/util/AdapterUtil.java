package com.wafflestudio.siksha.util;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.page.BreakfastPage;
import com.wafflestudio.siksha.page.DinnerPage;
import com.wafflestudio.siksha.page.LunchPage;

import java.util.HashMap;
import java.util.List;

public class AdapterUtil {
  public static class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;

    private List<RestaurantClassifiedForm> forms;
    private int pageIndex;

    private TextView restaurantNameView;

    public ExpandableListAdapter(Context context, List<RestaurantClassifiedForm> forms, int pageIndex) {
      this.context = context;
      this.forms = forms;
      this.pageIndex = pageIndex;
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
      int size = forms.get(groupPosition).menus.size();

      if (size == 0)
        Toast.makeText(context, R.string.restaurant_no_menu, Toast.LENGTH_SHORT).show();

      return size;
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

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      final String restaurantName = forms.get(groupPosition).restaurant;

      if (convertView == null)
        convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_layout, null);

      restaurantNameView = (TextView) convertView.findViewById(R.id.restaurant_name);
      restaurantNameView.setTypeface(FontUtil.fontAPAritaDotumMedium);
      restaurantNameView.setText(restaurantName);

      return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      final RestaurantCrawlingForm.MenuInfo menu = forms.get(groupPosition).menus.get(childPosition);

      if (convertView == null)
        convertView = LayoutInflater.from(context).inflate(R.layout.menu_layout, null);

      LinearLayout menuLayout = (LinearLayout) convertView.findViewById(R.id.menu_layout);
      TextView price = (TextView) menuLayout.findViewById(R.id.menu_price);
      TextView name = (TextView) menuLayout.findViewById(R.id.menu_name);

      name.setTypeface(FontUtil.fontAPAritaDotumMedium);

      switch (pageIndex) {
        case 0:
          price.setBackgroundResource(R.drawable.breakfast_price_style);
          break;
        case 1:
          price.setBackgroundResource(R.drawable.lunch_price_style);
          break;
        case 2:
          price.setBackgroundResource(R.drawable.dinner_price_style);
          break;
      }

      price.setText(menu.price);
      name.setText(menu.name);

      return convertView;
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
