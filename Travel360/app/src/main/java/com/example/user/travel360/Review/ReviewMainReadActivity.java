package com.example.user.travel360.Review;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.R;
import com.example.user.travel360.ReviewReadDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by user on 2016-08-10.
 */
public class ReviewMainReadActivity extends Activity {


    ScrollView mScroll;

    ViewGroup v;
    LinearLayout BestReview, NormalReview;
    int BEST_REVIEW_NUM = 3;
    int NORMAL_REVIEW_NUM = 3;
    View[] BestReviewITem = new View[10];
    View[] NormalReviewITem = new View[10];
    String text1 = "작년에 파리 갔다왔습니다. 사실 여권도 없는데 뻥친거에요ㅠㅠ 그냥 리뷰 예시 쓰려고 지금…";
    // 영향력 있는 여행자 리뷰!!!
    ImageView[] BUser_profile = new ImageView[10]; // 사용자 프로필 사진
    TextView[] BUser_name = new TextView[10]; // 사용자 이름
    TextView[] BText1 = new TextView[10]; //리뷰 본문
    TextView[] BDate = new TextView[10]; //리뷰 날짜
    TextView[] BStarNum = new TextView[10]; //별점 점수
    ImageView[] BStar = new ImageView[10]; //별점 이미지

    // 일반 여행자 리뷰
    ImageView[] NUser_profile = new ImageView[10]; // 사용자 프로필 사진
    TextView[] NUser_name = new TextView[10]; // 사용자 이름
    TextView[] NText1 = new TextView[10]; //리뷰 본문
    TextView[] NDate = new TextView[10]; //리뷰 날짜
    TextView[] NStarNum = new TextView[10]; //별점 점수
    ImageView[] NStar = new ImageView[10]; //별점 이미지

