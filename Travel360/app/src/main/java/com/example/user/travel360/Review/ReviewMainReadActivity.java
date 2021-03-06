package com.example.user.travel360.Review;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.Navigationdrawer.ApplicationController;
import com.example.user.travel360.Navigationdrawer.LoginActivity;
import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;


/**
 * Created by user on 2016-08-10.
 */
public class ReviewMainReadActivity extends AppCompatActivity {

    ImageView Place_Img;
    ScrollView mScroll;
    TextView place_name;
    TextView star_num1;
    TextView heart_num;

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

    //서버에서 받아와 저장할 배열
    String[] Best_text1 = new String[10];
    String[] Best_Date = new String[10];
    String[] Best_StarNum = new String[10];
    String[] Best_Ster = new String[10];
    int[] seq = new int[100];
    int[] user_info_seq = new int[100];
    int user_info_seq2 = 0;
    String as;

    int Count_Review = 0;
    // 일반 여행자 리뷰
    ImageView[] NUser_profile = new ImageView[10]; // 사용자 프로필 사진
    TextView[] NUser_name = new TextView[10]; // 사용자 이름
    TextView[] NText1 = new TextView[10]; //리뷰 본문
    TextView[] NDate = new TextView[10]; //리뷰 날짜
    TextView[] NStarNum = new TextView[10]; //별점 점수
    ImageView[] NStar = new ImageView[10]; //별점 이미지
    String Location;
    String Evaluation;
    String Start;
    //플로팅 버튼
    ImageButton WriteReview, Up;
    String[] Text_bump = new String[1000];
    int arr_len = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.back_button));
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar1));

        setContentView(R.layout.activity_review_main_read);
        Intent intent = getIntent();
        Location = intent.getStringExtra("Location");

        String img = Location + ".jpg";
        getImage_Server(img);
        Evaluation = intent.getStringExtra("Evaluation");
        Start = intent.getStringExtra("Start");
        Log.d("SUN_h", Location + "location" + "/" + Evaluation + "/" + Start);
        init();
        getTravelReviewAll_Server();
        //   getTravleReviewAll_Server();
        // Bestinit();
        //Normalinit();
        onClickEvent();

    }

    @Override
    protected void onResume() {
        // Bestinit();
        super.onResume();
    }

    public void init() {


        place_name = (TextView) findViewById(R.id.place_name);
        place_name.setText(Location);
        Place_Img = (ImageView) findViewById(R.id.place_img);

        star_num1 = (TextView) findViewById(R.id.star_num);
        star_num1.setText(Evaluation);

        heart_num = (TextView) findViewById(R.id.heart_num);

//        star_num1 = (TextView) findViewById(R.id.star_num);
//        if (Location.equals("연세대")) {
//            star_num1.setText("4.1");
//            Place_Img.setImageDrawable(getResources().getDrawable(R.drawable.yonsei));
//        } else if (Location.equals("숭실대")) {
//            star_num1.setText("3.9");
//            Place_Img.setImageDrawable(getResources().getDrawable(R.drawable.soongsil));
//        } else if (Location.equals("Paris")) {
//            star_num1.setText("3.4");
//            Place_Img.setImageDrawable(getResources().getDrawable(R.drawable.paris3));
//        } else if (Location.equals("서울")) {
//            star_num1.setText("3.3");
//            Place_Img.setImageDrawable(getResources().getDrawable(R.drawable.seoul3));
//        } else {
//            star_num1.setText("2.8");
//            Place_Img.setImageDrawable(getResources().getDrawable(R.drawable.london3));
//        }


        BestReview = (LinearLayout) findViewById(R.id.best_review);
        NormalReview = (LinearLayout) findViewById(R.id.normal_review);
        WriteReview = (ImageButton) findViewById(R.id.write_review_btn);
        //   WriteReview.setImageDrawable(getResources().getDrawable(R.drawable.));
        Up = (ImageButton) findViewById(R.id.up);
        Up.setImageDrawable(getResources().getDrawable(R.drawable.top_button));
        mScroll = (ScrollView) findViewById(R.id.read_review_scroll);


    }

    public void onClickEvent() {
        WriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "글쓰기", Toast.LENGTH_SHORT).show();
                String LoginFlag = ApplicationController.getInstance().getEmail();

                boolean check = (LoginFlag + "").equals(null + "");

                if (check) {
                    AlertDialog.Builder dialogB = new AlertDialog.Builder(ReviewMainReadActivity.this);
                    dialogB.setMessage("로그인 하시겠습니까?").setCancelable(false).setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ReviewMainReadActivity.this, LoginActivity.class);
                                    intent.putExtra("Location", Location);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = dialogB.create();

                    alert.show();
                } else {
                    ApplicationController.getInstance().setLocation(Location);
                    ReviewWriteActivity reviewWriteActivity = new ReviewWriteActivity(ReviewMainReadActivity.this);
                    reviewWriteActivity.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
                    reviewWriteActivity.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            BestReview.removeAllViews();
                            NormalReview.removeAllViews();
                            getTravelReviewAll_Server();

                        }
                    });
                    reviewWriteActivity.show();

                }


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Bestinit();
        super.onActivityResult(requestCode, resultCode, data);

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

    void getTravelReviewAll_Server() {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("location", Location);
        Log.d("SUN", "getTravleReviewAll_Server()");
        client.get("http://kibox327.cafe24.com//travelReviewList.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //  LayoutInflater inflater = LayoutInflater.from(getApplication().getApplicationContext());
                Log.d("SUN_jj", "statusCode : " + statusCode + " , response : " + new String(response));

                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("reviews") + "";
                    JSONArray arr = new JSONArray(objStr);
                    // Log.d("SUN_a", arr.length()+"길이");
                    LayoutInflater inflater = LayoutInflater.from(getApplication().getApplicationContext());
                    for (int i = 0; i < arr.length(); i++) {


                        arr_len = arr.length();
                        Log.d("SUN_a", "efwefwfefwfef");
                        JSONObject obj = (JSONObject) arr.get(i);

                        seq[i] = (Integer) obj.get("seq");
                        user_info_seq[i] = (Integer) obj.get("user_info_seq");
                        user_info_seq2 = (Integer) obj.get("user_info_seq");
                        // evaluation
                        long write_date_client = (long) obj.get("write_date_client");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                        final String write_date = df.format(write_date_client);
                        final String text = (String) obj.get("text");
                        Text_bump[i] = text;
                        //----------------------------------------------------------------------------------------------------------------------------
                        BestReviewITem[i] = inflater.inflate(R.layout.review_read_listitem_view, v, false); // 추가할 순위권 여행지 뷰 inflate

                        // 메달 아이콘, 여행지 장소 텍스트, 여행지 별점 ID 불러오기
                        BUser_profile[i] = (ImageView) BestReviewITem[i].findViewById(R.id.Review_read_user_img);
                        BStar[i] = (ImageView) BestReviewITem[i].findViewById(R.id.Review_read_star);
                        BUser_name[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_user_name);
                        BText1[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_text);
                        BDate[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_date);
                        BStarNum[i] = (TextView) BestReviewITem[i].findViewById(R.id.Review_read_starnum);


                        //***********이 부분에서 서버에서 받아와서 바꿔주면 된다!!!!!***********
                        BUser_profile[i].setImageResource(R.drawable.empty_profile);
                        BStar[i].setImageResource(R.drawable.star);
                        //    BUser_name[i].setText("오탁은");
                        // BStarNum[i].setText("4.8");
                        getUserInfo_Server(user_info_seq[i], i);
                        Log.d("CHECK_SEQ", "리뷰메인 SEQ : " + user_info_seq[i]);

                        BText1[i].setText(Text_bump[i]);
                        Log.d("SUN_1", Text_bump[i] + "Location : " + Location);
                        BDate[i].setText("2016.08.23");
                        heart_num.setText("총 리뷰 :" + (i + 1));
                        BestReviewITem[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //int user_info_seq, String write_date, String text
                                ReviewReadDialog reviewReadDialog = new ReviewReadDialog(ReviewMainReadActivity.this, user_info_seq2, write_date, text, 3.5); // 사용자 seq, 작성날짜, 작성내용, 별점
                                reviewReadDialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);


                                reviewReadDialog.show();
                            }
                        });
                        //*******************************************************************
                        if (i < 3) {
                            BestReview.addView(BestReviewITem[i]); // 추가해주기
                        } else {
                            NormalReview.addView(BestReviewITem[i]);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN", "e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    void getUserInfo_Server(int seq, final int i) {


        RequestParams params = new RequestParams();
        // 보내는 data는 seq 만 있으면 됩니다.
        params.put("seq", seq);    //바꿔주기

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getUserInfo_Server()");
        client.get("http://kibox327.cafe24.com/getUserInfo.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("userDto") + "";
                    JSONObject obj = new JSONObject(objStr);

                    String id = (String) obj.get("id");
                    String name = (String) obj.get("name");
                    String profile_image = (String) obj.get("profile_image");
                    Log.d("SUN", "profile_image : " + profile_image);
//받아온걸로 입히기
                    BUser_name[i].setText(name);
                    //   BUser_profile[i].setImageResource(R.drawable.profile_sample);

                  /*  review_dateTV.setText(wrteDate);
                    user_nameTV.setText(name);
                    review_evTV .setText(writeEvl+"");
                    review_contextTV .setText(writeText);
*/

                } catch (JSONException e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }


    void getImage_Server(String imageName) {

        RequestParams params = new RequestParams();
        // 보내는 data는 imageName 만 있으면 됩니다.
        params.put("imageName", imageName);
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getImage_Server()");
        client.get("http://kibox327.cafe24.com/Image.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // byteArrayToBitmap 를 통해 reponse로 받은 이미지 데이터 bitmap으로 변환
                Log.d("Image", "SUN : " );
                Bitmap bitmap = byteArrayToBitmap(response);
                // userProfileImg.setImageBitmap(bitmap);
                Place_Img.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Image", "Fail : " );
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray) {  // byte -> bitmap 변환 및 반환
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }


}