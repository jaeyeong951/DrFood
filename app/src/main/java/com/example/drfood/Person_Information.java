package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Person_Information extends Activity {

    TextView txtResult;
    Button Save_btn;
    Button Go_Back;

    Intent Main_Back;

    //받은 것들
    private String UserUid;
    private String UserEmail;
    private String UserName;

    //보낼 것들
    ArrayList<Integer> Allegy_Exgist_index;
    String Trans_Allegy_Exgist_index;
    int Allegy_Exgist_Num;
    int OneMale_TwoFemale;
    int Age;

    //데이터베이스
    private DatabaseReference mDatabase;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_infomation);
        //초기화
        Trans_Allegy_Exgist_index = "";

        Main_Back = new Intent();

        Intent Got_Data = getIntent();
        UserUid = Got_Data.getStringExtra("UserUid");
        UserEmail = Got_Data.getStringExtra("UserEmail");
        UserName = Got_Data.getStringExtra("UserName");
        Allegy_Exgist_index = Got_Data.getIntegerArrayListExtra("Allegy_Exgist_index");
        Allegy_Exgist_Num = Got_Data.getIntExtra("Allegy_Exgist_Num",0);
        Trans_Allegy_Exgist_index = Got_Data.getStringExtra("Trans_Allegy_Exgist_index");

        //데이터베이스가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Save_btn = (Button)findViewById(R.id.User_Information_Save);
        Go_Back = (Button)findViewById(R.id.Go_Back);
        txtResult = (TextView)findViewById(R.id.txtResult);

        Save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Main_Back.putExtra("Male_or_Female", OneMale_TwoFemale);
                //Main_Back.putExtra("Age",Age);
                Main_Back.putExtra("Allegy_Exgist_index",Allegy_Exgist_index);
                Main_Back.putExtra("Allegy_Exgist_Num",Allegy_Exgist_Num);
                Main_Back.putExtra("Trans_Allegy_Exgist_index", Trans_Allegy_Exgist_index);
                Toast.makeText(Person_Information.this, "저장했습니다", Toast.LENGTH_SHORT).show();

                mDatabase.child("people").child(UserUid).child("알러지 번호").setValue(Trans_Allegy_Exgist_index);
                mDatabase.child("people").child(UserUid).child("알러지 개수").setValue(Allegy_Exgist_Num);

            }
        });

        Go_Back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setResult(3100, Main_Back);
                finish();
            }
        });

    }


    //버튼
    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, AllegyPopup.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Trans_Allegy_Exgist_index = "";
                //데이터 받기
                String result = data.getStringExtra("result");
                Allegy_Exgist_index = data.getIntegerArrayListExtra("Allegy_Exgist_index");
                Allegy_Exgist_Num = data.getIntExtra("Allegy_Exgist_Num",1);
                Log.d("intentAllegyExgistindex", "" + Allegy_Exgist_index.get(0));
                Log.d("intentAllegyExgistNum","" +Allegy_Exgist_Num);

                if(Allegy_Exgist_Num != 0){
                    for(int i = 0; i < Allegy_Exgist_Num; i++){
                        Trans_Allegy_Exgist_index = Trans_Allegy_Exgist_index + Allegy_Exgist_index.get(i) + "_";
                        //Log.d("TransAllegyExgistindex", Trans_Allegy_Exgist_index);
                    }
                }

                txtResult.setText(result);
            }
        }
    }


}
