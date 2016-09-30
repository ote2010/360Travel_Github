package com.example.user.travel360.Fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.travel360.Navigationdrawer.ApplicationController;
import com.example.user.travel360.R;
import com.example.user.travel360.Review.ReviewMainReadActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;


public class Review_fragment extends Fragment {
    ViewGroup v;
    int i=0;
    LinearLayout mainReviewContainer; // 추가할 리뷰 프레그먼트 레이아웃
    ImageView[] reviewMainImage = new ImageView[10]; // 리뷰 순위권 메인 이미지 뷰 - 버튼 동작 설정해야함
    Button[] reviewImageButton = new Button[10]; // 리뷰의 간략화 정보 버튼 - 버튼 동작 설정해야함
    ImageView[] medalIcon = new ImageView[10]; // 메달 아이콘 이미지뷰
    TextView[] rankPlaceTextView = new TextView[1000]; // 순위권 여행지 리뷰의 장소 텍스트뷰
    TextView[] rankEvaluationTextView = new TextView[1000]; // 순위권 장소의 별점 텍스트뷰
    View[] rankItem = new View[10]; // 순위권 여행지 리뷰의 뷰

    ListView listView; // 순위권 밖의 리스트 뷰
    //ReviewAdapter adapter; // 리스트 뷰를 위한 어댑터

    //****순위권 밖 리스트 아이템을 위한 문자열 배열. 서버에서 받아온다고 가정******
    String[] location = new String[1000];
    int[] userseq = new int[1000];
    long[] start_date = new long[1000];
    // Float [] evaluation = new Float[10];
    float[] evaluation = new float[1000];
    String start[] = new String[1000];

    //  String [] ages = {"2.6", "1.9", "1.8", "1.7"};
    //*************************************************************************
//    ReviewListItemView view = new ReviewListItemView(getActivity().getApplicationContext());
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = (ViewGroup) inflater.inflate(R.layout.fragment_review_fragment, container, false);
        mainReviewContainer = (LinearLayout) v.findViewById(R.id.mainReviewContainer);
        //   showReviewRanking_Server();

        return v;
    }

    public void RankList() {
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

        for (  i = 0; i < 1000; i++) {
            if ((ApplicationController.getInstance().getLocation(i) + "").equals(null + "")) {
                Log.d("SUN_h", i + "번");
                return;
            } else {
                if (i < 3) {
                    Log.d("SUN_h", i + "번");
                    rankItem[i] = inflater.inflate(R.layout.review_main_rankitem, v, false); // 추가할 순위권 여행지 뷰 inflate

                    // 메달 아이콘, 여행지 장소 텍스트, 여행지 별점 ID 불러오기
                    medalIcon[i] = (ImageView) rankItem[i].findViewById(R.id.medalIcon);
                    rankPlaceTextView[i] = (TextView) rankItem[i].findViewById(R.id.rankPlaceTextview);
                    rankEvaluationTextView[i] = (TextView) rankItem[i].findViewById(R.id.rankEvaluationTextview);
                    reviewMainImage[i] = (ImageView) rankItem[i].findViewById(R.id.reviewMainImage);
                  //  reviewImageButton[i] = (Button) rankItem[i].findViewById(R.id.reviewImageButton);

                    // i가 0이면 1등, 1이면 2등, 2이면 3등
                    if (i == 0) {
                        medalIcon[i].setImageResource(R.drawable.goldmedal);
                    } else if (i == 1) {
                        medalIcon[i].setImageResource(R.drawable.silvermedal);
                    } else if (i == 2) {
                        medalIcon[i].setImageResource(R.drawable.blonzemedal);
                    }

                    //***********이 부분에서 서버에서 받아와서 바꿔주면 된다!!!!!***********
                    reviewMainImage[i].setImageResource(R.drawable.testimg1);
                    Log.d("SUN_h", "00000000000000");
                    final String Intent_Start = ApplicationController.getInstance().getStartDate(i);
                    final String Intent_location  = ApplicationController.getInstance().getLocation(i);
                    final float Intent_evaluation = ApplicationController.getInstance().getEvalucation(i);
                    rankPlaceTextView[i].setText(ApplicationController.getInstance().getLocation(i) + "");
                    rankEvaluationTextView[i].setText(ApplicationController.getInstance().getEvalucation(i) + "");

                    reviewMainImage[i].setOnClickListener(new ImageView.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // ***이 부분에서 서버에게 정보를 요청하는 코드가 필요함***
                            Intent intent = new Intent(getActivity(), ReviewMainReadActivity.class);
                            intent.putExtra("Location",Intent_location);
                            ApplicationController.getInstance().setLocation(Intent_location);
                            intent.putExtra("Evaluation", Intent_evaluation);
                            intent.putExtra("Start",Intent_Start);
                            startActivity(intent);
                        }
                    });
                    //*******************************************************************

                    mainReviewContainer.addView(rankItem[i]); // 추가해주기
                } else {
                    Log.d("SUN_h", i + "번");
                    rankItem[i] = inflater.inflate(R.layout.review_main_listviewitem, v, false);

                    rankPlaceTextView[i] = (TextView) rankItem[i].findViewById(R.id.PlaceTextView);
                    rankEvaluationTextView[i] = (TextView) rankItem[i].findViewById(R.id.EvaluationTextView);
                    final String Intent_Start = ApplicationController.getInstance().getStartDate(i);
                    final String Intent_location  = ApplicationController.getInstance().getLocation(i);
                    final float Intent_evaluation = ApplicationController.getInstance().getEvalucation(i);
                    rankPlaceTextView[i].setText(ApplicationController.getInstance().getLocation(i) + "");
                    rankEvaluationTextView[i].setText(ApplicationController.getInstance().getEvalucation(i) + "");
                    //  final int finalI = i;
                    rankItem[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ReviewMainReadActivity.class);

                            intent.putExtra("Location",Intent_location);
                            intent.putExtra("Evaluation", Intent_evaluation);
                            intent.putExtra("Start",Intent_Start);
                            // ApplicationController.getInstance().getTravelReviewAll_Server(Intent_location);
                            startActivity(intent);
                        }
                    });
                    mainReviewContainer.addView(rankItem[i]);

                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        RankList();
        for (int i = 0; i < 10; i++) {
            Log.d("SUN_h", location[i] + "s");
        }
        super.onActivityCreated(savedInstanceState);
    }

