package com.example.drfood;

import android.app.Activity;
import android.widget.Toast;

public class BackKeyClickHandler {
    private long backKeyClickTime = 0;
    private Activity activity;

    public  BackKeyClickHandler(Activity activity){
        this.activity = activity;
    }


    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyClickTime + 2000){
            backKeyClickTime = System.currentTimeMillis();
            showToast();
            return;
        }
        if(System.currentTimeMillis() <= backKeyClickTime + 2000){
            activity.finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
    public void showToast(){
        Toast.makeText(activity,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
    }
}
