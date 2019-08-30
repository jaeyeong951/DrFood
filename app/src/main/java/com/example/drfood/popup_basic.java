package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class popup_basic extends Activity {


   CheckBox Male;
   CheckBox Female;
   EditText Age;

   String Check_Age;
   Boolean Check_Male;
   Boolean Check_Female;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_basic);



        Check_Age = "";
        Check_Male = false;
        Check_Female = false;
        Male = (CheckBox) findViewById(R.id.남);
        Female = (CheckBox) findViewById(R.id.여);
        Age = (EditText) findViewById(R.id.Age);

        Check_Age = getIntent().getStringExtra("UserAge");

        Age.setText(Check_Age);
        if(getIntent().getStringExtra("UserS").equals("남자")){
            Check_Male = true;
            Male.setChecked(true);

        }else if(getIntent().getStringExtra("UserS").equals("여자")){
            Check_Male = true;
            Female.setChecked(true);
        }




        Male.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_Male = !Check_Male;
            }
        });

        Female.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_Female = !Check_Female;
            }
        });
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        Check_Age = Age.getText().toString();

        if(Check_Male && Check_Female){
            Toast.makeText(this, "성별 하나만 눌러주세요",Toast.LENGTH_SHORT).show();
        }else {
            //데이터 전달하기
            Intent intent = new Intent();
            intent.putExtra("result", "Close Popup");
            intent.putExtra("Check_Male", Check_Male);
            intent.putExtra("Check_Female", Check_Female);
            intent.putExtra("Check_Age", Check_Age);
            setResult(2000, intent);

            //액티비티(팝업) 닫기
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
