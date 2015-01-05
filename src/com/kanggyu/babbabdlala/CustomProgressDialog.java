package com.kanggyu.babbabdlala;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class CustomProgressDialog extends Activity{
	
	private Handler mHandler;
    private ProgressDialog dialog; 
     
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        mHandler = new Handler();
         
        dialog = new ProgressDialog(this);
        dialog.setMessage("잠시만 기다려 주세요.");
        dialog.setCancelable(false);
        dialog.show();
         
        mHandler.postDelayed(mRunnable, 5000);
         
    }
     
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
                CustomProgressDialog.this.finish();
            }
        }
    };

}
