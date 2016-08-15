package com.example.user.travel360;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Search_Activity extends Activity {

    int checkSum = 0;
    Button FilterButton, Tstory, Review, Tag, New_turn, Detail_date, Reco_turn, User_near, Internal, external, Total, City, Total2, agic;
    RelativeLayout Btn_List1;
    LinearLayout Btn_List2, Btn_List3, Btn_List4;
    Boolean chk1 = false;
    Boolean chk2 = false;
    Boolean chk3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
     //   requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
     //이게 최선   requestWindowFeature(Window.FEATURE_ACTION_BAR);
     //   requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
       // requestWindowFeature(Window.);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //팝업 외부 뿌연 효과
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //뿌연 효과 정도
        layoutParams.dimAmount = 0.7f;
        //적용
        getWindow().setAttributes(layoutParams);


        setContentView(R.layout.activity_search_);
    }

    public void init() {
        FilterButton = (Button) findViewById(R.id.FilterButton);
        Tstory = (Button) findViewById(R.id.Tstory);
        Review = (Button) findViewById(R.id.Review);
        Tag = (Button) findViewById(R.id.Tag);
        New_turn = (Button) findViewById(R.id.New_turn);
        Detail_date = (Button) findViewById(R.id.Detail_date);
        Reco_turn = (Button) findViewById(R.id.Reco_turn);
        User_near = (Button) findViewById(R.id.User_near);
        Internal = (Button) findViewById(R.id.Internal);
        external = (Button) findViewById(R.id.external);
        Total = (Button) findViewById(R.id.Total);
        City = (Button) findViewById(R.id.City);
        Total2 = (Button) findViewById(R.id.Total2);
        agic = (Button) findViewById(R.id.agic);
        Btn_List1 = (RelativeLayout) findViewById(R.id.BtnList1);
        Btn_List2 = (LinearLayout) findViewById(R.id.BtnList2);
        Btn_List3 = (LinearLayout) findViewById(R.id.BtnList3);
        Btn_List4 = (LinearLayout) findViewById(R.id.BtnList4);

        FilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Btn_List1.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.Tstory || v.getId() == R.id.Review || v.getId() == R.id.Tag) {
            chk1 = true;
        } else if (v.getId() == R.id.New_turn || v.getId() == R.id.Detail_date || v.getId() == R.id.Reco_turn) {
            chk2 = true;
        } else if (v.getId() == R.id.User_near || v.getId() == R.id.Internal || v.getId() == R.id.external) {
            chk3 = true;
        }

        if (chk1 == true && chk2 == true && chk3 == true) {
            Btn_List2.setVisibility(View.VISIBLE);
        }

    }
}
