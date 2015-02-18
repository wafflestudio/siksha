package com.wafflestudio.siksha.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class WidgetFetchService extends Service {
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private final static String jikYoungUrl = "http://www.snuco.com/html/restaurant/restaurant_menu1.asp";
    public final static String[] jikYoungCafes = {"학생회관식당", "3식당", "기숙사식당", "자하연식당", "302동식당", "동원관식당", "감골식당"};
    private Document jikYoungPage;

    public ArrayList<String> cafeMenuList;
    public ArrayList<String> cafeList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            cafeList = intent.getStringArrayListExtra(BabWidgetProvider.CAFE_LIST);
            for (int i = 0; i < cafeList.size(); i++) {
                Log.e("FetchCafeList", cafeList.get(i));
            }
        }

        fetchData();
        return super.onStartCommand(intent, flags, startId);
    }

    private void fetchData() {
        new Fetch().execute((Void) null);
    }

    public class Fetch extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... params) {
            try {
                jikYoungPage = Jsoup.connect(jikYoungUrl).timeout(10000).get();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        protected void onPostExecute(Boolean result) {
            if(result == true) {
                parsing();
                populateWidget();
            }
        }
    }

    private void parsing() {
        Elements jikYoungTable = jikYoungPage.select("table:has(img[alt=상품명])");
        cafeMenuList = new ArrayList<String>();
        for (int i = 0; i < jikYoungCafes.length; i++) {
            if (cafeList.contains(jikYoungCafes[i])) {
                Elements tr = jikYoungTable.select("tr:contains(" + jikYoungCafes[i] + ")");
                String preLunch = tr.select("td:nth-child(5)").text();
                preLunch = preLunch.replaceAll(" ", "/");
                String[] lunch = preLunch.split("/");

                for(int j = 0; j < lunch.length; j++)
                {
                    char start = lunch[j].charAt(0);
                    if(start == 'ⓐ')
                        lunch[j] = lunch[j].substring(1) + "#1700";
                    else if(start == 'ⓑ')
                        lunch[j] = lunch[j].substring(1) + "#2000";
                    else if(start == 'ⓒ')
                        lunch[j] = lunch[j].substring(1) + "#2500";
                    else if(start == 'ⓓ')
                        lunch[j] = lunch[j].substring(1) + "#3000";
                    else if(start == 'ⓔ')
                        lunch[j] = lunch[j].substring(1) + "#3500";
                    else if(start == 'ⓕ')
                        lunch[j] = lunch[j].substring(1) + "#4000";
                    else if(start == 'ⓖ')
                        lunch[j] = lunch[j].substring(1) + "#4500";
                    else if(start == 'ⓗ')
                        lunch[j] = lunch[j].substring(1) + "#Etc";
                }

                String temp = jikYoungCafes[i] + "!";
                for (int j = 0; j < lunch.length; j++) {
                    temp = temp + lunch[j].trim();
                }
                cafeMenuList.add(temp);
            }
        }
    }

    private void populateWidget() {
        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(BabWidgetProvider.DATA_FETCHED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        widgetUpdateIntent.putStringArrayListExtra(BabWidgetProvider.CAFE_MENU_LIST, cafeMenuList);
        sendBroadcast(widgetUpdateIntent);
        this.stopSelf();
    }
}
