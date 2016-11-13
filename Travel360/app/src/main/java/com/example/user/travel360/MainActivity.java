package com.example.user.travel360;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.CustomList.GpsInfo;
import com.example.user.travel360.Navigationdrawer.ApplicationController;
import com.example.user.travel360.Navigationdrawer.FriendActivity;
import com.example.user.travel360.Navigationdrawer.LoginActivity;
import com.example.user.travel360.Navigationdrawer.UserActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;
    SharedPreferences.Editor edit;
    LinearLayout Message, Scrap, MyText, MyReply, Setting, Notice, Ask, Vr, FriendList,Nav_Logout, Nav_Search;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    RelativeLayout LayoutLogin;
    LinearLayout LayoutNoLogin;
    ImageView UserProfileImg;
    NavigationView navigationView;
    TextView UserNameTextView;//, UserIDTextView;
    //Button Logoutbtn;
    FragmentManager manager; // 프레그먼트를 관리하는 클래스의 참조변수
    FragmentTransaction tran; // 프레그먼트를 추가/삭제/재배치 하는 클래스의 참조변수
    Fragment timeLabelFragment; // 프래그먼트 참조 변수

    public static Activity AActivity;//메인 액티비티를 Splash 화면에서 종료시키기 위해 선언
    TabLayout tabLayout;

    int userSeq = -1;


    public  String searchCate="-1", searchCateDetail="-1",searchStartDate="-1", searchEndDate="-1";
    final static int REQUEST_SEARCH = 1000;

    Context mContext;

    GpsInfo gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
       //setTheme(android.R.style.Theme_Holo_Light_NoActionBar_TranslucentDecor);





       setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // 툴바
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.home_button));
                        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.travel_button_grey));
                        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.review_button_grey));
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.home_button_grey));
                        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.travel_button));
                        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.review_button_grey));
                        break;
                    case 2:
                        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.home_button_grey));
                        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.travel_button_grey));
                        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.review_button));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });


        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.home_button));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.travel_button_grey));
        tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.review_button_grey));
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabscroll));
        //viewPager.setCurrentItem(tab.getPosition());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();

        onClickBtn();
        isLoginned();
    }


    public void init() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);

        LayoutLogin = (RelativeLayout)  headerview.findViewById(R.id.LayoutLogin);
        LayoutNoLogin = (LinearLayout)  headerview.findViewById(R.id.LayoutNoLogin);
        UserProfileImg = (ImageView) headerview. findViewById(R.id.UserProfileImageView);
        UserNameTextView = (TextView) headerview. findViewById(R.id.UserNameTextView);
        //UserIDTextView = (TextView) findViewById(R.id.UserIDTextView);
        //Logoutbtn = (Button) findViewById(R.id.Logoutbtn);
        Nav_Search = (LinearLayout)findViewById(R.id.nav_search);
        Nav_Logout = (LinearLayout)findViewById(R.id.nav_logout);
        Message = (LinearLayout) findViewById(R.id.message);
        Scrap = (LinearLayout) findViewById(R.id.scrap);
        MyText = (LinearLayout) findViewById(R.id.mytext);
        MyReply = (LinearLayout) findViewById(R.id.myreply);
        Setting = (LinearLayout) findViewById(R.id.setting);
        Notice = (LinearLayout) findViewById(R.id.notice);
        Ask = (LinearLayout) findViewById(R.id.ask);
        Vr = (LinearLayout) findViewById(R.id.vr);
        FriendList = (LinearLayout) findViewById(R.id.nav_friendList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) // 드로어가 열렸으면 닫기
        {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public void onClickBtn() {

        LayoutNoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        /*Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutNoLogin.setVisibility(View.VISIBLE);
                LayoutLogin.setVisibility(View.INVISIBLE);
                ApplicationController.getInstance().setEmail(null);
            }
        });*/
        UserProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserActivity.class);

                String LoginFlag = ApplicationController.getInstance().getEmail();

                boolean check = (LoginFlag + "").equals(null + "");

                if (check)
                {
                    Toast.makeText(getApplicationContext(), "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                } else
                {
                    userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());
                    intent.putExtra("userSeq", userSeq);
                    startActivity(intent);
                }
            }
        });
        Nav_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Search_Activity.class);
                startActivity(intent);
            }
        });
        FriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                startActivity(intent);
            }
        });
       Nav_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutNoLogin.setVisibility(View.VISIBLE);
                LayoutLogin.setVisibility(View.INVISIBLE);
                ApplicationController.getInstance().setEmail(null);
            }
        });
        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        MyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        MyReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Vr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        FriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                startActivity(intent);

            }
        });
    }

    public void isLoginned() {
        String LoginFlag = ApplicationController.getInstance().getEmail();

        boolean check = (LoginFlag + "").equals(null + "");

        if (check) {
            LayoutLogin.setVisibility(View.INVISIBLE);
            LayoutNoLogin.setVisibility(View.VISIBLE);
        } else {
            //UserIDTextView.setText(LoginFlag);
            LayoutNoLogin.setVisibility(View.INVISIBLE);
            LayoutLogin.setVisibility(View.VISIBLE);
            String user_seq = ApplicationController.getInstance().getSeq();
            Log.d("CHECK_SEQ", "SEQ : "+user_seq);
            getUserInfo_Server(user_seq);
        }
    }
    double latitude,longitude;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent intent = new Intent(getApplicationContext(), Search_Activity.class);
            startActivityForResult(intent,REQUEST_SEARCH);
            //startActivity(intent);
            // Toast.makeText(getApplicationContext(),"search click", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.visit){

            gps = new GpsInfo(this);
            if (gps.isGetLocation()) {

                 latitude = gps.getLatitude();
                 longitude = gps.getLongitude();

                if(latitude == 0.0 | longitude == 0.0){
                    Toast.makeText(getApplicationContext(), "정확한 위치를 위해 다시 실행해주세요.", Toast.LENGTH_LONG).show();
                }
                else {

                    LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflate.inflate(R.layout.dialog_visit, null);

                    final EditText visitEt = (EditText) layout.findViewById(R.id.visitEt);
                    Button visitBtn = (Button) layout.findViewById(R.id.visitBtn);

                    AlertDialog.Builder aDialog = new AlertDialog.Builder(mContext);
                    aDialog.setView(layout);

                    final AlertDialog ad = aDialog.create();

                    visitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(visitEt.getText().toString().equals(""))
                                Toast.makeText(getApplicationContext(), "내용을 입력해 주세요", Toast.LENGTH_LONG).show();
                            else if(ApplicationController.getInstance().getSeq() == null)
                            {
                                Toast.makeText(getApplicationContext(), "로그인 하셔야 사용 가능합니다.", Toast.LENGTH_LONG).show();
                            }
                            else {
                                userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());
                                Log.d("userSeq", String.valueOf(userSeq));
                                Toast.makeText(getApplicationContext(), "위도: " + latitude + "\n경도: " + longitude + "\n " + visitEt.getText().toString(), Toast.LENGTH_LONG).show();
                                writeVisit_Server(latitude,longitude , userSeq,visitEt.getText().toString());
                            }

                            ad.cancel();
                        }
                    });

                    ad.show();
                }

            } else {
                // GPS 를 사용할수 없으므로
                gps.showSettingsAlert();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) // 네비게이션 항목이 선택되면 불리는 함수
    {
        int id = item.getItemId();

//        if (id == R.id.nav_search) {
//            Intent intent = new Intent(MainActivity.this, Search_Activity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_friend) {
//            Intent intent = new Intent(MainActivity.this, FriendActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.message) {
//
//        } else if (id == R.id.scrap) {
//
//        } else if (id == R.id.mytext) {
//
//        } else if (id == R.id.myreply) {
//
//        } else if (id == R.id.setting) {
//
//        }else if (id == R.id.notice) {
//
//        }else if(id == R.id.vr){
//
//        }else if(id == R.id.logout)
//        {
//            LayoutNoLogin.setVisibility(View.VISIBLE);
//            LayoutLogin.setVisibility(View.INVISIBLE);
//            ApplicationController.getInstance().setEmail(null);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SEARCH:

                if (resultCode == RESULT_OK) {
                    searchCate = data.getStringExtra("searchCate");
                    searchCateDetail = data.getStringExtra("searchCateDetail");
                    searchStartDate = data.getStringExtra("searchStartDate");
                    searchEndDate = data.getStringExtra("searchEndDate");
                    Log.d("SAERCH", "검색확인 : " + searchCate + " " + searchCateDetail + " " + searchStartDate + " " + searchEndDate);
                    //mViewPager.setAdapter(mSectionsPagerAdapter);

                    Toast.makeText(MainActivity.this, "검색확인", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                    intent.putExtra("searchCate", searchCate);
                    intent.putExtra("searchCateDetail", searchCateDetail);
                    intent.putExtra("searchStartDate", searchStartDate);
                    intent.putExtra("searchEndDate", searchEndDate);
                    startActivity(intent);
                }
                else{
                    searchCate="-1";
                    searchCateDetail="-1";
                    searchStartDate="-1";
                    searchEndDate="-1";
                    //mViewPager.setAdapter(mSectionsPagerAdapter);
                    Log.d("SAERCH", "검색취소 : " +searchCate + " " + searchCateDetail + " " + searchStartDate + " " + searchEndDate);
                    Toast.makeText(MainActivity.this, "검색취소", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    //--------------------------------------------------------fragment tab 추가

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0)
                return new com.example.user.travel360.Fragment.Main_fragment();
            else if (position == 1)
                return new com.example.user.travel360.Fragment.Story_fragment();
            else
                return new com.example.user.travel360.Fragment.Review_fragment();
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            Log.d("@@@", position + "@@");
//            return PlaceholderFragment.newInstance(position);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                /*
                case 0:
                    return "메인";
                case 1:
                    return "여행기";
                case 2:
                    return "리뷰";*/
            }
            return null;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        isLoginned();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLoginned();
    }


    /************* 사용자 정보 **************/
    void getUserInfo_Server(String user_seq) {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq 만 있으면 됩니다.
        params.put("seq",user_seq);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getUserInfo_Server()");
        client.get("http://kibox327.cafe24.com/getUserInfo.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try{
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("userDto") + "";
                    JSONObject obj = new JSONObject(objStr);

                    String id = (String)obj.get("id");
                    String name = (String)obj.get("name");
                    String profile_image = (String)obj.get("profile_image");
                    getImage_Server(profile_image);
                    UserNameTextView.setText(id+"\n("+name+")");

                    Log.d("FRAG_ACIT", "profile_image : "+profile_image);


                }catch (JSONException e){

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {  }
        });
    }


    /***************  image 가져오기  *********************/

    public Bitmap byteArrayToBitmap(byte[] byteArray ) {  // byte -> bitmap 변환 및 반환
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return bitmap ;
    }


    void getImage_Server(String imgname) {

        RequestParams params = new RequestParams();
        // 보내는 data는 imageName 만 있으면 됩니다.
        params.put("imageName", imgname);
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getImage_Server()");
        client.get("http://kibox327.cafe24.com/Image.do", params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response)
            {
                // byteArrayToBitmap 를 통해 reponse로 받은 이미지 데이터 bitmap으로 변환
                Bitmap bitmap = byteArrayToBitmap(response);
                UserProfileImg.setImageBitmap(bitmap);

                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo)
            {
            }
        });
    }

    /* 방명록 */
    void writeVisit_Server(double lat, double lon, int userSeq,String message) {

        RequestParams params = new RequestParams();
        // 보내는 data는 imageName 만 있으면 됩니다.
        params.put("lat", lat);
        params.put("lon", lon);
        params.put("userSeq", userSeq);
        params.put("message", message);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getImage_Server()");
        client.get("http://kibox327.cafe24.com/insertMessage.do", params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response)
            {
                // byteArrayToBitmap 를 통해 reponse로 받은 이미지 데이터 bitmap으로 변환
                Bitmap bitmap = byteArrayToBitmap(response);
                UserProfileImg.setImageBitmap(bitmap);

                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                Toast.makeText(getApplicationContext(), "방명록을 남겼습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo)
            {
            }
        });
    }

}
