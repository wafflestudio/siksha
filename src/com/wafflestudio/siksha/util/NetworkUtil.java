package com.wafflestudio.siksha.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
  private static NetworkUtil networkUtil;
  private ConnectivityManager connectivityManager;

  private NetworkUtil() { }

  public static NetworkUtil getInstance() {
    if (networkUtil == null)
      networkUtil = new NetworkUtil();

    return networkUtil;
  }

  public void setConnectivityManager(Context context) {
    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  public boolean isOnline() {
    NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    if (mobileNetwork.isConnected() || wifi.isConnected())
      return true;
    else
      return false;
  }
}