    //플로팅 버튼
    FloatingActionButton WriteReview, Up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_review_main_read);

        init();
        getTravleReviewAll_Server();
       // Bestinit();
        Normalinit();
        onClickEvent();

    }

    public void init() {
        BestReview = (LinearLayout) findViewById(R.id.best_review);
        NormalReview = (LinearLayout) findViewById(R.id.normal_review);
        WriteReview = (FloatingActionButton) findViewById(R.id.write_review_btn);
        Up = (FloatingActionButton) findViewById(R.id.up);
        mScroll = (ScrollView) findViewById(R.id.read_review_scroll);


    }

    public void onClickEvent() {
        WriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "글쓰기", Toast.LENGTH_SHORT).show();
                ReviewWriteActivity reviewWriteActivity = new ReviewWriteActivity(ReviewMainReadActivity.this);

               reviewWriteActivity.show();
            }
        });
        Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplication(), "we", Toast.LENGTH_SHORT).show();
                scrollToEnd();

            }
        });

    }

    public void Bestinit() {

        LayoutInflater inflater = LayoutInflater.from(getApplication().getApplicationContext());
        for (int i = 0; i < BEST_REVIEW_NUM; i++) {
            BestReviewITem[i] = inflater.inflate(R.layout.review_read_listitem_view, v, false); // 추가할 순위권 여행지 뷰 inflate

            // 메달 아이콘, 여행지 장소 텍스트, 여행지 별점 ID 불러오기
            BUser_profile[i] = (ImageView) BestReviewITem[i].findViewById(R.id.Review_read_user_img);
            BStar[i] = (ImageView) BestReviewITem[i].findViewById(R.id.Review_read_star);
            BUser_name[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_user_name);
            BText1[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_text);
            BDate[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_date);
            BStarNum[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_starnum);


            //***********이 부분에서 서버에서 받아와서 바꿔주면 된다!!!!!***********
            BUser_profile[i].setImageResource(R.drawable.img1);
            BStar[i].setImageResource(R.drawable.star);
            BUser_name[i].setText("오탁은");
            BStarNum[i].setText("4.8");

            BText1[i].setText(text1);
            BDate[i].setText("2016.08.23");

            BestReviewITem[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReviewReadDialog reviewReadDialog = new ReviewReadDialog(ReviewMainReadActivity.this);
                    reviewReadDialog.show();
                }
            });
            //*******************************************************************

            BestReview.addView(BestReviewITem[i]); // 추가해주기
        }

    }

    public void Normalinit() {

        LayoutInflater inflater = LayoutInflater.from(getApplication().getApplicationContext());
        for (int i = 0; i < NORMAL_REVIEW_NUM; i++) {
            NormalReviewITem[i] = inflater.inflate(R.layout.review_read_listitem_view, v, false); // 추가할 순위권 여행지 뷰 inflate

            // 메달 아이콘, 여행지 장소 텍스트, 여행지 별점 ID 불러오기
            NUser_profile[i] = (ImageView) NormalReviewITem[i].findViewById(R.id.Review_read_user_img);
            NStar[i] = (ImageView) NormalReviewITem[i].findViewById(R.id.Review_read_star);
            NUser_name[i] = (TextView) NormalReviewITem[i].findViewById(R.id.Review_read_user_name);
            NText1[i] = (TextView) NormalReviewITem[i].findViewById(R.id.Review_read_text);
            NDate[i] = (TextView) NormalReviewITem[i].findViewById(R.id.Review_read_date);
            NStarNum[i] = (TextView) NormalReviewITem[i].findViewById(R.id.Review_read_starnum);


            //***********이 부분에서 서버에서 받아와서 바꿔주면 된다!!!!!***********
            NUser_profile[i].setImageResource(R.drawable.img1);
            NStar[i].setImageResource(R.drawable.star);
            NUser_name[i].setText("오탁은");
            NStarNum[i].setText("4.8");

            NText1[i].setText(text1);
            NDate[i].setText("2016.08.23");


            NormalReviewITem[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReviewReadDialog reviewReadDialog = new ReviewReadDialog(ReviewMainReadActivity.this);
                    reviewReadDialog.show();
                }
            });
            //*******************************************************************

            NormalReview.addView(NormalReviewITem[i]); // 추가해주기
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Toast.makeText(this, "홈 아이콘 이벤트", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.menuButton) {
            Toast.makeText(this, "메뉴 버튼 이벤트", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_main_read, menu);
        return true;
    }

    public void scrollToEnd() {
        Log.d("@@@", "in");
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                Log.d("@@@", "in2");
                mScroll.fullScroll(View.FOCUS_UP);
            }

        });

    }
    void getTravleReviewAll_Server() {

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "getTravleReviewAll_Server()");
        client.get("http://kibox327.cafe24.com//travelReviewList.do", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {         }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));

                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("reviews") + "";
                    JSONArray arr = new JSONArray(objStr);
                    LayoutInflater inflater = LayoutInflater.from(getApplication().getApplicationContext());
                    for(int i=0; i<arr.length(); i++ ) {


                        JSONObject obj = (JSONObject)arr.get(i);
/*
                        int seq  = (Integer)obj.get("seq");
                        int user_info_seq = (Integer)obj.get("user_info_seq");
                        String presentation_image = (String)obj.get("presentation_image");
                        String text = (String)obj.get("text");

                     */

                        BestReviewITem[i] = inflater.inflate(R.layout.review_read_listitem_view, v, false); // 추가할 순위권 여행지 뷰 inflate

                        // 메달 아이콘, 여행지 장소 텍스트, 여행지 별점 ID 불러오기
                        BUser_profile[i] = (ImageView) BestReviewITem[i].findViewById(R.id.Review_read_user_img);
                        BStar[i] = (ImageView) BestReviewITem[i].findViewById(R.id.Review_read_star);
                        BUser_name[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_user_name);
                        BText1[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_text);
                        BDate[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_date);
                        BStarNum[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_starnum);


                        //***********이 부분에서 서버에서 받아와서 바꿔주면 된다!!!!!***********
                        BUser_profile[i].setImageResource(R.drawable.img1);
                        BStar[i].setImageResource(R.drawable.star);
                        BUser_name[i].setText("오탁은");
                        BStarNum[i].setText("4.8");

                        Log.d("Review@@@",(String)obj.get("text"));
                        BText1[i].setText((String)obj.get("text"));
                        BDate[i].setText("2016.08.23");

                        BestReviewITem[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ReviewReadDialog reviewReadDialog = new ReviewReadDialog(ReviewMainReadActivity.this);
                                reviewReadDialog.show();
                            }
                        });
                        //*******************************************************************

                        BestReview.addView(BestReviewITem[i]); // 추가해주기
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN",  "e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {          }
        });
    }

}
