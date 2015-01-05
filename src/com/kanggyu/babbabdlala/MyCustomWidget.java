package com.kanggyu.babbabdlala;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyCustomWidget extends AppWidgetProvider {
	
	private static final String TAG = "MyCustomWidget";
	private Context context;
	
	@Override
	public void onEnabled(Context context) {
		Log.i(TAG, "======================= onEnabled() =======================");
		super.onEnabled(context);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		Log.i(TAG, "======================= onUpdate() =======================");
		
		this.context = context;
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		
		
		for(int i=0; i<appWidgetIds.length; i++){ 
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mycustomwidget);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		
		
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.i(TAG, "======================= onDeleted() =======================");
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		Log.i(TAG, "======================= onDisabled() =======================");
		super.onDisabled(context);
	}
	
	public void initUI(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i(TAG, "======================= initUI() =======================");
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mycustomwidget);

		Intent eventIntent 				= new Intent(Const.ACTION_EVENT);
		Intent activityIntent 			= new Intent(Const.ACTION_CALL_ACTIVITY);
		Intent dialogIntent 			= new Intent(Const.ACTION_DIALOG);
		
		Intent listIntent				= new Intent(Const.ACTION_LIST);

		PendingIntent eventPIntent 			= PendingIntent.getBroadcast(context, 0, eventIntent		, 0);
		PendingIntent activityPIntent 		= PendingIntent.getBroadcast(context, 0, activityIntent		, 0);
		PendingIntent dialogPIntent 		= PendingIntent.getBroadcast(context, 0, dialogIntent		, 0);
		
		PendingIntent listPIntent 		= PendingIntent.getBroadcast(context, 0, listIntent		, 0);

		views.setOnClickPendingIntent(R.id.btn_event          	, eventPIntent);
		views.setOnClickPendingIntent(R.id.btn_call_activity    , activityPIntent);
		views.setOnClickPendingIntent(R.id.btn_set_alram        , dialogPIntent);
		
		views.setOnClickPendingIntent(R.id.widget_list        , listPIntent);

		for(int appWidgetId : appWidgetIds) {
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) { 
		super.onReceive(context, intent);
		
		String action = intent.getAction();
		Log.d(TAG, "onReceive() action = " + action);
		
		// Default Recevier
		if(AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)){
			
		}
		else if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)){
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			initUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
		}
		else if(AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)){
			
		}
		else if(AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)){
			
		}
		
		// Custom Recevier
		else if(Const.ACTION_EVENT.equals(action)){
			Toast.makeText(context, "Receiver 수신 완료!!.", Toast.LENGTH_SHORT).show();
		}
		else if(Const.ACTION_CALL_ACTIVITY.equals(action)){
			callActivity(context);
		}
		else if(Const.ACTION_DIALOG.equals(action)){
			createDialog(context);
		}
		else if(Const.ACTION_LIST.equals(action)){
			showList(context);
		}
		
	}
	
	private void callActivity(Context context){  
		Log.d(TAG, "callActivity()");
		Intent intent = new Intent("arabiannight.tistory.com.widget.CALL_ACTIVITY");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent); 
	}
	
	
	private void createDialog(Context context){
		Log.d(TAG, "createDialog()");
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		 
        Intent Intent = new Intent("arabiannight.tistory.com.widget.CALL_PROGRESSDIALOG");
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);
         
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pIntent);
	}
	
	private void showList(Context context){
		Log.d(TAG, "showList()");
	}
	
}
