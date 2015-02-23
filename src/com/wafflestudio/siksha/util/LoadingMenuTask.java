package com.wafflestudio.siksha.util;

import android.content.Context;
import android.os.Message;
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
import com.wafflestudio.siksha.MainActivity;
import com.wafflestudio.siksha.R;
import com.wafflestudio.siksha.dialog.CafeInfoDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoadingMenuTask {

  public static final String BASE_URL = "http://dev.wafflestudio.net:4210";
  private static final int NUM_RESTAURANTS = RestaurantInfoUtil.restaurants.length;

  private Context context;
  private ExpandableListView restaurantList;

  public RestaurantCrawlingForm[] forms = new RestaurantCrawlingForm[NUM_RESTAURANTS];

  private android.os.Handler handler = new android.os.Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 200 :
          Log.d("zz", msg.what + "");
          new Crawling(context, restaurantList);
          break;
        default :
          Log.d("dd", msg.what + "");
          new CrawlingFromJsoup(context, restaurantList);
      }
    }
  };

  public LoadingMenuTask(final Context context, final ExpandableListView restaurantList) {
    this.context = context;
    this.restaurantList = restaurantList;

    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          URL url = new URL(BASE_URL + "/graduate");
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setConnectTimeout(2 * 1000);
          connection.setReadTimeout(3 * 1000);

          Log.d("Here?", connection + "");
          handler.sendEmptyMessage(connection.getResponseCode());
        }
        catch (Exception e) {
          e.printStackTrace();
          handler.sendEmptyMessage(0);
        }
      }
    }).start();
  }

  // 안드로이드 정책상 백그라운드 쓰레드에서 백그라운드 쓰레드를 호출 ㄴㄴ
  // getResponseCode()를 백그라운드 쓰레드에서 돌리고 있잖아.
  // code == 200 -> 서버와 성공적인 통신 -> new Crawling() 호출 -> But, Crawling 클래스은 백그라운드 쓰레드에서 작업하는게 있어.
  // getResponseCode()를 일단 백그라운드에서 받았잖아. -> 이거를 handler를 통해서 ui thread로 넘겨 -> 핸들러를 통해서 넘어온 ui thread에서
  // new Crawling()을 호출. -> ui thread에서 background 호출출

 public static class Crawling {

    private HashMap<String, RestaurantCrawlingForm> map;
    private ExpandableListView restaurantList;
    private Context context;

    public Crawling(Context context, ExpandableListView restaurantList) {
      map = new HashMap<String, RestaurantCrawlingForm>();
      this.restaurantList = restaurantList;
      this.context = context;

      ExecutorService executorService = Executors.newFixedThreadPool(10);
      final CompletionService<RestaurantCrawlingForm> completionService =
              new ExecutorCompletionService<RestaurantCrawlingForm>(executorService);

      for (int i = 0; i < NUM_RESTAURANTS; i++) {
        completionService.submit(new CrawlingCallable(RestaurantInfoUtil.routes[i]));
      }

      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            for (int i = 0; i < NUM_RESTAURANTS; i++) {
              Future<RestaurantCrawlingForm> future = completionService.take();
              RestaurantCrawlingForm form = future.get();

              if (form == null) {

              }
              map.put(form.restaurant, form);
            }
          }
          catch (Exception e) {
            e.printStackTrace();
          }
          finally {
            handler.sendEmptyMessage(0);
          }
        }
      }).start();
    }

    private android.os.Handler handler = new android.os.Handler() {
      @Override
      public void handleMessage(Message msg) {
        List<RestaurantCrawlingForm> forms = new ArrayList<RestaurantCrawlingForm>();
        for(int i = 0; i < map.size(); i++) {
          forms.add(map.get(RestaurantInfoUtil.restaurants[i]));
        }
        ExpandableListAdapter adapter = new ExpandableListAdapter(context, restaurantList, forms, MainActivity.operatingHours, MainActivity.locations);
        restaurantList.setAdapter(adapter);
      }
    };

    public static String getJsonFromUrl(String route) {
      StringBuilder data = new StringBuilder();

      try {
        URL url = new URL(BASE_URL + route);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(3 * 1000);

        if (connection.getResponseCode() == 200) {
          InputStream is = connection.getInputStream();
          BufferedReader br = new BufferedReader(new InputStreamReader(is));

          String line;
          while ((line = br.readLine()) != null) {
            data.append(line);
          }
          is.close();
          br.close();
        } else {
          connection.disconnect();
          data = null;
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
        data = null;
      } catch (IOException e) {
        e.printStackTrace();
        data = null;
      }

      return data == null ? null : data.toString();
    }

    private class CrawlingCallable implements Callable<RestaurantCrawlingForm> {
      private String route;
      private RestaurantCrawlingForm form;

      public CrawlingCallable(String route) {
        this.route = route;
      }

      @Override
      public RestaurantCrawlingForm call() {
        try {
          Gson gson = new Gson();
          form = gson.fromJson(Crawling.getJsonFromUrl(route), RestaurantCrawlingForm.class);
        }
        catch (Exception e) {
          e.printStackTrace();
          return null;
        }

        return form;
      }
    }
  }





  private static class ExpandableListAdapter extends BaseExpandableListAdapter {

    private ExpandableListView cafeList;
    private Context context;

    private TextView restaurantName;
    private Button restaurantInfo;
    private TextView time;

    private HashMap<String, String> operatingHours;
    private HashMap<String, String> locations;

    private List<RestaurantCrawlingForm> forms;

    public ExpandableListAdapter(Context context, ExpandableListView cafeList, List<RestaurantCrawlingForm> forms, HashMap<String, String> operatingHours, HashMap<String, String> locations) {
      this.context = context;
      this.cafeList = cafeList;
      this.forms = forms;
      this.operatingHours = operatingHours;
      this.locations = locations;
    }

    public int getGroupCount() {
      // returns the number of restaurants
      return forms.size();
    }

    public long getGroupId(int groupPosition) {
      // returns index of restaurants
      return groupPosition;
    }

    public RestaurantCrawlingForm getGroup(int groupPosition) {
      // returns name of restaurants
      return forms.get(groupPosition);
    }

    public int getChildrenCount(int groupPosition) {
      // the number of menus
      return forms.get(groupPosition).menus.length;
    }

    public long getChildId(int groupPosition, int childPosition) {
      // index of menu
      return childPosition;
    }

    public RestaurantCrawlingForm.RestaurantInfo getChild(int groupPosition, int childPosition) {
      // name of menu
      return forms.get(groupPosition).menus[childPosition];
    }

    public boolean hasStableIds() {
      return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      // function returns view
      // Layout Inflating
      // set name of cafeteria

      final String name = forms.get(groupPosition).restaurant;

      if (convertView == null) {
        // when view is not made yet
        convertView = LayoutInflater.from(context).inflate(R.layout.cafe_list, null);
        // layout > cafe_list.xml
        // inflating = making View from code in xml
      }

      restaurantName = (TextView) convertView.findViewById(R.id.cafe_name); // >> cafe_list.xml
      restaurantInfo = (Button) convertView.findViewById(R.id.show_info); // >> cafe_list.xml
      restaurantName.setText(name); // set cafe name on each View

      // signal = (TextView)convertView.findViewById(R.id.signal);
      // signal.setText(signalInGroup);

      restaurantInfo.setOnClickListener(new OnClickListener() {
        // when click info
        public void onClick(View v) {
          CafeInfoDialog dialog = new CafeInfoDialog(context, name, operatingHours, locations);
          dialog.setCanceledOnTouchOutside(true);
          dialog.show();
        }
      });

      return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
      View view = null;

      int numMenu = 0;
      for(int i = 0; i < forms.get(groupPosition).menus.length; i++) {
        if (forms.get(groupPosition).menus[i].name != null)
          numMenu++;
      }

      final RestaurantCrawlingForm.RestaurantInfo info = forms.get(groupPosition).menus[childPosition];

      if (convertView == null)
        view = LayoutInflater.from(context).inflate(R.layout.menu_details, null);
      else
        view = convertView;

      RelativeLayout menuLayout = (RelativeLayout) view.findViewById(R.id.menu_details); // menu_details.xml
      TextView price = (TextView) menuLayout.findViewById(R.id.menu_price); // menu_details.xml
      TextView name = (TextView) menuLayout.findViewById(R.id.menu_name); // menu_details.xml
      TextView time = (TextView) menuLayout.findViewById(R.id.menu_time); // menu_details.xml
      TextView noMenuLayout = (TextView) view.findViewById(R.id.no_menu); //menu_details.xml

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

      return view;
    }
  }
}
