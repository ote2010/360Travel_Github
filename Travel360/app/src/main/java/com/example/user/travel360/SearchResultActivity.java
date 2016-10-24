package com.example.user.travel360;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SearchResultActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;

    String cate="-1", cateDetail="-1" ,sDate="-1", eDate="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_Holo_Light_NoActionBar_TranslucentDecor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar1));
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        cate = intent.getStringExtra("searchCate");
        cateDetail = intent.getStringExtra("searchCateDetail");
        sDate = intent.getStringExtra("searchStartDate");
        eDate = intent.getStringExtra("searchEndDate");
        Log.d("FRAG_ACIT", "result : " + this.cate + " , " +   this.cateDetail + " , " + this.sDate + " , " + this.eDate );
        Toast.makeText(getApplicationContext(),"result : " + this.cate + " \n " +   this.cateDetail + " \n " + this.sDate + " \n " + this.eDate ,Toast.LENGTH_SHORT).show();
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
                        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.travel_button));
                        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.review_button_grey));
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.travel_button_grey));
                        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.review_button));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.travel_button));
        tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.review_button_grey));
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabscroll));

        mSectionsPagerAdapter.search_location(cate, cateDetail ,sDate, eDate);

    }

    //--------------------------------------------------------fragment tab 추가

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public void search_location(String cate, String cateDetail ,String sDate, String eDate){
            //new com.example.user.travel360.Fragment.Search_story_fragment().printif(cate, cateDetail ,sDate, eDate);
            new com.example.user.travel360.Fragment.Search_story_fragment().search_location(cate, cateDetail ,sDate, eDate);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0)
                return new com.example.user.travel360.Fragment.Search_story_fragment();
            else
                return new com.example.user.travel360.Fragment.Search_review_fragment();

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
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

}
