package com.example.user.travel360.Navigationdrawer;


import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;

public class ApplicationController extends Application {
    // 사용자 속성 저장
    int arr_len;
    String[] Text_bump = new String[1000];
    String email = null;
    String pw = null;
    String gender;
    int point;
    String seq;
    Boolean LoginFlag = false;
    String[] location = new String[1000];
    int[] userseq = new int[1000];
    long[] start_date = new long[1000];
    // Float [] evaluation = new Float[10];
    float[] evaluation = new float[1000];
    String start[] = new String[1000];
    String Location;
    // SharedPreference
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    /**
     * Application 클래스를 상속받은 ApplicationController 객체는 어플리케이션에서 단 하나만 존재해야 합니다.
     * 따라서 내부에 ApplicationController 형의 instance를 만들어준 후
     * getter를 통해 자신의 instance를 가져오는 겁니다.
     */
    // ApplicationController 인스턴스 생성 및 getter 설정
    private static ApplicationController instance;

    public static ApplicationController getInstance() {
        return instance;
    }

    /* public void setLoginFlag(boolean a) {
         this.LoginFlag = a;
     }
 */
    public int getArr_len() {
        return arr_len;
    }

    public String getText_bump(int i) {
        return Text_bump[i];
    }

    public Boolean getLoginFlag() {
        pref = getSharedPreferences("login", 0);
        edit = pref.edit();

        this.email = pref.getString("email", email);
        if (email.equals(null) == true) {
            return false;
        } else
            return true;
    }

    public String getEmail() {
        pref = getSharedPreferences("login", 0);
        edit = pref.edit();

        this.email = pref.getString("email", email);
//        Log.d("@seq@", seq);
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        edit.putString("email", e);
        editor.commit();

    }
    public void setLocation(String Lo){
        this.Location = Lo;
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        edit.putString("Location", Location);
        editor.commit();
    }
    public String getLocation1() {
        pref = getSharedPreferences("login", 0);
        edit = pref.edit();

        this.Location = pref.getString("Location", Location);
//        Log.d("@seq@", seq);

        return Location;
    }

    public String getSeq() {

        pref = getSharedPreferences("login", 0);
        edit = pref.edit();

        this.seq = pref.getString("seq", seq);
//        Log.d("@seq@", seq);
        return seq;
    }

    public void setSeq(String a) {
        this.seq = a;
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        edit.putString("seq", a);
        editor.commit();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 어플이 실행되자마자 ApplicationController가 실행됩니다.
         * 자신의 instance를 생성하고 networkService를 만들어줍니다.
         */
        Log.i("MyTag", "Application 객체가 가장 먼저 실행됩니다.");
        // 인스턴스 가져오고 서비스 실행
        ApplicationController.instance = this;

        this.initSharedPreference();
        //  this.showReviewRanking_Server();
    }

    public String getLocation(int i) {
        return location[i];
    }

    public float getEvalucation(int i) {
        return evaluation[i];
    }

    public String getStartDate(int i) {
        return start[i];
    }

    public void initSharedPreference() {
        pref = getSharedPreferences("login", 0);
        edit = pref.edit();

        this.email = pref.getString("email", email);              // 이메일 가져옴
        this.pw = pref.getString("pw", pw);                      // 비밀번호 가져옴
        this.gender = pref.getString("gender", null);           // 성별 가져옴
        // Toast.makeText(getApplicationContext(), pw + "/" + gender + "/" + email, Toast.LENGTH_SHORT).show();
        this.LoginFlag = pref.getBoolean("LoginFlag", true);
        pref = getSharedPreferences("option", 0);
        edit = pref.edit();


    }
//
//    void showReviewRanking_Server() {
//
//        AsyncHttpClient client = new AsyncHttpClient();
//
//        Log.d("SUN", "writeStory_Server()");
//        client.get("http://kibox327.cafe24.com/travelReviewRankingList.do", new AsyncHttpResponseHandler() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                Log.d("SUN_h", "statusCode : " + statusCode + " , response : " + new String(response));
//                String res = new String(response);
//
//                try {
//                    JSONObject object = new JSONObject(res);
//                    String objStr = object.get("reviews") + "";
//                    JSONArray arr = new JSONArray(objStr);
//                    for (int i = 0; i < arr.length(); i++) {
//                        JSONObject obj = (JSONObject) arr.get(i);
//                        //     Float a   = (float)obj.get("evaluation");
//                        //   evaluation[i] = a;
//                        location[i] = (String) obj.get("location");
//                        String b = (String) obj.get("location");
//                        location[i] = b;
//                        // String text  = (String)obj.get("text");
//                        int c = (int) obj.get("seq");
//                        userseq[i] = c;
//                        long d = (long) obj.get("write_date_client");
//                        //start_date[i]=d;
//                        // long finish_date = (long)obj.get("finish_date_client");
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
//                        start[i] = df.format(d);
//                        // String finish = df.format(finish_date);
//                        Log.d("SUN_h", "evaluation : " + evaluation[i] + " " + " , location : " + location[i] + " , text : " + " , userseq : " + userseq[i] + " , start : " + start[i]);
//                        if (i < 3) {
////                           rankPlaceTextView[i].setText(location[i] + "");
//                            //                         rankEvaluationTextView[i].setText(evaluation[i] + "");
//                        } else {
//                            //  view.setPlace(location[i]);
//                            //   view.setEvaluation(evaluation[i]+"");
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.d("SUN_h", "e : " + e.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.d("SUN_확인", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
//            }
//
//            @Override
//            public void onRetry(int retryNo) {
//            }
//        });
//        //  Log.d("SUN_h", "lenth" + location.length);
//    }




}