package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {

    ImageButton startButton;
    ImageButton Button;
    materialParser pharm;

    private final static int CAMERA_PERMISSIONS_GRANTED = 100;

    //데이터 베이스 쪽
    private DatabaseReference mDatabase;
    private String Snack_Name;
    private boolean Exgist_Result;
    private String UserUid;
    private String UserEmail;
    private String UserName;
    String rawMaterial;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터 베이스 주소 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //로그인 화면으로 넘어감
        Intent Go_Login_Page = new Intent(MainActivity.this,LoginPageAct.class);

        startActivityForResult(Go_Login_Page, 3000);
        //3000은 로그인



//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);


        startButton = findViewById(R.id.barcode);   // Button Boilerplate
        Button = findViewById(R.id.chips_button);

        getCameraPermission();

        // 다음 Activity로 넘어가기 위한 onClickListener
        // 이렇게 한 이유는 Permission Check 가
        // 기본적으로 UI Thread가 아닌 다른 Thread 에서 동시에 실행되기 때문에
        // 첫 실행 때, 권한이 없어서 SurfaceView 에서 addCallback 처리를 제대로 못하는 상황이 생긴다.
        // 그래서 검은 화면이 나온다. 고로, 아예 Activity를 다르게 해줬다.
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goNextActivity = new Intent(getApplicationContext(), QRCodeScan.class);
                startActivityForResult(goNextActivity, 1001);
            }
        });

    }

    private boolean getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // 권한이 왜 필요한지 설명이 필요한가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                Toast.makeText(this, "카메라 사용을 위해 확인버튼을 눌러주세요!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        CAMERA_PERMISSIONS_GRANTED);
                return true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            final String result = data.getStringExtra("key");
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.getKey().equals(result)){

                        //데이터베이스 안에 존재하면 true 없으면 false
                        Exgist_Result = true;

                        Snack_Name = dataSnapshot.getValue().toString();
                        Log.d("Snack name : ", Snack_Name);

                        pharm = new materialParser();
                        pharm.execute();

                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mDatabase.child("food").child("snack").child("바코드").addChildEventListener(childEventListener);


        }
        else if(requestCode == 3000){
            UserUid = data.getStringExtra("UserUid");
            UserName = data.getStringExtra("UserName");
            UserEmail = data.getStringExtra("UserEmail");
            Log.d("UserUid_Main", UserUid);
            Log.d("UserName_Main",UserName);
            Log.d("UserEmail_Main", UserEmail);

        }
    }

    class materialParser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //public final static String PHARM_URL = "http://openapi.hira.or.kr/openapi/service/pharmacyInfoService/getParmacyBasisList";
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://apis.data.go.kr/B553748/CertImgListService/getCertImgListService?serviceKey=%2BwvPpNobnpO%2BxNDsB3NdwZqjZYg4C8JqEy7NhZxXof%2F2Owy9Vu2eYP1pZVtIw%2FcPEVTx8nKQ1ph%2F4ppRNxKBLA%3D%3D&prdlstNm="
                        + Snack_Name);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                xpp.setInput(bis, "utf-8");

                int event_type = xpp.getEventType();

                ArrayList<String> materialList = new ArrayList<>();

                while (event_type != XmlPullParser.END_DOCUMENT) {
                    if (event_type == XmlPullParser.START_TAG) {
                        tag = xpp.getName();
                    } else if (event_type == XmlPullParser.TEXT) {
                        /*
                         * 성분만 가져와 본다.
                         */
                        if (tag.equals("rawmtrl")) {
                            if(!xpp.getText().equals("\n")){
                                rawMaterial = xpp.getText();
                            }
                        }
                    } else if (event_type == XmlPullParser.END_TAG) {
                        tag = xpp.getName();
                        if (tag.equals("item")) {
                            //Log.e("태그의 끝",rawMaterial);
                            //System.out.println(rawMaterial);
                            materialList.add(rawMaterial);
                        }
                        //item별로 분리
                    }
                    event_type = xpp.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            return rawMaterial;
        }

        @Override
        protected void onPostExecute(String material){
            String[] rawMaterialSplited = rawMaterial.split("\\(|\\)|\\{|\\}|\\[|\\]|\\,");
//            List<String> rawMatertialSplitedArray = new LinkedList<>(Arrays.asList(rawMaterialSplited));
            List<String> rawMaterialSplitedArray = new ArrayList<>();
            for(int i = 0; i < rawMaterialSplited.length; i++){
                rawMaterialSplitedArray.add(rawMaterialSplited[i]);
            }
            //이 밑은 재료중 ~산이라는 글자가 포함되면 삭제
            //검토중
            Iterator<String> rawIt = rawMaterialSplitedArray.iterator();
            while(rawIt.hasNext()){
                if(rawIt.next().contains("산")){
                    rawIt.remove();
                }
            }
            for(int i = 0; i < rawMaterialSplitedArray.size(); i++){
                Log.e("재려전부다",rawMaterialSplitedArray.get(i));
            }

        }
    }
}