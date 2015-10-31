package com.wafflestudio.siksha.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wafflestudio.siksha.form.AppVersion;
import com.wafflestudio.siksha.form.InformationJSON;
import com.wafflestudio.siksha.form.LatestData;
import com.wafflestudio.siksha.util.Date;
import com.wafflestudio.siksha.util.Preference;
import com.wafflestudio.siksha.widget.WidgetProvider;

import java.io.FileOutputStream;
import java.io.IOException;

public class JSONDownloader {
    private static final String SERVER_URL = "http://kanggyu94.fun25.co.kr:13204";
    private static final String REDIRECT_SERVER_URL = "http://kanggyu94.fun25.co.kr:13204";

    private static final String ROUTE_MENU_VIEW = "/menus/view";
    private static final String ROUTE_INFORMATION_VIEW = "/informations/view";
    private static final String ROUTE_INFORMATION_LATEST = "/informations/latest";
    private static final String ROUTE_LATEST_APP_VERSION_CHECK = "/version";

    private static final String QUERY_TODAY = "?date=today";
    private static final String QUERY_TOMORROW = "?date=tomorrow";

    private static final int OPTION_CRAWLING_INSTANTLY = 0;
    private static final int OPTION_CACHED_TODAY = 1;
    private static final int OPTION_CACHED_TOMORROW = 2;

    private static final int REQUEST_TYPE_CHECK = 0;
    private static final int REQUEST_TYPE_DOWNLOAD = 1;

    private Context context;

    private String action;
    private int option;

    public JSONDownloader(Context context, String action) {
        this.context = context;

        this.action = action;
        this.option = chooseDownloadOption();
    }

    public static boolean isJSONUpdated(Context context) {
        String latestDownload = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_LATEST_MENU_DATA);
        Log.d("isJSONUpdated()", "latest_download : " + latestDownload);

