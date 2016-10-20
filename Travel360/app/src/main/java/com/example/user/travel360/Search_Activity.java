package com.example.user.travel360;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.travel360.CustomDialog.TFDatePickerDialog;
import com.example.user.travel360.CustomList.GpsInfo;

public class Search_Activity extends Activity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener{

    TFDatePickerDialog tfDatePickerDialog;
    int Year, Month, Day;
    final int RESULT_SEARCH = 1000;
    CheckBox storyCheck, reviewCheck;
    LinearLayout cateLayout, dateLayout;
    RadioGroup radioGroup;
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
                    intent.putExtra("searchCate", cate);
                    intent.putExtra("searchCateDetail", cateDetail);
                    intent.putExtra("searchStartDate", sDate);
                    intent.putExtra("searchEndDate", eDate);
                    setResult(RESULT_OK, intent);
                    finish();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radioNear:
                cateDetail = "0";
                dateLayout.setVisibility(View.GONE);
                gps = new GpsInfo(this);
                if (gps.isGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    sDate = String.valueOf(latitude);
                    eDate = String.valueOf(longitude);

                    Toast.makeText(
                            getApplicationContext(),
                            "위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();
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
                        String ToD = tfDatePickerDialog.getToDate();
                        String FromD = tfDatePickerDialog.getFromDate();
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

    public void init() {
        cateLayout = (LinearLayout)findViewById(R.id.cateLayout);
        dateLayout = (LinearLayout)findViewById(R.id.dateLayout);

        storyCheck = (CheckBox)findViewById(R.id.storyCheck);
        reviewCheck = (CheckBox)findViewById(R.id.reviewCheck);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        startDate = (EditText)findViewById(R.id.startDate);
        endDate = (EditText)findViewById(R.id.endDate);

        searchOkBtn = (Button)findViewById(R.id.searchOkBtn);

        storyCheck.setOnCheckedChangeListener(this);
        reviewCheck.setOnCheckedChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        searchOkBtn.setOnClickListener(this);

        searchCancleBtn = (Button)findViewById(R.id.searchCancleBtn);
        searchCancleBtn.setOnClickListener(this);
    }
}
