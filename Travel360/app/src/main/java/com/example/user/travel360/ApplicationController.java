package com.example.user.travel360;


import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class ApplicationController extends Application {
    // 사용자 속성 저장
    String email;
    String pw;
    String gender;
    int point;
    Boolean description;
    String search_res = "111"; // NFC로 찾고자 하는 매장 이름

    double latitude, longitude;

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


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPw(String Pw) {
        this.pw = Pw;

    }

    public void setGender(String gender) {
        this.gender = gender;

    }

    public String getEmail() {
        return email;
    }

    public String getPw() {
        return pw;
    }

    public String getGender() {
        return gender;
    }

    public Boolean getDescription() {
        return description;
    }

    public int getPoint() {
        return point;
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
    }


    public void initSharedPreference() {
        pref = getSharedPreferences("login", 0);
        edit = pref.edit();

        email = pref.getString("email", null);              // 이메일 가져옴
        pw = pref.getString("pw", null);                      // 비밀번호 가져옴
        gender = pref.getString("gender", null);           // 성별 가져옴


        pref = getSharedPreferences("option", 0);
        edit = pref.edit();


    }


}
