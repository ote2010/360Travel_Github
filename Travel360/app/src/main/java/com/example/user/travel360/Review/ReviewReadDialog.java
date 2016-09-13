package com.example.user.travel360;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ReviewReadDialog extends Dialog {

    Button Detail, Add_Traveler, Close;
    TextView Name, Date, Review_Text, Star_Num;
    ImageView Star;
    String a = "작년에 파리 갔다 왔습니다. 사실 여권도 없는데 뻥친거에요ㅠㅠ 그냥 리뷰 예시 쓰려구 이러구 있습니다ㅠㅠ 무슨 내용으로 리뷰를 적을까. 360Studio 파이팅!! 임베디드 소프트웨어 경진대회 대상이 목표입니다!!!! 전성일은 파호우 쿰척쿰척!!!!! 동영이는 카톡 답장좀해줘 제발!!!!!!";

    public ReviewReadDialog(Context context) {
        super(context);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_read_dialog);

        init();
        Review_Text.setText(a);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void init() {
        Detail = (Button) findViewById(R.id.dialog_datail_btn);
        Add_Traveler = (Button) findViewById(R.id.dialog_ad_traveler);
        Close = (Button) findViewById(R.id.dialog_close_btn);

        Name = (TextView) findViewById(R.id.dialog_user_name);
        Date = (TextView) findViewById(R.id.dialog_date);
        Star_Num = (TextView) findViewById(R.id.dialog_star_num);
        Review_Text = (TextView) findViewById(R.id.dialog_textview);

        Star = (ImageView) findViewById(R.id.dialog_star);

    }

}