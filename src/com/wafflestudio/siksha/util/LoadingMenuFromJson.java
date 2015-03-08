package com.wafflestudio.siksha.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.RestaurantInfoDialog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LoadingMenuFromJson {
  private Context context;

  private ExpandableListView expandableListView;
  private RestaurantCrawlingForm[] forms;

  public LoadingMenuFromJson(Context context, ExpandableListView expandableListView) {
    this.context = context;
    this.expandableListView = expandableListView;

    initSetting();
  }

  public void initSetting() {
    if (isJsonUpdated()) {
      Log.d("update", "true");

      forms = new ParsingJson(context).getParsedForms();
      expandableListView.setAdapter(new ExpandableListAdapter(context, forms, RestaurantInfoUtil.operatingHours, RestaurantInfoUtil.locations));
    }
    else {
      Log.d("update", "false");

      Intent intent = new Intent(context, DownloadingJson.class);
      intent.putExtra("is_need_set_ui", true);
      context.startService(intent);
    }
  }

  private boolean isJsonUpdated() {
    String recordedDate = SharedPreferenceUtil.load(context, SharedPreferenceUtil.PREF_NAME, "json_date");
    Log.d("recordedDate", recordedDate);
    return recordedDate.equals(CalendarUtil.getCurrentDate());
  }

  public static class ParsingJson {
    private Context context;

    public ParsingJson(Context context) {
      this.context = context;
    }

    public RestaurantCrawlingForm[] getParsedForms() {
      StringBuilder stringBuilder = new StringBuilder();

      try {
        FileInputStream fis = context.openFileInput("restaurants.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "euc-kr"));

        String line;
        while((line = br.readLine()) != null) {
          stringBuilder.append(line);
        }

        return new Gson().fromJson(stringBuilder.toString(), RestaurantCrawlingForm[].class);
      }
      catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
  }

  public static class DownloadingJsonReceiver extends BroadcastReceiver {
    public static final String ACTION_DOWNLOADING_JSON = "com.wafflestudio.siksha.DownloadingJson.FINISHED";

    private ExpandableListView expandableListView;

    public DownloadingJsonReceiver(ExpandableListView expandableListView) {
      this.expandableListView = expandableListView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      boolean isNeedSetUIComponent = intent.getBooleanExtra("is_need_set_ui", false);

      if (isNeedSetUIComponent) {
        RestaurantCrawlingForm[] forms = new ParsingJson(context).getParsedForms();
        expandableListView.setAdapter(new ExpandableListAdapter(context, forms, RestaurantInfoUtil.operatingHours, RestaurantInfoUtil.locations));
      }
    }
  }

  private static class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, String> operatingHours;
    private HashMap<String, String> locations;
    private RestaurantCrawlingForm[] forms;

    private TextView restaurantName;
    private Button restaurantInfo;

    public ExpandableListAdapter(Context context, RestaurantCrawlingForm[] forms, HashMap<String, String> operatingHours, HashMap<String, String> locations) {
      this.context = context;
      this.forms = forms;
      this.operatingHours = operatingHours;
      this.locations = locations;
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
      return forms[groupPosition].menus.length;
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
      final String name = forms[groupPosition].restaurant;

      if (convertView == null) {
        // when view is not made yet
        convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_list, null);
      }

      restaurantName = (TextView) convertView.findViewById(R.id.restaurant_name);
      restaurantInfo = (Button) convertView.findViewById(R.id.show_info);
      restaurantName.setText(name);

      restaurantInfo.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
          RestaurantInfoDialog dialog = new RestaurantInfoDialog(context, name, operatingHours, locations);
          dialog.setCanceledOnTouchOutside(true);
          dialog.show();
        }
      });

      return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      final RestaurantCrawlingForm.MenuInfo info = forms[groupPosition].menus[childPosition];

      int numMenu = 0;
      for(int i = 0; i < forms[groupPosition].menus.length; i++) {
        Log.d("name", forms[groupPosition].menus[i].name + "");
        if (forms[groupPosition].menus[i].name != null)
          numMenu++;
      }

      if (convertView == null)
        convertView = LayoutInflater.from(context).inflate(R.layout.menu_details, null);

      RelativeLayout menuLayout = (RelativeLayout) convertView.findViewById(R.id.menu_details); // menu_details.xml
      TextView price = (TextView) menuLayout.findViewById(R.id.menu_price); // menu_details.xml
      TextView name = (TextView) menuLayout.findViewById(R.id.menu_name); // menu_details.xml
      TextView time = (TextView) menuLayout.findViewById(R.id.menu_time); // menu_details.xml
      TextView noMenuLayout = (TextView) convertView.findViewById(R.id.no_menu); // menu_details.xml

      if (numMenu > 0) {
        menuLayout.setVisibility(View.VISIBLE);
        noMenuLayout.setVisibility(View.GONE);

        name.setText(info.name);
        price.setText(String.valueOf(info.price));
        time.setText(info.time);
      }
      else {
        noMenuLayout.setVisibility(View.GONE);
        menuLayout.setVisibility(View.GONE);

        if (isLastChild)
          noMenuLayout.setVisibility(View.VISIBLE);
      }

      return convertView;
    }
  }
}
