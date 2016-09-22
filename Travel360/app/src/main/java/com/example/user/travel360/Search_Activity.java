package com.example.user.travel360;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.travel360.Review.SelectCityDialog;
import com.example.user.travel360.uk.co.senab.photoview.TFDatePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Search_Activity extends Activity implements View.OnClickListener {

    TFDatePickerDialog tfDatePickerDialog;
    EditText SearchText;
    Button FilterButton, Tstory, Review, New_turn, Detail_date;
    LinearLayout Btn_List1;

    int chk = 0;
    boolean Lo_Tag = true;
    LocationManager l_manager;
    //사용자 위치 좌표
    public double latitude, longitude;
    int Year, Month, Day;
    // 버튼이 선택 됐는지 확인 하는 배열
    int[] checkList = {0, 0, 0, 0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //팝업 외부 뿌연 효과
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //뿌연 효과 정도
        layoutParams.dimAmount = 0.7f;
        //적용
        getWindow().setAttributes(layoutParams);


        setContentView(R.layout.activity_search_);

        init();

    }


    public void init() {
        FilterButton = (Button) findViewById(R.id.FilterButton);
        Tstory = (Button) findViewById(R.id.Tstory);
        Review = (Button) findViewById(R.id.Review);

        New_turn = (Button) findViewById(R.id.New_turn);
        Detail_date = (Button) findViewById(R.id.Detail_date);


        Btn_List1 = (LinearLayout) findViewById(R.id.BtnList1);


        GregorianCalendar calendar = new GregorianCalendar();


        SearchText = (EditText) findViewById(R.id.edit_search);


    }


    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FilterButton:


                break;
            //index 0
            case R.id.Tstory:
                if (checkList[0] == 0) {
                    checkList[0] = 1;
                    Tstory.setText("선택");
                } else if (checkList[0] == 1) {
                    checkList[0] = 0;
                    Tstory.setText("여행기");

                }

                break;
            //index 1
            case R.id.Review:
                if (checkList[1] == 0) {
                    checkList[1] = 1;
                    Review.setText("선택");
                    Review.setText("");
                } else if (checkList[1] == 1) {
                    checkList[1] = 0;
                    Review.setText("리뷰");
                }
                break;
            //index 2

            case R.id.New_turn:
                if (checkList[2] == 0) {
                    checkList[2] = 1;
                    New_turn.setText("선택");
                } else if (checkList[2] == 1) {
                    checkList[2] = 0;
                    New_turn.setText("최신순");
                }
                break;
            //index 3
            case R.id.Detail_date:
                if (checkList[0] == 0) {
                    checkList[0] = 1;
                    Detail_date.setText("선택");

                    //Dialog_DatePicker();
                    tfDatePickerDialog = new TFDatePickerDialog(this);
                    tfDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //TODO >>시작 날짜 끝날짜 받아오는거 했음 이걸 가지고 서버에 연동해서 해야함!
                            String ToD = tfDatePickerDialog.getToDate();
                            String FromD = tfDatePickerDialog.getFromDate();
                            Toast.makeText(getApplicationContext(), FromD + "\n" + ToD, Toast.LENGTH_SHORT).show();
                        }
                    });
                    tfDatePickerDialog.show();


                } else if (checkList[0] == 1) {
                    checkList[0] = 0;
                    Detail_date.setText("세부날짜선택");

                }
                break;

            case R.id.search_btn:
                //TODO 앞에서 클릭했던 조건들 그리고 edittext로 받아온 string값을 이용해서 검색해야함 (서버필요 ㅠㅡㅠ)
                break;
        }
    }


    private void Dialog_DatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Year = year;
                Month = monthOfYear + 1;
                Day = dayOfMonth;
                Detail_date.setText(Year + "/" + Month + "/" + Day);

            }
        };
        DatePickerDialog alert = new DatePickerDialog(this, mDateSetListener, year, month, day);

        alert.show();
    }


}
