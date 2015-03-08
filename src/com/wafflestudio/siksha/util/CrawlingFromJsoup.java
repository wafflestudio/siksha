package com.wafflestudio.siksha.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.RestaurantInfoDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
public class CrawlingFromJsoup {
/*
private static HashMap<String, ArrayList<String>> map;
private Context context;
private ExpandableListView cafeList;
private ExpandableListAdapter listAdapter;

private static HashMap<String, String>signal;

public CrawlingFromJsoup(Context context,ExpandableListView cafeList){
        map=new HashMap<String, ArrayList<String>>();
        signal=new HashMap<String, String>();
        this.context=context;
        this.cafeList=cafeList;
    new Parsing().execute((Void) null);
        }

public class Parsing extends AsyncTask<Void, Void, Boolean> {
  private Document graduatePage;
  private Document jikYoungPage;
  private Document junJikYoungPage;

  private Graduate graduate;
  private JikYoung jikYoung;
  private JunJikYoung junJikYoung;

  private ProgressDialog dialog;

  private final String[] jikYoungCafes = {"학생회관식당", "3식당", "기숙사식당", "자하연식당", "302동식당", "동원관식당", "감골식당"};
  private final String[] junJikYoungCafes = {"4식당", "두레미담", "301동식당", "공대간이식당", "상아회관", "220동식당"};

  private int nowHour;
  private int nowAmPm;

  @Override
  protected void onPreExecute() {
    nowHour = Calendar.getInstance().get(Calendar.HOUR);
    nowAmPm = Calendar.getInstance().get(Calendar.AM_PM);

    dialog = new ProgressDialog(context);
    dialog.setCancelable(false);
    dialog.setTitle("오늘의 식단을 가져오고 있습니다.");

    dialog.show();
  }

  @Override
  protected Boolean doInBackground(Void... params) {
    try {
      graduatePage = Jsoup.connect("http://dorm.snu.ac.kr/dk_board/facility/food.php").timeout(10000).get();
      jikYoungPage = Jsoup.connect("http://www.snuco.com/html/restaurant/restaurant_menu1.asp").timeout(10000).get();
      junJikYoungPage = Jsoup.connect("http://www.snuco.com/html/restaurant/restaurant_menu2.asp").timeout(10000).get();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  protected void onPostExecute(Boolean result) {
    if (result == true) {
      graduate = new Graduate();
      jikYoung = new JikYoung();
      junJikYoung = new JunJikYoung();

      listAdapter = new ExpandableListAdapter(context, cafeList, RestaurantInfoUtil.restaurants, MainActivity.operatingHours, MainActivity.locations, MainActivity.matching);
      cafeList.setAdapter(listAdapter);
    }

    dialog.dismiss(); //app start
  }

  private class Graduate {
    private ArrayList<ArrayList<String>> weeklyBreakfast;
    private ArrayList<ArrayList<String>> weeklyLunch;
    private ArrayList<ArrayList<String>> weeklySupper;

    private int todayIndex;

    public Graduate() {
      todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
      weeklyBreakfast = new ArrayList<ArrayList<String>>();
      weeklyLunch = new ArrayList<ArrayList<String>>();
      weeklySupper = new ArrayList<ArrayList<String>>();

      selecting();
    }

    public void selecting() {
      Elements tableBody = graduatePage.select("tbody").get(0).children();
      // get all tr in tbody
      // 0 ~ 1 : breakfast
      // 2 ~ 4 : lunch
      // 5 ~ 6 : supper

      // breakfast
      for (int i = 0; i < 2; i++) {
        Element tr = tableBody.get(i);
        Elements td = tr.select("td:not(td[rowspan], td[class=bg])");
        // td which has menu

        ArrayList<String> oneTrLine = new ArrayList<String>();

        for (Element e : td) {
          String price = e.select("li").attr("class");

          if (e.text().trim().length() != 0) {
            if (price.equals("menu_a"))
              oneTrLine.add(e.text().trim() + "#2000");
            else if (price.equals("menu_b"))
              oneTrLine.add(e.text().trim() + "#2500");
            else if (price.equals("menu_c"))
              oneTrLine.add(e.text().trim() + "#3000");
            else if (price.equals("menu_d"))
              oneTrLine.add(e.text().trim() + "#3500");
            else if (price.equals("menu_e"))
              oneTrLine.add(e.text().trim() + "#4000");
          } else
            oneTrLine.add("");
        }

        weeklyBreakfast.add(oneTrLine);
      }

      // lunch
      for (int i = 2; i < 5; i++) {
        Element tr = tableBody.get(i);
        Elements td = tr.select("td:not(td[rowspan], td[class=bg])");

        ArrayList<String> oneTrLine = new ArrayList<String>();
        for (Element e : td) {
          String price = e.select("li").attr("class");

          if (e.text().trim().length() != 0) {
            if (price.equals("menu_a"))
              oneTrLine.add(e.text().trim() + "#2000");
            else if (price.equals("menu_b"))
              oneTrLine.add(e.text().trim() + "#2500");
            else if (price.equals("menu_c"))
              oneTrLine.add(e.text().trim() + "#3000");
            else if (price.equals("menu_d"))
              oneTrLine.add(e.text().trim() + "#3500");
            else if (price.equals("menu_e"))
              oneTrLine.add(e.text().trim() + "#4000");
          } else
            oneTrLine.add("");
        }
        weeklyLunch.add(oneTrLine);
      }

      // supper
      for (int i = 5; i < 7; i++) {
        Element tr = tableBody.get(i);
        Elements td = tr.select("td:not(td[rowspan], td[class=bg])");

        ArrayList<String> oneTrLine = new ArrayList<String>();

        for (Element e : td) {
          String price = e.select("li").attr("class");

          if (e.text().trim().length() != 0) {
            if (price.equals("menu_a"))
              oneTrLine.add(e.text().trim() + "#2000");
            else if (price.equals("menu_b"))
              oneTrLine.add(e.text().trim() + "#2500");
            else if (price.equals("menu_c"))
              oneTrLine.add(e.text().trim() + "#3000");
            else if (price.equals("menu_d"))
              oneTrLine.add(e.text().trim() + "#3500");
            else if (price.equals("menu_e"))
              oneTrLine.add(e.text().trim() + "#4000");
          } else
            oneTrLine.add("");
        }
        weeklySupper.add(oneTrLine);
      }

      ArrayList<String> temp = new ArrayList<String>();

      if (nowAmPm == 0 && nowHour < 10) {
        // 24 ~ 10
        for (int i = 0; i < weeklyBreakfast.size(); i++) {
          signal.put("아워홈", "breakfast");
          String str = weeklyBreakfast.get(i).get(todayIndex);
          if (!str.equals("") && !str.equals(" "))
            temp.add(str);
        }
      } else if ((nowAmPm == 0 && nowHour >= 10) || (nowAmPm == 1 && nowHour < 3)) {
        // 10 ~ 16
        for (int i = 0; i < weeklyLunch.size(); i++) {
          signal.put("아워홈", "lunch");
          String str = weeklyLunch.get(i).get(todayIndex);
          if (!str.equals("") && !str.equals(" "))
            temp.add(str);
        }
      } else {
        // 16 ~ 24
        for (int i = 0; i < weeklySupper.size(); i++) {
          signal.put("아워홈", "dinner");
          String str = weeklySupper.get(i).get(todayIndex);
          if (!str.equals("") && !str.equals(" "))
            temp.add(str);
        }
      }
      map.put("아워홈", temp);
    }
  }

  private class JikYoung {
    private ArrayList<String[]> breakfastAll;
    private ArrayList<String[]> lunchAll;
    private ArrayList<String[]> supperAll;

    public JikYoung() {
      breakfastAll = new ArrayList<String[]>();
      lunchAll = new ArrayList<String[]>();
      supperAll = new ArrayList<String[]>();

      selecting();
    }

    public void selecting() {
      Elements jikYoungTable = jikYoungPage.select("table:has(img[alt=상품명])");

      for (int i = 0; i < jikYoungCafes.length; i++) {
        Elements tr = jikYoungTable.select("tr:contains(" + jikYoungCafes[i] + ")");

        String preBreakfast = tr.select("td:nth-child(3)").text();
        String preLunch = tr.select("td:nth-child(5)").text();
        String preSupper = tr.select("td:nth-child(7)").text();

        preBreakfast = preBreakfast.replaceAll(" ", "/");
        preLunch = preLunch.replaceAll(" ", "/");
        preSupper = preSupper.replaceAll(" ", "/");

        String[] breakfast = preBreakfast.split("/");
        String[] lunch = preLunch.split("/");
        String[] supper = preSupper.split("/");

        setPrice(breakfast);
        setPrice(lunch);
        setPrice(supper);

        breakfastAll.add(breakfast);
        lunchAll.add(lunch);
        supperAll.add(supper);

        ArrayList<String> temp = new ArrayList<String>();

        if (i == 0 || i == 2 || i == 3) {
          // these cafeterias offer breakfast
          if (nowAmPm == 0 && nowHour < 10) {
            signal.put(jikYoungCafes[i], "breakfast");
            for (int j = 0; j < breakfast.length; j++)
              temp.add(breakfast[j].trim());
          } else if ((nowAmPm == 0 && nowHour >= 10) || (nowAmPm == 1 && nowHour < 3)) {
            signal.put(jikYoungCafes[i], "lunch");
            for (int j = 0; j < lunch.length; j++)
              temp.add(lunch[j].trim());
          } else {
            signal.put(jikYoungCafes[i], "dinner");
            for (int j = 0; j < supper.length; j++)
              temp.add(supper[j].trim());
          }
        } else {
          if ((nowAmPm == 1 && nowHour >= 3) && (nowAmPm == 1 && nowHour < 12)) {
            // 15 ~ 24
            signal.put(jikYoungCafes[i], "dinner");
            for (int j = 0; j < supper.length; j++)
              temp.add(supper[j].trim());
          } else {
            signal.put(jikYoungCafes[i], "lunch");
            for (int j = 0; j < lunch.length; j++)
              temp.add(lunch[j].trim());
          }
        }

        map.put(jikYoungCafes[i], temp);
      }
    }

    public void setPrice(String[] arr) {
      for (int j = 0; j < arr.length; j++) {
        char start = arr[j].charAt(0);

        if (start == 'ⓐ')
          arr[j] = arr[j].substring(1) + "#1700";
        else if (start == 'ⓑ')
          arr[j] = arr[j].substring(1) + "#2000";
        else if (start == 'ⓒ')
          arr[j] = arr[j].substring(1) + "#2500";
        else if (start == 'ⓓ')
          arr[j] = arr[j].substring(1) + "#3000";
        else if (start == 'ⓔ')
          arr[j] = arr[j].substring(1) + "#3500";
        else if (start == 'ⓕ')
          arr[j] = arr[j].substring(1) + "#4000";
        else if (start == 'ⓖ')
          arr[j] = arr[j].substring(1) + "#4500";
        else if (start == 'ⓗ')
          arr[j] = arr[j].substring(1) + "#Etc";
      }
    }
  }

  private class JunJikYoung {
    private ArrayList<String[]> lunchAll;
    private ArrayList<String[]> supperAll;

    public JunJikYoung() {
      lunchAll = new ArrayList<String[]>();
      supperAll = new ArrayList<String[]>();

      selecting();
    }

    public void selecting() {
      Elements junJikYoungTable = junJikYoungPage.select("table:has(img[alt=상품명])");

      for (int i = 0; i < junJikYoungCafes.length; i++) {
        Elements tr = junJikYoungTable.select("tr:contains(" + junJikYoungCafes[i] + ")");

        String preLunch = tr.select("td:nth-child(5)").text();
        String preSupper = tr.select("td:nth-child(7)").text();

        Log.d("zz", preSupper);

        String preLunch2 = preLunch.replaceAll(" ", "/");
        String preSupper2 = preSupper.replaceAll(" ", "/");

        String[] lunch = preLunch2.split("/");
        String[] supper = preSupper2.split("/");




        setPrice(lunch);
        setPrice(supper);

        lunchAll.add(lunch);
        supperAll.add(supper);

        ArrayList<String> temp = new ArrayList<String>();

        if ((nowAmPm == 1 && nowHour >= 3) && (nowAmPm == 1 && nowHour < 12)) {
          signal.put(junJikYoungCafes[i], "dinner");
          for (int j = 0; j < supper.length; j++)
            temp.add(supper[j].trim());
        } else {
          signal.put(junJikYoungCafes[i], "lunch");
          for (int j = 0; j < lunch.length; j++)
            temp.add(lunch[j].trim());
        }
        map.put(junJikYoungCafes[i], temp);
      }
    }

    public void setPrice(String[] arr) {
      for (int j = 0; j < arr.length; j++) {
        char start = arr[j].charAt(0);

        if (start == 'ⓐ')
          arr[j] = arr[j].substring(1) + "#1700";
        else if (start == 'ⓑ')
          arr[j] = arr[j].substring(1) + "#2000";
        else if (start == 'ⓒ')
          arr[j] = arr[j].substring(1) + "#2500";
        else if (start == 'ⓓ')
          arr[j] = arr[j].substring(1) + "#3000";
        else if (start == 'ⓔ')
          arr[j] = arr[j].substring(1) + "#3500";
        else if (start == 'ⓕ')
          arr[j] = arr[j].substring(1) + "#4000";
        else if (start == 'ⓖ')
          arr[j] = arr[j].substring(1) + "#4500";
        else if (start == 'ⓗ')
          arr[j] = arr[j].substring(1) + "#Etc";
      }
    }
  }
}

  private class ExpandableListAdapter extends BaseExpandableListAdapter {
    private ArrayList<ArrayList<String>> cafesIncludeMenus;
    // array for cafeterias which have array for menus

    private ExpandableListView cafeList;
    private Context context;
    private String[] cafes;
    private HashMap<String, String> operatingHours;
    private HashMap<String, String> locations;
    private HashMap<String, String> matching;

    private TextView cafeName;
    private TextView operatingHour;
    private TextView location;
    private Button cafeInfo;
    private TextView signal;

    public ExpandableListAdapter(Context context, ExpandableListView cafeList, String[] cafes, HashMap<String, String> operatingHours, HashMap<String, String> locations, HashMap<String, String> matching) {
      this.context = context;
      this.cafes = cafes;
      this.cafeList = cafeList;
      this.operatingHours = operatingHours;
      this.locations = locations;
      this.matching = matching;

      cafesIncludeMenus = new ArrayList<ArrayList<String>>();
      cafesIncludeMenus.add(map.get("학생회관식당"));
      cafesIncludeMenus.add(map.get("자하연식당"));
      cafesIncludeMenus.add(map.get("4식당"));
      cafesIncludeMenus.add(map.get("기숙사식당"));
      cafesIncludeMenus.add(map.get("아워홈"));
      cafesIncludeMenus.add(map.get("301동식당"));
      cafesIncludeMenus.add(map.get("302동식당"));
      cafesIncludeMenus.add(map.get("동원관식당"));
      cafesIncludeMenus.add(map.get("감골식당"));
      cafesIncludeMenus.add(map.get("3식당"));
      cafesIncludeMenus.add(map.get("두레미담"));
      cafesIncludeMenus.add(map.get("공대간이식당"));
      cafesIncludeMenus.add(map.get("220동식당"));
      cafesIncludeMenus.add(map.get("상아회관"));
    }

    public int getGroupCount() {
      // returns the number of cafeterias
      return cafes.length;
    }

    public long getGroupId(int groupPosition) {
      // returns index of cafeteria
      return groupPosition;
    }

    public String getGroup(int groupPosition) {
      // returns name of cafeteria
      return cafes[groupPosition];
    }

    public int getChildrenCount(int groupPosition) {
      // the number of menus
      return cafesIncludeMenus.get(groupPosition).size();
    }

    public long getChildId(int groupPosition, int childPosition) {
      // index of menu
      return childPosition;
    }

    public String getChild(int groupPosition, int childPosition) {
      // name of menu
      return cafesIncludeMenus.get(groupPosition).get(childPosition);
    }

    public boolean hasStableIds() {
      return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      // function returns View
      // Layout Inflating
      // set name of cafeteria

      final String name = cafes[groupPosition];
      final String match = matching.get(name);
      final String signalInGroup = CrawlingFromJsoup.signal.get(match);

      if (convertView == null) {
        // when view is not made yet
        convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_list, null);
      }

      cafeName = (TextView)convertView.findViewById(R.id.restaurant_name);
      cafeInfo = (Button)convertView.findViewById(R.id.show_info);
      cafeName.setText(name);

      signal = (TextView)convertView.findViewById(R.id.signal);
      signal.setText(signalInGroup);

      cafeInfo.setOnClickListener(new View.OnClickListener() {
        // when click info
        public void onClick(View v) {
          RestaurantInfoDialog dialog = new RestaurantInfoDialog(context, name, operatingHours, locations);
          dialog.setCanceledOnTouchOutside(true);
          dialog.show();
        }
      });

      return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      View view = null;
      String child = cafesIncludeMenus.get(groupPosition).get(childPosition);

      if (convertView == null)
        view = LayoutInflater.from(context).inflate(R.layout.menu_details, null);
      else
        view = convertView;

      RelativeLayout menuLayout = (RelativeLayout)view.findViewById(R.id.menu_details); // menu_details.xml
      // final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progress); // menu_details.xml

      TextView price = (TextView)view.findViewById(R.id.menu_price); // menu_details.xml
      TextView cafeMenu = (TextView)view.findViewById(R.id.menu_name); // menu_details.xml

      TextView noMenuLayout = (TextView)view.findViewById(R.id.no_menu); //menu_details.xml

      if (child.length() > 1) {
        menuLayout.setVisibility(View.VISIBLE);
        noMenuLayout.setVisibility(View.GONE);

        cafeMenu.setText(child.substring(0, child.indexOf('#')));
        // progressBar.setProgress(15);

        if (child.endsWith("1700"))
          price.setText("1.7");
        else if (child.endsWith("2000"))
          price.setText("2.0");
        else if (child.endsWith("2500"))
          price.setText("2.5");
        else if (child.endsWith("3000"))
          price.setText("3.0");
        else if (child.endsWith("3500"))
          price.setText("3.5");
        else if (child.endsWith("4000"))
          price.setText("4.0");
        else if (child.endsWith("4500"))
          price.setText("4.5");
        else if (child.endsWith("Etc"))
          price.setText("Etc");
      }
      else {
        menuLayout.setVisibility(View.GONE);
        noMenuLayout.setVisibility(View.VISIBLE);
      }

      return view;
    }
  }
*/
}