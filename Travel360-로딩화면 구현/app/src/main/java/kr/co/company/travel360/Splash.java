package kr.co.company.travel360;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler hd = new Handler();
            hd.postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();       // 3 초후 이미지를 닫아버림
                }
            }, 3000);
        }

    }
