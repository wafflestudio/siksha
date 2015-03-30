package com.wafflestudio.siksha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DownloadingJsonReceiver extends BroadcastReceiver {
  public static final String ACTION_PRE_DOWNLOAD = "com.wafflestudio.siksha.DownloadingJson.PRE_DOWNLOAD";
  public static final String ACTION_CURRENT_DOWNLOAD = "com.wafflestudio.siksha.DownloadingJson.CURRENT_DOWNLOAD";

  private OnCompleteDownloadListener onCompleteDownloadListener;

  public interface OnCompleteDownloadListener {
    public void onComplete();
    public void onFail();
  }

  public void setOnCompleteDownloadListener(OnCompleteDownloadListener onCompleteDownloadListener) {
    this.onCompleteDownloadListener = onCompleteDownloadListener;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    boolean isSuccess = intent.getBooleanExtra("is_success", false);
    String action = intent.getAction();
    Log.d("DownloadingReciever_Action : ", action);

    if (action != null && action.equals(ACTION_CURRENT_DOWNLOAD)) {
      if (isSuccess)
        onCompleteDownloadListener.onComplete();
      else
        onCompleteDownloadListener.onFail();
    }
  }
}