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

public class AdapterUtil {
  public static class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;

    private HashMap<String, String> matchings;
    private RestaurantCrawlingForm[] forms;

    private TextView restaurantNameView;

    public ExpandableListAdapter(Context context, RestaurantCrawlingForm[] forms) {
      this.context = context;
      this.forms = forms;
      this.matchings = RestaurantInfoUtil.matchings;
    }

    public int getGroupCount() {
      // returns the number of restaurants
      return forms.length;
    }

    public long getGroupId(int groupPosition) {
      // returns index of restaurants
      return groupPosition;
    }

    public RestaurantCrawlingForm getGroup(int groupPosition) {
      // returns name of restaurants
      return forms[groupPosition];
    }

    public int getChildrenCount(int groupPosition) {
      // the number of menus
      int size = forms[groupPosition].menus.length;

      if (size == 0)
        Toast.makeText(context, R.string.restaurant_no_menu, Toast.LENGTH_SHORT).show();

      return size;
    }

    public long getChildId(int groupPosition, int childPosition) {
      // index of menu
      return childPosition;
    }

    public RestaurantCrawlingForm.MenuInfo getChild(int groupPosition, int childPosition) {
      // name of menu
      return forms[groupPosition].menus[childPosition];
    }

    public boolean hasStableIds() {
      return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      final String restaurantName = matchings.get(forms[groupPosition].restaurant);

      if (convertView == null) {
        // when view is not made yet
        convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_layout, null);
      }

      restaurantNameView = (TextView) convertView.findViewById(R.id.restaurant_name);
      restaurantNameView.setTypeface(FontUtil.fontAPAritaDotumMedium);
      restaurantNameView.setText(restaurantName);

      return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      final RestaurantCrawlingForm.MenuInfo menu = forms[groupPosition].menus[childPosition];

      if (convertView == null)
        convertView = LayoutInflater.from(context).inflate(R.layout.menu_layout, null);

      LinearLayout menuLayout = (LinearLayout) convertView.findViewById(R.id.menu_layout);
      TextView price = (TextView) menuLayout.findViewById(R.id.menu_price);
      TextView name = (TextView) menuLayout.findViewById(R.id.menu_name);

      price.setText(String.valueOf(menu.price));
      name.setText(menu.name);
      name.setTypeface(FontUtil.fontAPAritaDotumMedium);

      return convertView;
    }
  }

  public static class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ExpandableListAdapter expandableListAdapter;

    public ViewPagerAdapter(Context context, ExpandableListAdapter expandableListAdapter) {
      this.context = context;
      this.expandableListAdapter = expandableListAdapter;
    }

    @Override
    public int getCount() {
      return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
      View view = null;

      if (position == 0)
        view = new BreakfastPage(context, expandableListAdapter);
      else if (position == 1)
        view = new LunchPage(context, expandableListAdapter);
      else
        view = new DinnerPage(context, expandableListAdapter);

      pager.addView(view, position);

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
