package com.example.user.travel360;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.Navigationdrawer.ApplicationController;
import com.example.user.travel360.Navigationdrawer.FriendActivity;
import com.example.user.travel360.Navigationdrawer.LoginActivity;
import com.example.user.travel360.Navigationdrawer.UserActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;
    SharedPreferences.Editor edit;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    RelativeLayout LayoutLogin;
    LinearLayout LayoutNoLogin;
    ImageView UserProfileImg;
    NavigationView navigationView;
    TextView UserNameTextView, UserIDTextView;
    Button Logoutbtn;
    FragmentManager manager; // 프레그먼트를 관리하는 클래스의 참조변수
    FragmentTransaction tran; // 프레그먼트를 추가/삭제/재배치 하는 클래스의 참조변수
    Fragment timeLabelFragment; // 프래그먼트 참조 변수

    public static Activity AActivity;//메인 액티비티를 Splash 화면에서 종료시키기 위해 선언
    TabLayout tabLayout;

    int userSeq = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        LayoutLogin = (RelativeLayout) findViewById(R.id.LayoutLogin);
        LayoutNoLogin = (LinearLayout) findViewById(R.id.LayoutNoLogin);
        UserProfileImg = (ImageView) findViewById(R.id.UserProfileImageView);
        UserNameTextView = (TextView) findViewById(R.id.UserNameTextView);
        UserIDTextView = (TextView) findViewById(R.id.UserIDTextView);
        Logoutbtn = (Button) findViewById(R.id.Logoutbtn);
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

        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutNoLogin.setVisibility(View.VISIBLE);
                LayoutLogin.setVisibility(View.INVISIBLE);
                ApplicationController.getInstance().setEmail(null);
            }
        });
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
                    intent.putExtra("seq", userSeq);
                    startActivity(intent);
                }
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
            UserIDTextView.setText(LoginFlag);
            LayoutNoLogin.setVisibility(View.INVISIBLE);
            LayoutLogin.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent intent = new Intent(getApplicationContext(), Search_Activity.class);
            startActivity(intent);

            // Toast.makeText(getApplicationContext(),"search click", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) // 네비게이션 항목이 선택되면 불리는 함수
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.message) {
            // Handle the camera action
        } else if (id == R.id.scrap) {

        } else if (id == R.id.mytext) {

        } else if (id == R.id.myreply) {

        }
        // 여기서부터 추가한 사항들입니다. 그냥 그대로 추가했습니다.
        else if (id == R.id.setting) {

        } else if (id == R.id.notice) {

        } else if (id == R.id.vr) {

        }else if (id == R.id.nav_friend) {
            Intent intent = new Intent( MainActivity.this,FriendActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_search){
            Intent intent = new Intent( MainActivity.this,Search_Activity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
