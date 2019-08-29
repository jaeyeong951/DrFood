package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class allergy_corrspond_popup extends Activity {
    TextView txtText,title;
    private ArrayList<String> Allergy_user_have;
    private int Allergy_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.allergy_correspond_popup);

        Intent intent = getIntent();
        Allergy_user_have = intent.getExtras().getStringArrayList("일치알러지");
        Allergy_num = intent.getExtras().getInt("일치알러지갯수");
        //UI 객체생성

        txtText = (TextView)findViewById(R.id.txtText);
        title = (TextView)findViewById(R.id.title);
        String text = "";
        String num = Integer.toString(Allergy_num);
        for(int i = 0; i < Allergy_num; i++){
            if(i == 0) {
                text = text + Allergy_user_have.get(i);
            }
            else{
                text = text + ", " + Allergy_user_have.get(i);
            }
        }

        txtText.setText(text+" 알러지 위험이 있습니다!");
        title.setText(num + "개의 알러지 위험!");

//        //데이터 가져오기
//        Intent intent = getIntent();
//        String data = intent.getStringExtra("data");
//        txtText.setText(data);
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
