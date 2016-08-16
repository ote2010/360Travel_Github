package com.example.user.travel360;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    RelativeLayout LayoutLogin;
    LinearLayout LayoutNoLogin;
    ImageView UserProfileImg;
    NavigationView navigationView;

    FragmentManager manager; // 프레그먼트를 관리하는 클래스의 참조변수
    FragmentTransaction tran; // 프레그먼트를 추가/삭제/재배치 하는 클래스의 참조변수
    Fragment timeLabelFragment; // 프래그먼트 참조 변수

    /*
    2016-07-23 PM 10:09 오탁은
    1)기존의 fab로 존재했었던 메시지 버튼을 삭제하고 관련 코드를 삭제했습니다.
    2)네비게이션 드로어의 xml 파일을 수정했습니다. 기존 네비게이션 드로어의 항목들을 바꾸었습니다.
    수정된 파일 : MainActivity.java, content_main.xml, nav_header_main.xml

    2016-07-24 AM 10:37 전성일
    로딩 화면 추가했습니다.
    앱 동작이 세로로만 동작하는 코드를 추가하였습니다.
    수정된 파일 : MainActivity.java, AndroidManifest.xml
    추가된 파일 : splash.xml, Splash.java

    2016-07-24 AM 12:53 오탁은
    1)액션바를 바꿨습니다. 방법은 app_bar_main.xml의 toolbar에 RelativeLayout을 집어넣어서 바꿨습니다.
      ->커스텀 액션바로 바꾸려고 했으나 이미 메니페스트에 NoActionBar로 정의되어있는 탓에 액션바를 커스텀 액션바로 바꾼다고 해도
      프로젝트에서 똑같은 액션바를 계속 쓰는 것이 아니기 때문에 이 방법이 제일 간단하고 좋은 것 같아서 이렇게 바꿉니다!
      추후에 다른 액션바가 필요하다면 xml로 정의해서 액티비티 상단에 붙히는 형식으로 진행하면 될 것 같습니다.
    수정된 파일 : app_bar_main.xml

    2016-07-27 PM 1:49 전성일
    - 앱 시작 시 인터넷 연결 확인 동작을 추가하였습니다.
    수정된 파일: AndroidManifest.xml, Splash.java, MainActivity.java

    2016-07-27 PM 3:39 오탁은
    - 유정이가 작업한 navi_test 프로젝트를 본 프로젝트에 포팅했습니다.
    프래그먼트 탭을 추가했습니다.
    추가된 파일 : layout.fragment로 시작되는 레이아웃, java.Fragment 폴더
    수정된 파일 : content_main.xml, activity_main.java 등등

    PM 11:35 오탁은
    - 여행기 메인화면 story_main.xml을 만들었습니다.
    아직 xml만 작성한 상태이고 java 코드는 아직 작성 못했습니다.

    2016-07-29 AM 1:15 오탁은
    - Story_fragment.java를 수정하고, story_main_item.xml을 작성했습니다.
    앞서 story_main은 제가 구성을 잡기 위해 xml을 작성해본 것이고, story_main_item.xml은 여행기 게시글 하나하나를
    서버에서 동적으로 가져온다고 가정하여 만든 xml입니다. (그러니까 story_main.xml은 그냥 참고용, story_main_item.xml을 실제로 사용)
    아직 다 만든건 아니고 좀 더 작업해야합니다.

    2016-07-29 PM 3:11 전성일
    - UserActivity.java(사용자 프로필 화면)를 추가하였습니다. 아직 구성은 못했습니다.
    UserActivity를 여는 동작은 R.id.ImageView1을 통해 수행됩니다. 해당 코드가 MainActivity에 있습니다.
    MainActivity 내 onCreateOptionsMenu에 해당 코드가 존재합니다.(이 곳이 아니라 onCreate에 하면 죽습니다. 참고바람!!)
    코드 자체는 단순해서 주석넣진 않을게요!^^
    추가된 파일 : UserActivity.java, activity_user.xml

    2016-07-29 PM 7:27 오탁은
    성일이가 한거 변수명 조금 바꾸고 color.xml 바꿨습니다.

    2016-07-31 AM 12:51 오탁은
    여행기 메인 구성 및 코드를 완성했습니다.
    그 과정에서 content_main.xml의 탭메뉴가 scroll이 되어서.. scroll 속성 없애버렸습니다
    그리고 Story_fragment.java의 코드를 많이 추가하고 수정했습니다.
    서버에서 정보를 받아온다는 가정 하에 모든 구성을 동적으로 게시하도록 코드를 짰습니다.
    그리고 그를 위해서 time_label.xml을 추가했습니다. 자세한 사항은 주석 확인 부탁드리고 다음에 모이게 되면 자세하게 설명드리겠습니다.
    추가된 파일 : time_label.xml
    수정된 파일 : Story_fragment.java, fragment_story_fragment.xml, story_main_item.xml

    2016-08-03 PM 05:33 오탁은
    여행지 리뷰 메인 구성 및 코드를 완성했습니다.
    Review_fragment.java 코드를 수정 및 추가했으며, 그 과정에서 ReviewListItemView.java 라는 클래스를 만들었습니다.
    리스트 아이템을 위한 클래스입니다. 그리고 review_main_listviewItem.xml과 review_main_rankitem.xml을 추가했습니다.
    전자는 리스트뷰 아이템을 위한 xml이고 후자는 순위권 여행지 리뷰를 위한 xml입니다.
    수정된 파일 : Review_fragment.java
    추가된 파일 : review_main_listviewItem.xml, review_main_rankitem.xml, ReviewListItemView.java

    2016-08-03 PM 06:02 전성일
    UserActivity에서 사용자 프로필 화면 상태 바를 없앴습니다.
    여행기 코드 부분을 그대로 UserActivity에 적용시켰습니다.
    activity_user.xml 을 완성하였습니다.
    수정된 파일 : UserActivity.java, activity_user.xml

    2016-08-04 PM 03:05 전성일
    글쓰기 화면 구성하였습니다.
    res/menu/story_write.xml 파일과 StoryWriteActivity.java 파일 참고 바랍니다.
    추가된 파일 : StoryWriteActivity.java, story_write.xml

    2016-8-2 김유정
    메인 액션바에 서치버튼을 추가하여 search_Activity를 인텐트로 실행되도록 했습니다.

    2016-08-05 PM 06:53 김유정
    메인화면 구현.
    fragment_main_fragment.xml 레이아웃 구현
    Main_fragment() 코드상 서버구축시 읽어올수 있도록하는 함수 readText(), reacImg() 함수 선언만 해놓음
    그리고 setmaxline() 함수를 통해 텍스트뷰에 써질 내용 라인 제한가능 ex) a.setMaxLine(5)하면 setText할 텍스트의 길이가 아무리 길어도 딱 5줄만 출력!!!!!!!!!!!!!!

    2016-08-10 AM 12:45 오탁은
    여행기, 리뷰 클릭시 본문 액티비티를 띄우는 코드 작성.
    세부 사항들은 아직 구현X. 코드는 성일이의 여행기 글쓰기를 많이 참고하여 따로 주석은 달지 않음.
    추가된 파일 : ReviewMainReadAcitivity.java, StoryReadActivity,java, activity_review_main_read.xml, activity_story_read.xml

    2016-08-11 PM 02:54 오탁은
    여행기 본문에 사진 추가. (사진이 올라온 여행기라고 가정. 뷰어 코드 작성중)
    사진 개수에 따른 레이아웃을 결정함. 각각 업로드된 사진이 2개일때, 3개일때, 4개일때, 4개 이상일때의 경우들.
    그에 따라 추가된 xml : story_2pic.xml, story_3pic.xml, story_4pic.xml, story_morethan4pic.xml

    2016-08-12 AM 12:04 오탁은
    PhotoView 라이브러리를 추가했습니다. (uk.co.senab.photoView)
    ImageViewer.class랑 activity_imageViewer.xml로 뷰어 액티비티를 추가했으며, 포토뷰 라이브러리를 성공적으로 포팅해서
    이미지가 줌도 되고 더블 탭하면 줌도 됩니다!!!끼야호

    2016-08-12 AM 01:25 오탁은
    PhotoView 라이브러리를 이용해서 이미지 뷰어 완성했습니다. 쓸기(스와이프?)도 됩니다 끼야호

    2016-08-13 AM 04:25 오탁은
    여행기 본문에서 사진을 웹 상에서 불러오는 코드로 수정 및 추가했습니다.
    AsyncTask를 이용해서 백그라운드에서 쓰레드로 이미지를 로드합니다.
    StoryReadActivity, ImageViewer.java 수정중

	2016-08-13 PM 06:41 오탁은
	여행기 본문 본격적으로 작업중. 현재 글 불러오기 코드 완성.

	2016-08-14 AM 05:25 오탁은
	여행기 본문 일단 완성. 보다 세부적인건 서버 구축이 어느정도 진행되면 다시 수정하겠음.

	2016-08-16 PM 07:00
	이미지 뷰어 완성. 뷰페이저 때문에 으아 내 멘탈
	*/

    public static Activity AActivity;//메인 액티비티를 Splash 화면에서 종료시키기 위해 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // 툴바
        setSupportActionBar(toolbar);


        //----프레그먼트 추가된 부분----
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //----프레그먼트 추가된 부분----

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        init();

        onClickBtn();

    }


    public void init() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);

        LayoutLogin = (RelativeLayout) findViewById(R.id.LayoutLogin);
        LayoutNoLogin = (LinearLayout) findViewById(R.id.LayoutNoLogin);
        UserProfileImg = (ImageView) findViewById(R.id.UserProfileImageView);

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

    //네비게이션 드로어에 항목을 추가하기 위해 코드를 추가했습니다.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) // 네비게이션 항목이 선택되면 불리는 함수
    {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }
        // 여기서부터 추가한 사항들입니다. 그냥 그대로 추가했습니다.
        else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_question) {

        } else if (id == R.id.nav_connectVR) {

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
                case 0:
                    return "메인";
                case 1:
                    return "여행기";
                case 2:
                    return "리뷰";
            }
            return null;
        }
    }
}
