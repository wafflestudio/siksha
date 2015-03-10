package com.wafflestudio.siksha.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.DownloadingRetryDialog;
import com.wafflestudio.siksha.dialog.ProgressDialog;
import com.wafflestudio.siksha.service.DownloadingJson;

import java.util.HashMap;

public class LoadingMenuFromJson {
  private Context context;

  private ExpandableListView expandableListView;
  private static ProgressDialog progressDialog;

  private RestaurantCrawlingForm[] forms;

  public LoadingMenuFromJson(Context context, ExpandableListView expandableListView) {
    this.context = context;
    this.expandableListView = expandableListView;
  }

  public void initSetting() {
    if (isJsonUpdated()) {
      Log.d("is_json_updated", "true");

      forms = new ParsingJson(context).getParsedForms();
      expandableListView.setAdapter(new ExpandableListAdapter(context, forms));
    }
    else {
      Log.d("is_json_updated", "false");

      progressDialog = new ProgressDialog(context, context.getString(R.string.downloading_message));
      progressDialog.setCancelable(false);

      if (!NetworkUtil.getInstance().isOnline())
        new DownloadingRetryDialog(context).show();
      else
        startDownloadingService(context);
    }
  }

  public static void startDownloadingService(Context context) {
    progressDialog.startShowing();

    Intent intent = new Intent(context, DownloadingJson.class);
    intent.putExtra("is_need_set_expandable_list_view", true);
    context.startService(intent);
  }

  private boolean isJsonUpdated() {
    String recordedDate = SharedPreferenceUtil.loadValueOfString(context, SharedPreferenceUtil.PREF_APP_NAME, "json_date");
    Log.d("recorded_date", recordedDate);
    return recordedDate.equals(CalendarUtil.getCurrentDate());
  }

  public static class DownloadingJsonReceiver extends BroadcastReceiver {
    public static final String ACTION_DOWNLOADING_JSON = "com.wafflestudio.siksha.DownloadingJson.FINISHED";

    private ExpandableListView expandableListView;

    public DownloadingJsonReceiver(ExpandableListView expandableListView) {
      this.expandableListView = expandableListView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      boolean isNeedSetUIComponent = intent.getBooleanExtra("is_need_set_expandable_list_view", true);

      if (progressDialog.isShowing())
        progressDialog.quitShowing();

      if (isNeedSetUIComponent) {
        RestaurantCrawlingForm[] forms = new ParsingJson(context).getParsedForms();
        expandableListView.setAdapter(new ExpandableListAdapter(context, forms));
      }
    }
  }

  private static class ExpandableListAdapter extends BaseExpandableListAdapter {
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

      if (isExpanded) {
        convertView.setBackgroundColor(Color.parseColor("#99ff9426"));
        restaurantNameView.setTextColor(Color.parseColor("#ffffff"));
      }
      else {
        convertView.setBackgroundColor(Color.parseColor("#00ffffff"));
        restaurantNameView.setTextColor(Color.parseColor("#000000"));
      }

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
}