//    // 리스트 뷰에 적용되는 어댑터 클래스. 이부분에서 리스트 아이템을 적용해준다.
//    class ReviewAdapter extends BaseAdapter {
//        @Override
//        public int getCount() // 리스트뷰 아이템 개수 가져오기
//        {
//            return 4;
//        }
//
//        @Override
//        public Object getItem(int position) // 아이템 가져오기
//        {
//            return location[position];
//        }
//
//        @Override
//        public long getItemId(int position) // 아이템 ID 가져오기
//        {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) // 뷰 가져오기
//        {
//            ReviewListItemView view = new ReviewListItemView(getActivity().getApplicationContext());
//            view.setPlace(location[position]);
//            Log.d("SUN_h", location[position] + "view");
//            view.setEvaluation(evaluation[position] + "");
//            return view;
//        }
//
//
//    }

    /************************
     * 리뷰 랭킹
     *********************/
    void showReviewRanking_Server() {

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStory_Server()");
        client.get("http://kibox327.cafe24.com/travelReviewRankingList.do", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN_h", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);

                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("reviews") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = (JSONObject) arr.get(i);
                        //     Float a   = (float)obj.get("evaluation");
                        //   evaluation[i] = a;
                        location[i] = (String) obj.get("location");
                        String b = (String) obj.get("location");
                        location[i] = b;
                        // String text  = (String)obj.get("text");
                        int c = (int) obj.get("seq");
                        userseq[i] = c;
                        long d = (long) obj.get("write_date_client");
                        //start_date[i]=d;
                        // long finish_date = (long)obj.get("finish_date_client");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                        start[i] = df.format(d);
                        // String finish = df.format(finish_date);
                        Log.d("SUN_h", "evaluation : " + evaluation[i] + " " + " , location : " + location[i] + " , text : " + " , userseq : " + userseq[i] + " , start : " + start[i]);
                        if (i < 3) {
//                           rankPlaceTextView[i].setText(location[i] + "");
                            //                         rankEvaluationTextView[i].setText(evaluation[i] + "");
                        } else {
                            //  view.setPlace(location[i]);
                            //   view.setEvaluation(evaluation[i]+"");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN_h", "e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN_확인", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
        //  Log.d("SUN_h", "lenth" + location.length);
    }
}
