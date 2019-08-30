package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Person_Information extends Activity {

    TextView txtResult;
    TextView Person_Email;
    TextView Person_Nick;
    ImageButton Back_Button;
    Button Save_btn;
    ListView listview;
    TextView Text_S;
    TextView Text_Age;

    ArrayAdapter adapter;

    Intent Main_Back;

    //받은 것들
    private String UserUid;
    private String UserEmail;
    private String UserName;
    private String UserAge;
    private String UserS;
    ArrayList<String> Allegy_Types;
    ArrayList<String> Apeal_Types;

    //보낼 것들
    ArrayList<Integer> Allegy_Exgist_index;
    String Trans_Allegy_Exgist_index;
    int Allegy_Exgist_Num;
    int OneMale_TwoFemale;

    //데이터베이스
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Bitmap bitmap;


    String Check_Age;
    Boolean Check_Male;
    Boolean Check_Female;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_infomation);
        //초기화
        Trans_Allegy_Exgist_index = "";
        mAuth = FirebaseAuth.getInstance();
        Allegy_Types = new ArrayList<String>();
        Apeal_Types = new ArrayList<String>();
        Apeal_Types.clear();
        Allegy_Types.clear();
        Check_Male = false;
        Check_Female = false;

        final FirebaseUser user = mAuth.getCurrentUser();
        CircularImageView user_profile = (CircularImageView)findViewById(R.id.User_Profile);
        Thread mThread= new Thread(){

            @Override

            public void run() {
                try{
                    URL url = new URL(user.getPhotoUrl().toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException ee) {
                    ee.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
            user_profile.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        Main_Back = new Intent();


        Intent Got_Data = getIntent();
        UserUid = Got_Data.getStringExtra("UserUid");
        UserEmail = Got_Data.getStringExtra("UserEmail");
        UserName = Got_Data.getStringExtra("UserName");
        UserAge = Got_Data.getStringExtra("UserAge");
        UserS = Got_Data.getStringExtra("UserS");
        Allegy_Exgist_index = Got_Data.getIntegerArrayListExtra("Allegy_Exgist_index");
        Allegy_Exgist_Num = Got_Data.getIntExtra("Allegy_Exgist_Num",0);
        Trans_Allegy_Exgist_index = Got_Data.getStringExtra("Trans_Allegy_Exgist_index");
        Allegy_Types = Got_Data.getStringArrayListExtra("Allegy_Types");
        Text_Age = (TextView)findViewById(R.id.나이);
        Text_S = (TextView)findViewById(R.id.성별);

        Main_Back.putExtra("Allegy_Exgist_index",Allegy_Exgist_index);
        Main_Back.putExtra("Allegy_Exgist_Num",Allegy_Exgist_Num);
        Main_Back.putExtra("Trans_Allegy_Exgist_index", Trans_Allegy_Exgist_index);

        //데이터베이스가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //txtResult = (TextView)findViewById(R.id.txtResult);
        Person_Email = (TextView)findViewById(R.id.person_email);
        Person_Nick = (TextView)findViewById(R.id.Person_Nick);
        Back_Button = (ImageButton)findViewById(R.id.back_button);
        listview = (ListView)findViewById(R.id.Allegy_List_User);



        //성별, 나이
        if(UserS.equals("모름")){
            Text_S.setVisibility(View.GONE);
        }else{
            Text_S.setText(UserS);
        }
        if(UserAge.equals("0")){
            Text_Age.setVisibility(View.GONE);
        }else{
            Text_Age.setText(UserAge + "세");
        }
        //이메일 부분 수정
        Person_Email.setText(UserEmail);
        //닉네임 부분 수정
        Person_Nick.setText(UserName);
        //나타내야할 알러지들
        for(int i = 0; i < Allegy_Exgist_Num; i++){
            Apeal_Types.add(Allegy_Types.get(Allegy_Exgist_index.get(i)));
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Apeal_Types){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);
                view.isHorizontalFadingEdgeEnabled();

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.BLACK);
                tv.setWidth(1000);

                // Generate ListView Item using TextView
                return view;
            }
        };

        listview.setAdapter(adapter);




        Back_Button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setResult(3100, Main_Back);
                finish();
            }
        });


    }

    //버튼
    public void mOnPopupClick2(View v){
        Intent Basic_info = new Intent(this, popup_basic.class);
        Basic_info.putExtra("UserAge", UserAge);
        Basic_info.putExtra("UserS", UserS);
        startActivityForResult(Basic_info, 2000);
    }

    //버튼
    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, AllegyPopup.class);
        intent.putExtra("data", "Test Popup");
        intent.putExtra("Allegy_Exgist_index",Allegy_Exgist_index);
        intent.putExtra("Allegy_Exgist_Num",Allegy_Exgist_Num);
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
                        Log.d("TransAllegyExgistindex", Trans_Allegy_Exgist_index);
                    }
                }
                Main_Back.putExtra("Allegy_Exgist_index",Allegy_Exgist_index);
                Main_Back.putExtra("Allegy_Exgist_Num",Allegy_Exgist_Num);
                Main_Back.putExtra("Trans_Allegy_Exgist_index", Trans_Allegy_Exgist_index);

                mDatabase.child("people").child(UserUid).child("알러지 번호").setValue(Trans_Allegy_Exgist_index);
                mDatabase.child("people").child(UserUid).child("알러지 개수").setValue(Allegy_Exgist_Num);

                Apeal_Types.clear();
                for(int i = 0; i < Allegy_Exgist_Num; i++){
                    Apeal_Types.add(Allegy_Types.get(Allegy_Exgist_index.get(i)));
                }
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Apeal_Types){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Get the Item from ListView
                        View view = super.getView(position, convertView, parent);
                        view.isHorizontalFadingEdgeEnabled();

                        // Initialize a TextView for ListView each Item
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);

                        // Set the text color of TextView (ListView Item)
                        tv.setTextColor(Color.BLACK);
                        tv.setWidth(1000);


                        // Generate ListView Item using TextView
                        return view;
                    }
                };
                listview.setAdapter(adapter);

            }
        }else if(requestCode==2000) {
            Check_Male = data.getBooleanExtra("Check_Male", false);
            Check_Female = data.getBooleanExtra("Check_Female", false);
            Check_Age = data.getStringExtra("Check_Age");
            if(!Check_Male && !Check_Female){
                UserS = "모름";
            }
            UserAge = Check_Age;
            if(Check_Female){
                Text_S.setText("여자");
                UserS = "여자";
                mDatabase.child("people").child(UserUid).child("성별").setValue("여자");
                Text_S.setVisibility(View.VISIBLE);
            }
            if(Check_Male){
                Text_S.setText("남자");
                UserS = "남자";
                mDatabase.child("people").child(UserUid).child("성별").setValue("남자");
                Text_S.setVisibility(View.VISIBLE);
            }
            if(UserS.equals("모름")){
                Text_S.setVisibility(View.GONE);
            }
            if(UserAge.equals("0")){
                Text_Age.setVisibility(View.GONE);
            }else{
                Text_Age.setText(UserAge + "세");
                Text_Age.setVisibility(View.VISIBLE);
            }


            mDatabase.child("people").child(UserUid).child("나이").setValue(Check_Age);
        }
    }


    @Override
    public void onBackPressed(){
        setResult(3100, Main_Back);
        finish();
    }

}