        return latestDownload.equals(Date.getDate());
    }

    private void compareLocalAndServerData(final UpdateCallback callback) {
        final String localLatestData = Preference.loadStringValue(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_LATEST_INFORMATION_DATA);
        Log.d("cmpLocalAndServerData()", "local_latest_data" + localLatestData);

        fetchJSONFromServer(new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                String serverLatestData = new Gson().fromJson(response, LatestData.class).latest;
                Log.d("cmpLocalAndServerData()", "server_latest_data : " + serverLatestData);
                if (localLatestData.equals(serverLatestData))
                    callback.onUpdated();
                else
                    callback.onNotUpdated();
            }

            @Override
            public void onFailure() {
                callback.onUpdated();
            }
        }, REQUEST_TYPE_CHECK);
    }

    private int chooseDownloadOption() {
        int hour = Date.getHour();
        int minute = Date.getMinute();

        if (hour == 0 && minute < 5)
            return OPTION_CRAWLING_INSTANTLY; // request server for crawling web page instantly
        else if (hour < 21)
            return OPTION_CACHED_TODAY; // request server for fetching cached json about today contents
        else
            return OPTION_CACHED_TOMORROW; // request server for fetching cached json about tomorrow contents
    }

    private String getFullURL(boolean isAlive, int requestType) {
        final String BASE_URL = isAlive ? SERVER_URL : REDIRECT_SERVER_URL;
        String URL = null;

        if (action.equals(JSONDownloadReceiver.ACTION_LATEST_APP_VERSION_CHECK))
            URL = BASE_URL + ROUTE_LATEST_APP_VERSION_CHECK;
        else if (action.equals(JSONDownloadReceiver.ACTION_INFORMATION_DOWNLOAD)) {
            if (requestType == REQUEST_TYPE_DOWNLOAD)
                URL = BASE_URL + ROUTE_INFORMATION_VIEW;
            else if (requestType == REQUEST_TYPE_CHECK)
                URL = BASE_URL + ROUTE_INFORMATION_LATEST;
        }
        else {
            if (option == OPTION_CACHED_TODAY)
                URL = BASE_URL + ROUTE_MENU_VIEW + QUERY_TODAY;
            else if (option == OPTION_CACHED_TOMORROW)
                URL = BASE_URL + ROUTE_MENU_VIEW  + QUERY_TOMORROW;
            else
                URL = BASE_URL + ROUTE_MENU_VIEW;
        }

        return URL;
    }

    private void fetchJSONFromServer(final VolleyCallback callback, final int requestType) {
        final String PRIMARY_REQUEST = "PRIMARY_REQUEST" + "/" + action;
        final String RETRY_REQUEST = "RETRY_REQUEST" + "/" + action;
        final RequestQueue requestQueue = DownloadRequestManager.getInstance(context.getApplicationContext()).getRequestQueue();

        StringRequest primaryRequest = new StringRequest(Request.Method.GET, getFullURL(true, requestType),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("(1) : " + action, "request_type : " + requestType + " / success");
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestQueue.cancelAll(PRIMARY_REQUEST);

                StringRequest retryRequest = new StringRequest(Request.Method.GET, getFullURL(false, requestType),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("(2) : " + action, "request_type : " + requestType + " / success");
                                callback.onSuccess(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("(2) : " + action, "request_type : " + requestType + " / failure");
                        requestQueue.cancelAll(RETRY_REQUEST);
                        callback.onFailure();
                    }
                });
                retryRequest.setTag(RETRY_REQUEST);
                requestQueue.add(retryRequest);
            }
        });
        primaryRequest.setTag(PRIMARY_REQUEST);
        requestQueue.add(primaryRequest);
    }

    private void writeJSONOnInternalStorage(String data) {
        try {
            String fileName = action.equals(JSONDownloadReceiver.ACTION_INFORMATION_DOWNLOAD) ? "informations.json" : "menus.json";
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes("euc-kr"));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLatestDownloadTime(String key, String time) {
        Preference.save(context, Preference.PREF_APP_NAME, key, time);
        Log.d("updLatestDownloadTime()", action);
        Log.d("updLatestDownloadTime()", key + " / " + time);
    }

    private void updateRefreshTimestamp() {
        String timestamp = Date.getRefreshTimestamp();
        Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_REFRESH_TIMESTAMP, timestamp);
        Log.d("updRefreshTimestamp()", action + " / " + timestamp);
    }

    private void sendSignalToApp(String action, int callbackType) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("callback_type", callbackType);
        context.sendBroadcast(intent);
    }

    private void sendSignalToWidget(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    public void start() {
        if (action.equals(JSONDownloadReceiver.ACTION_LATEST_APP_VERSION_CHECK)) {
            fetchJSONFromServer(new VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    String latestAppVersion = new Gson().fromJson(response, AppVersion.class).latest;
                    Preference.save(context, Preference.PREF_APP_NAME, Preference.PREF_KEY_LATEST_APP_VERSION, latestAppVersion);
                    sendSignalToApp(action, JSONDownloadReceiver.TYPE_ON_SUCCESS);
                }

                @Override
                public void onFailure() {
                    sendSignalToApp(action, JSONDownloadReceiver.TYPE_ON_FAILURE);
                }
            }, REQUEST_TYPE_CHECK);
        }
        else if (action.equals(JSONDownloadReceiver.ACTION_INFORMATION_DOWNLOAD)) {
            compareLocalAndServerData(new UpdateCallback() {
                @Override
                public void onUpdated() { sendSignalToApp(action, JSONDownloadReceiver.TYPE_ON_SUCCESS); }

                @Override
                public void onNotUpdated() {
                    fetchJSONFromServer(new VolleyCallback() {
                        @Override
                        public void onSuccess(String response) {
                            writeJSONOnInternalStorage(response);
                            updateLatestDownloadTime(Preference.PREF_KEY_LATEST_INFORMATION_DATA, new Gson().fromJson(response, InformationJSON.class).time);
                            sendSignalToApp(action, JSONDownloadReceiver.TYPE_ON_SUCCESS);
                        }

                        @Override
                        public void onFailure() {
                            sendSignalToApp(action, JSONDownloadReceiver.TYPE_ON_FAILURE);
                        }
                    }, REQUEST_TYPE_DOWNLOAD);
                }
            });
        }
        else {
            fetchJSONFromServer(new VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    writeJSONOnInternalStorage(response);
                    updateLatestDownloadTime(Preference.PREF_KEY_LATEST_MENU_DATA, Date.getDate());
                    updateRefreshTimestamp();
                    sendSignalToWidget(WidgetProvider.STATE_NEW_DATA_FETCHED);
                    sendSignalToApp(action, JSONDownloadReceiver.TYPE_ON_SUCCESS);
                }

                @Override
                public void onFailure() {
                    sendSignalToWidget(WidgetProvider.STATE_UPDATE_FAILURE);
                    sendSignalToApp(action, JSONDownloadReceiver.TYPE_ON_FAILURE);
                }
            }, REQUEST_TYPE_DOWNLOAD);
        }
    }

    interface VolleyCallback {
        void onSuccess(String response);
        void onFailure();
    }

    interface UpdateCallback {
        void onUpdated();
        void onNotUpdated();
    }
}