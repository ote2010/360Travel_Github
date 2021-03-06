package com.example.user.travel360;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.travel360.CustomDialog.TFDatePickerDialog;
import com.example.user.travel360.CustomList.GpsInfo;

import java.util.Calendar;

public class Search_Activity extends Activity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener{

    Calendar calendar;
    TFDatePickerDialog tfDatePickerDialog;
    int Year, Month, Day;
    final int RESULT_SEARCH = 1000;
    CheckBox storyCheck, reviewCheck;
    LinearLayout cateLayout, dateLayout;
    RadioGroup radioGroup;
    RadioButton radioNear,radioDate;
    EditText startDate, endDate;
    String cate="-1", cateDetail="-1" ,sDate="-1", eDate="-1";
    Button searchOkBtn, searchCancleBtn;

    GpsInfo gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //팝업 외부 뿌연 효과
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //뿌연 효과 정도
        layoutParams.dimAmount = 0.7f;
        //적용
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_search);

        init();
    }

    @Override
    public void onClick(View v) {

        Intent intent = getIntent();
        switch (v.getId())
        {
            case R.id.searchOkBtn: // 검색 버튼
                if(Integer.parseInt(cate)>-1  &&  Integer.parseInt(cateDetail)>-1) {
                    if( sDate == null || eDate == null){
                        Toast.makeText(getApplicationContext(),"기간을 확인해 주세요 ",Toast.LENGTH_SHORT).show();
                    }
                    else if(sDate.equals("-1") && eDate.equals("-1")){
                        Toast.makeText(getApplicationContext(),"정확한 위치를 위해 다시한번 선택해주세요 ",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        intent.putExtra("searchCate", cate);
                        intent.putExtra("searchCateDetail", cateDetail);
                        intent.putExtra("searchStartDate", sDate);
                        intent.putExtra("searchEndDate", eDate);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"조건을 선택해주세요",Toast.LENGTH_SHORT).show();
                break;

            case R.id.searchCancleBtn:
                intent.putExtra("searchCate", "-1");
                intent.putExtra("searchCateDetail", "-1");
                intent.putExtra("searchStartDate",  "-1");
                intent.putExtra("searchEndDate",  "-1");
                setResult(RESULT_CANCELED, intent);
                finish();
                break;

            case R.id.radioNear:
                cateDetail = "0";
                dateLayout.setVisibility(View.GONE);
                gps = new GpsInfo(this);
                if (gps.isGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    if(latitude == 0.0 | longitude == 0.0){
                        Toast.makeText(getApplicationContext(), "정확한 위치를 위해 다시 눌러주세요.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        sDate = String.valueOf(latitude);
                        eDate = String.valueOf(longitude);

                        Toast.makeText(
                                getApplicationContext(),
                                "위도: " + latitude + "\n경도: " + longitude,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
                break;

            case R.id.radioDate:
                cateDetail = "1";
                dateLayout.setVisibility(View.VISIBLE);
                tfDatePickerDialog = new TFDatePickerDialog(this);
                tfDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        calendar = Calendar.getInstance();
                        String ToD = tfDatePickerDialog.getToDate();
                        String FromD = tfDatePickerDialog.getFromDate();
                        Log.d("DATE@@", "To : "+ToD+"From : "+FromD);
                        if(ToD.equals("empty")){
                            ToD = calendar.get(Calendar.YEAR)+"년 "+(calendar.get(Calendar.MONTH)+1)+"월 "+calendar.get(Calendar.DAY_OF_MONTH)+"일";

                        }
                        if(FromD.equals("empty")){
                            FromD = calendar.get(Calendar.YEAR)+"년 "+(calendar.get(Calendar.MONTH)+1)+"월 "+calendar.get(Calendar.DAY_OF_MONTH)+"일";
                        }
                        startDate.setText(FromD);
                        endDate.setText(ToD);
                        sDate = FromD;
                        eDate = ToD;
                        //Toast.makeText(getApplicationContext(), FromD + "\n" + ToD, Toast.LENGTH_SHORT).show();
                    }
                });
                tfDatePickerDialog.show();

                break;
        }
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(!storyCheck.isChecked() && !reviewCheck.isChecked()){
            cateLayout.setVisibility(View.GONE);
            cate = "-1";
        }
        else {
            cateLayout.setVisibility(View.VISIBLE);
            if (storyCheck.isChecked() && reviewCheck.isChecked()) { // 여행기 , 리뷰 검색 둘다
                cate = "2";
            } else if (!storyCheck.isChecked() && reviewCheck.isChecked()) { // 리뷰 검색만
                cate = "1";
            } else if (storyCheck.isChecked() && !reviewCheck.isChecked()) { // 여행기 검색만
                cate = "0";
            }
        }
    }


    public void init() {
        cateLayout = (LinearLayout)findViewById(R.id.cateLayout);
        dateLayout = (LinearLayout)findViewById(R.id.dateLayout);

        storyCheck = (CheckBox)findViewById(R.id.storyCheck);
        reviewCheck = (CheckBox)findViewById(R.id.reviewCheck);
        radioNear = (RadioButton)findViewById(R.id.radioNear);
        radioDate = (RadioButton)findViewById(R.id.radioDate);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        startDate = (EditText)findViewById(R.id.startDate);
        endDate = (EditText)findViewById(R.id.endDate);

        searchOkBtn = (Button)findViewById(R.id.searchOkBtn);

        storyCheck.setOnCheckedChangeListener(this);

        reviewCheck.setOnCheckedChangeListener(this);
       // radioGroup.setOnCheckedChangeListener(this);

        radioNear.setOnClickListener(this);
        radioDate.setOnClickListener(this);

        searchOkBtn.setOnClickListener(this);

        searchCancleBtn = (Button)findViewById(R.id.searchCancleBtn);
        searchCancleBtn.setOnClickListener(this);
    }
}
