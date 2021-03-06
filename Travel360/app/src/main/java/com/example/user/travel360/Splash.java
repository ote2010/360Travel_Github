package com.example.user.travel360;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
      //  MainActivity aActivity = (MainActivity)MainActivity.AActivity;//인터넷 연결 안됬을 시 종료시키기 위해 aActivity를 하나 만듬
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //위 세줄은 인터넷 연결 확인 관련 변수들
        if (wifi.isConnected() || mobile.isConnected()) {
            Handler hd = new Handler();
            hd.postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();       // 3 초후 이미지를 닫아버림
                }
            }, 100);
            //3000 -> 100으로 잠시 바꿈. 원활한 디버깅을 위해

        //연결되있다면 계속 진행한다.
        } else {
            Toast.makeText(getApplicationContext(), "인터넷 연결 안됨", Toast.LENGTH_SHORT).show();
           finish();
        }//연결이 안됬다면 메인 액티비티를 종료시킨다.


    }

}
