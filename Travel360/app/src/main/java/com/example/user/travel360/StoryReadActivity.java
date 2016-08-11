package com.example.user.travel360;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2016-08-09.
 */
public class StoryReadActivity extends AppCompatActivity
{
    // 이미지 업로드는 최대 10개까지 가능하도록 설정하자.
    ImageView[] ImageViewer = new ImageView[10]; // 뷰어 이미지뷰. 최대 10개.

    ImageView morepic4Viewer1; // 첫번째 사진
    ImageView morepic4Viewer2; // 두번째 사진
    ImageView morepic4Viewer3; // 세번째 사진
    FrameLayout otherimg; // +5 라고 표시되있는 프레임 레이아웃
    TextView otherimgCount; // +5 표시 텍스트뷰

    LinearLayout pic2container;
    LinearLayout pic3container;
    LinearLayout pic4container;
    LinearLayout morethanpic4container;

    int imgCount;
    boolean imgUploadCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_story_read);

        // 버튼 클릭을 지정하기 위한 id 찾기
        morepic4Viewer1 = (ImageView) findViewById(R.id.morepic4Viewer1);
        morepic4Viewer2 = (ImageView) findViewById(R.id.morepic4Viewer2);
        morepic4Viewer3 = (ImageView) findViewById(R.id.morepic4Viewer3);
        otherimg = (FrameLayout) findViewById(R.id.otherimg);
        // 서버에서 받아온 이미지 개수에 따라서 바꿔줘야 하는 이미지 개수 텍스트 뷰
        otherimgCount = (TextView) findViewById(R.id.otherimgCount);

        // *****여행기에서 사진이 업로드되었을때, 사진 개수에 따른 레이아웃 구성을 보여주기 위한 코드 ******

        pic2container = (LinearLayout) findViewById(R.id.pic2container);
        pic3container = (LinearLayout) findViewById(R.id.pic3container);
        pic4container = (LinearLayout) findViewById(R.id.pic4container);
        morethanpic4container = (LinearLayout) findViewById(R.id.morethanpic4container);

        ViewGroup.LayoutParams pms1 = pic2container.getLayoutParams();
        ViewGroup.LayoutParams pms2 = pic3container.getLayoutParams();
        ViewGroup.LayoutParams pms3 = pic4container.getLayoutParams();
        ViewGroup.LayoutParams pms4 = morethanpic4container.getLayoutParams();

        pms1.width = getResources().getDisplayMetrics().widthPixels;
        pms1.height = pms1.width;
        pms2.width = getResources().getDisplayMetrics().widthPixels;
        pms2.height = pms2.width;
        pms3.width = getResources().getDisplayMetrics().widthPixels;
        pms3.height = pms3.width;
        pms4.width = getResources().getDisplayMetrics().widthPixels;
        pms4.height = pms4.width;
        // *******************************************************************************************

        imgCount = 7; // 서버에서 불러올 이미지가 총 7개라고 가정.

        otherimgCount.setText("+" + String.valueOf(imgCount - 3));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            //Toast.makeText(this, "홈 아이콘 이벤트", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        if(id == R.id.menuButton){
            Toast.makeText(this,"메뉴 버튼 이벤트", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_read, menu);
        return true;
    }

    public void morepic4Viewer1Click(View v)
    {
        if(!imgUploadCheck)
        {
            ImageUpload();
            imgUploadCheck = true;
        }
        startActivity(new Intent(getApplicationContext(), ImageViewer.class));
    }

    public void morepic4Viewer2Click(View v)
    {
        if(!imgUploadCheck)
        {
            ImageUpload();
            imgUploadCheck = true;
        }
    }

    public void morepic4Viewer3Click(View v)
    {
        if(!imgUploadCheck)
        {
            ImageUpload();
            imgUploadCheck = true;
        }
    }

    public void otherimgClick(View v)
    {
        if(!imgUploadCheck)
        {
            ImageUpload();
            imgUploadCheck = true;
        }
    }

    public void ImageUpload()
    {
        /* 서버 개발이 완료되면 활용할 코드. 현재는 그냥 가정해서 일일히 ImageViewer에 할당해주는 것으로 한다.
        for(int i = 0; i < imgCount; i++)
        {
            ImageViewer[i] = new ImageView(getApplicationContext());

            // *** 서버에서 순차적으로 이미지를 받아오는 코드를 이 부분에서 작성하여 ImageViewer[i]에 할당해준다. ***
            ImageViewer[i].setImageResource(R.drawable.testimg1);
            // ***********************************************************************************************
        }
        */

        // !!!!! 가정해서 작성하는 코드 (실제로는 이렇게 안하고 위에서 처럼 할거임 !!!!!
        for(int i = 0; i < imgCount; i++)
        {
            ImageViewer[i] = new ImageView(getApplicationContext());
        }

        ImageViewer[0].setImageResource(R.drawable.testimg1);
        ImageViewer[1].setImageResource(R.drawable.testimg2);
        ImageViewer[2].setImageResource(R.drawable.hwiin);
        ImageViewer[3].setImageResource(R.drawable.testimg1);
        ImageViewer[4].setImageResource(R.drawable.testimg2);
        ImageViewer[5].setImageResource(R.drawable.hwiin);
        ImageViewer[6].setImageResource(R.drawable.testimg1);
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
}
