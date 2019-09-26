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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AllegyPopup extends Activity {

    TextView txtText;
    ArrayList<Integer> Allegy_Exgist_index;
    int Allegy_Exgist_Num;
    Boolean  Allegy_Exgist_CheckBox[];
    ListView listview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.allegy_popup_activity);
        //초기화
        Allegy_Exgist_index = new ArrayList<Integer>();
        Allegy_Exgist_index.clear();
        Allegy_Exgist_Num = 0;
        //밑에 데이터베이스에 있는 데이터를 넣기 만들기
        Allegy_Exgist_index = getIntent().getIntegerArrayListExtra("Allegy_Exgist_index");
        Allegy_Exgist_Num = getIntent().getIntExtra("Allegy_Exgist_Num", 0);



        // 빈 데이터 리스트 생성.
        final ArrayList<String> items = new ArrayList<String>() ;

        items.add("게");        items.add("새우");        items.add("땅콩");        items.add("호두");
        items.add("대두");        items.add("밀");        items.add("메밀");        items.add("우유");
        items.add("난류");        items.add("생선류");        items.add("오징어");        items.add("조개");
        items.add("닭도리");        items.add("돼지고기");        items.add("쇠고기");        items.add("딸기");
        items.add("망고");        items.add("멜론");        items.add("바나나");        items.add("사과");
        items.add("살구");        items.add("오렌지");        items.add("자두");        items.add("참외");
        items.add("체리");        items.add("키위");        items.add("복숭아");        items.add("토마토");
        items.add("계피");        items.add("마늘");        items.add("버섯");        items.add("당근");
        items.add("오이");        items.add("쌀");        items.add("번데기");

        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)

                tv.setTextColor(Color.BLACK);


                // Generate ListView Item using TextView
                return view;
            }
        };


        // listview 생성 및 adapter 지정.

        listview = (ListView) findViewById(R.id.Allegy_List) ;
        listview.setAdapter(adapter) ;

        Allegy_Exgist_CheckBox = new Boolean[listview.getCount()];
        for(int j = 0; j< listview.getCount(); j++){
            Allegy_Exgist_CheckBox[j] = false;
        }


        for(int i = 0; i < Allegy_Exgist_Num; i++){
            listview.setItemChecked( Allegy_Exgist_index.get(i), true);
            Allegy_Exgist_CheckBox[Allegy_Exgist_index.get(i)] = true;
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);

                Allegy_Exgist_CheckBox[index] = !Allegy_Exgist_CheckBox[index];

                Toast.makeText(AllegyPopup.this,  clickItemObj.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        /* //Item 안에 있는 거 확인
        for(int i = 0; i < listview.getCount(); i++){
           Log.d("메이플" ,listview.getItemAtPosition(i).toString());
        }*/

        /*
        //UI 객체생성
        txtText = (TextView)findViewById(R.id.txtText);

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        txtText.setText(data);
        */
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        Allegy_Exgist_index.clear();
        Allegy_Exgist_Num = 0;
        for(int i = 0; i < listview.getCount(); i++){
            if(Allegy_Exgist_CheckBox[i] == true){
                Allegy_Exgist_index.add(i);
                Allegy_Exgist_Num++;
            }
        }
        if(Allegy_Exgist_Num == 0){
            Allegy_Exgist_index.add(9999);
        }

        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        intent.putExtra("Allegy_Exgist_index", Allegy_Exgist_index);
        intent.putExtra("Allegy_Exgist_Num", Allegy_Exgist_Num);
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
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
        //안드로이드 백버튼 막기
        return;
    }
}
