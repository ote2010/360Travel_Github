package com.example.user.travel360;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2016-08-09.
 */
public class StoryReadActivitySample extends AppCompatActivity
{
    /*
    프로세스 정리
    1) 표현할 이미지 개수 imgCount를 서버에서 받아온다.
    2) imgCount가 4개 이상, 4개, 3개, 2개, 1개 일 때의 경우에 따라 동적으로 코드에서 만들어준다.
    3) 받아온게 사진인지 그림인지. 판단해서 순차적으로 보여줘야한다.
    */
    ImageView morepic4Viewer1; // 첫번째 사진
    ImageView morepic4Viewer2; // 두번째 사진
    ImageView morepic4Viewer3; // 세번째 사진
    ImageView morepic4Viewer4; // 네번째 사진
    FrameLayout otherimg; // +5 라고 표시되있는 프레임 레이아웃
    TextView otherimgCount; // +5 표시 텍스트뷰

    LinearLayout pic2container;
    LinearLayout pic3container;
    LinearLayout pic4container;
    LinearLayout morethanpic4container;

    int imgCount;
    Intent imgCountIntent;

    //쓰레드 작업을 위한 변수
    Bitmap[] bitmapImg = new Bitmap[10];
    ImageLoadingTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_story_read);

        // 버튼 클릭을 지정하기 위한 id 찾기
        morepic4Viewer1 = (ImageView) findViewById(R.id.morepic4Viewer1);
        morepic4Viewer2 = (ImageView) findViewById(R.id.morepic4Viewer2);
        morepic4Viewer3 = (ImageView) findViewById(R.id.morepic4Viewer3);
        morepic4Viewer4 = (ImageView) findViewById(R.id.morepic4Viewer4);
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

        // +숫자 텍스트 알맞게 고치기
        otherimgCount.setText("+" + String.valueOf(imgCount - 3));

        // 쓰레드 작업을 위한 코드
        task = new ImageLoadingTask();
        //이미지 URL : 1 파리 2 서울 3 프라하 4 리우데자네이루
        task.execute("http://www.gaviota.kr/xe/files/attach/images/163/900/003/PARIS_111001_14.jpg"
        , "http://cfd.tourtips.com/@cms_600/2015081735/gjexj7/%EC%84%9C%EC%9A%B8_%EA%B4%91%ED%99%94%EB%AC%B8%EC%9D%B4%EC%88%9C%EC%8B%A0%EB%8F%99%EC%83%81_MT(2).JPG"
        , "http://cfile28.uf.tistory.com/image/161C0E484D996432071D6C"
        , "http://cfile215.uf.daum.net/image/2278674F539FAD9C24F377");

        imgCountIntent = new Intent(getApplicationContext(), ImageViewer.class);

        // 사진을 클릭했을때 인텐트와 함께 액티비티에 전달
        morepic4Viewer1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imgCountIntent.putExtra("imgCountIntent", imgCount); // 로드해야할 이미지 개수
                imgCountIntent.putExtra("imgIndex", 1); // 로드 요청한 이미지 인덱스
                startActivity(imgCountIntent);
            }
        });

        morepic4Viewer2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imgCountIntent.putExtra("imgCountIntent", imgCount); // 로드해야할 이미지 개수
                imgCountIntent.putExtra("imgIndex", 1); // 로드 요청한 이미지 인덱스
                startActivity(imgCountIntent);
            }
        });

        morepic4Viewer3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imgCountIntent.putExtra("imgCountIntent", imgCount); // 로드해야할 이미지 개수
                imgCountIntent.putExtra("imgIndex", 1); // 로드 요청한 이미지 인덱스
                startActivity(imgCountIntent);
            }
        });

        otherimg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imgCountIntent.putExtra("imgCountIntent", imgCount); // 로드해야할 이미지 개수
                imgCountIntent.putExtra("imgIndex", 1); // 로드 요청한 이미지 인덱스
                startActivity(imgCountIntent);
            }
        });
    }

    // 이미지 웹 상에서 불러오기 위한 AsyncTask 쓰레드 클래스
    private class ImageLoadingTask extends AsyncTask <String, Integer, Long>
    {
        //실제 스레드 작업을 작성하는 곳이며 execute에서 전달한 params 인수를 사용할 수 있다.
        @Override
        protected Long doInBackground(String... params)
        {
            try
            {
                int temp;
                if(imgCount >= 4) // 이미지가 4개 이상이면 그냥 4개만 이미지 표현해주면 됨!
                {
                    temp = 4;
                }
                else
                {
                    temp = imgCount;
                }

                for(int i = 0; i < temp; i++)
                {
                    URL ImageUrl = new URL(params[i]);
                    HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();

                    bitmapImg[i] = BitmapFactory.decodeStream(is);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        //doInBackground 작업의 리턴값을 파라미터로 받으며 작업이 끝났음을 알리는 작업을 작성한다.
        @Override
        protected void onPostExecute(Long aLong)
        {
            morepic4Viewer1.setImageBitmap(bitmapImg[0]);
            morepic4Viewer2.setImageBitmap(bitmapImg[1]);
            morepic4Viewer3.setImageBitmap(bitmapImg[2]);
            morepic4Viewer4.setImageBitmap(bitmapImg[3]);
        }

        //doInBackground 시작 전에 호출되어 UI 스레드에서 실행된다. 주로 로딩바나 Progress 같은 동작 중임을 알리는 작업을 작성한다.
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //publishProgress()를 통해 호출되며 UI 스레드에서 실행된다. 파일 내려받는다고 치면 그때 퍼센티지 표시 작업 같은 걸 작성한다.
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
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
}
