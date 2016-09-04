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
    Button FilterButton, Tstory, Review, Tag, New_turn, Detail_date, Reco_turn, User_near, Internal, external, Total, City, Total2, agic, Search_btn;
    RelativeLayout Btn_List1;
    LinearLayout Btn_List2, Btn_List3, Btn_List4;
    int chk = 0;
    boolean Lo_Tag = true;
    LocationManager l_manager;
    //사용자 위치 좌표
    public double latitude, longitude;
    int Year, Month, Day;
    // 버튼이 선택 됐는지 확인 하는 배열
    int[] checkList = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


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

        GregorianCalendar calendar = new GregorianCalendar();

        Search_btn = (Button) findViewById(R.id.search_btn);
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

                chk++;
                if (chk % 2 == 1) {
                    Btn_List1.setVisibility(View.VISIBLE);
                } else {
                    Btn_List1.setVisibility(View.INVISIBLE);
                    Btn_List2.setVisibility(View.INVISIBLE);
                    Btn_List3.setVisibility(View.INVISIBLE);
                }
                break;
            //index 0
            case R.id.Tstory:
                Btn_List2.setVisibility(View.VISIBLE);
                checkList[0]++;
                if (checkList[0] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    Tstory.setText("선택됌");
                } else {
                    // TODO 버튼 이미지 바뀌게
                    Tstory.setText("여행기");
                }

                break;
            //index 1
            case R.id.Review:
                Btn_List2.setVisibility(View.VISIBLE);
                checkList[1]++;
                if (checkList[1] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    Review.setText("선택됌");
                } else {
                    // TODO 버튼 이미지 바뀌게
                    Review.setText("리뷰");
                }
                break;
            //index 2
            case R.id.Tag:
                Btn_List2.setVisibility(View.VISIBLE);
                checkList[2]++;
                if (checkList[2] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    Tag.setText("선택됌");
                } else {
                    // TODO 버튼 이미지 바뀌게
                    Tag.setText("태크");
                }
                break;
            //index 3
            case R.id.New_turn:
                Btn_List2.setVisibility(View.VISIBLE);
                checkList[3]++;
                if (checkList[3] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    New_turn.setText("선택됌");
                } else {
                    // TODO 버튼 이미지 바뀌게
                    New_turn.setText("최신순");
                }
                break;
            //index 4
            case R.id.Detail_date:
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

                Btn_List2.setVisibility(View.VISIBLE);
                break;
            //index 5
            case R.id.Reco_turn:
                Btn_List2.setVisibility(View.VISIBLE);
                checkList[5]++;
                if (checkList[5] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    Reco_turn.setText("선택됌");
                } else {
                    // TODO 버튼 이미지 바뀌게
                    Reco_turn.setText("추천순");
                }
                break;
            //index 6
            case R.id.User_near:
                String provider = null;
                l_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                provider = LocationManager.NETWORK_PROVIDER;
                // locationUpdates(provider, 1초에 한번 , 최소거리 1m, 이벤트 감지자)
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }


                l_manager.requestLocationUpdates(provider, 10, 1, l_listener);

                Btn_List2.setVisibility(View.VISIBLE);
                break;
            //index 7
            case R.id.Internal:
                Btn_List2.setVisibility(View.VISIBLE);
                checkList[7]++;

                Log.d("@@@", checkList[7] + "/" + checkList[8]);
                if (checkList[7] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    external.setText("해외");
                    Internal.setText("선택됌");
                    if (checkList[8] != 0) {
                        checkList[8]++;
                    }
                } else {
                    // TODO 버튼 이미지 바뀌게
                    Internal.setText("국내");
                }
                break;
            //index 8
            case R.id.external:
                Btn_List2.setVisibility(View.VISIBLE);
                checkList[8]++;

                Log.d("@@@", checkList[7] + "/" + checkList[8]);
                if (checkList[8] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    Internal.setText("국내");
                    external.setText("선택됌");
                    if (checkList[7] != 0) {
                        checkList[7]++;
                    }
                } else {
                    // TODO 버튼 이미지 바뀌게
                    external.setText("해외");
                }
                break;
            //index 9
            case R.id.Total:
                Btn_List3.setVisibility(View.VISIBLE);
                checkList[9]++;
                if (checkList[9] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    Total.setText("선택됌");
                } else {
                    // TODO 버튼 이미지 바뀌게
                    Total.setText("전체");
                }
                break;
            //index 10
            case R.id.City:
                Btn_List3.setVisibility(View.VISIBLE);
                checkList[10]++;

                // TODO 버튼 이미지 바뀌게
                City.setText("선택됌");
                Total.setText("전체");
                final SelectCityDialog selectCityDialog = new SelectCityDialog(this);
                selectCityDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        String city = selectCityDialog.getCity();
                        Toast.makeText(getApplicationContext(), city, Toast.LENGTH_SHORT).show();
                    }
                });
                selectCityDialog.show();


                break;
            //index 11
            case R.id.Total2:
                checkList[11]++;
                if (checkList[11] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    Total2.setText("선택됌");
                } else {
                    // TODO 버튼 이미지 바뀌게
                    Total2.setText("전체");
                }
                break;
            //index 12
            case R.id.agic:
                checkList[12]++;
                if (checkList[12] % 2 == 1) {
                    // TODO 버튼 이미지 바뀌게
                    agic.setText("선택됌");
                    Total2.setText("전체");
                } else {
                    // TODO 버튼 이미지 바뀌게
                    agic.setText("동/구/읍/면 선택");
                }
                break;
            case R.id.search_btn:
                //TODO 앞에서 클릭했던 조건들 그리고 edittext로 받아온 string값을 이용해서 검색해야함 (서버필요 ㅠㅡㅠ)
                break;
        }
    }

//사용자 좌표값 받아오기
    LocationListener l_listener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // provider 상태가 변경되었을 때

        }

        @Override
        public void onProviderEnabled(String provider) {
            // gps를 사용할 수 있는 경우
            //  Toast.makeText(getApplicationContext(), "find provider", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            // GPS가 꺼져있는 경우
            //  Toast.makeText(getApplicationContext(), "no found provider", Toast.LENGTH_SHORT).show();
            Intent gpsOptionIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionIntent);
        }

        @Override
        public void onLocationChanged(Location location) {
            // 현재 위치의 값이 변경될 때 수행되는 메소드
            String str = null;


            Log.d("GPS@", "111");
            //위도와 경도

            if (Lo_Tag == true) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                if (location != null) {
                    Log.d("GPS@", "222");
                    User_near.setText(String.valueOf(latitude) + "/" + String.valueOf(longitude));
                } else {
                    //  Toast.makeText(getApplicationContext(), "onLocationChanged() : location is null", Toast.LENGTH_SHORT).show();
                }


            }
            Lo_Tag = false;
        }
    };


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
