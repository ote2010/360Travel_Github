package com.example.user.travel360.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.travel360.Navigationdrawer.UserActivity;
import com.example.user.travel360.R;
import com.example.user.travel360.Review.ReviewMainReadActivity;
import com.example.user.travel360.Story.StoryReadActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Main_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    View v;
    String string_story_text1, string_story_text2, string_traveler_text1, string_traveler_text2, string_place_text1, string_place_text2;

    TextView Reco_story_text1, Reco_story_text2, Reco_traveler_text1, Reco_traveler_text2, Reco_place_text1, Reco_place_text2;
    ImageView Reco_traveler_img1, Reco_traveler_img2;
    //Button Reco_story_btn1, Reco_story_btn2, Reco_place_btn1, Reco_place_btn2;
    FrameLayout Reco_story_btn1, Reco_story_btn2;
    LinearLayout Reco_place_btn1, Reco_place_btn2;

    //오늘의 추천 여행기 본문 서버에서 받아서 저장할 배열
    String[] Today_Reco_Story_Text = new String[2];
    //추천여행가 본문 & 유저정보 받아서 저장할 배열
    String[] Reco_Traveler_Text = new String[2];
    String[] Reco_Traveler_UserName = new String[2];

    //추천 여행기 본문 받아서 저장할 배열
    String[] Reco_Place_Text = new String[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main_fragment, container, false);

        init();
        putDate();
        onClickBtn1();

        return v;
    }

    public void init() {
        //textView 선언
        Reco_story_text1 = (TextView) v.findViewById(R.id.reco_storytext1);
        Reco_story_text2 = (TextView) v.findViewById(R.id.reco_storytext2);
        Reco_traveler_text1 = (TextView) v.findViewById(R.id.reco_travelertext1);
        Reco_traveler_text2 = (TextView) v.findViewById(R.id.reco_travelertext2);
        Reco_place_text1 = (TextView) v.findViewById(R.id.reco_placeText1);
        Reco_place_text2 = (TextView) v.findViewById(R.id.reco_placeText2);

        //button 선언
        Reco_story_btn1 = (FrameLayout) v.findViewById(R.id.reco_storybtn1);
        Reco_story_btn2 = (FrameLayout) v.findViewById(R.id.reco_storybtn2);
        Reco_place_btn1 = (LinearLayout) v.findViewById(R.id.reco_placebtn1);
        Reco_place_btn2 = (LinearLayout) v.findViewById(R.id.reco_placebtn2);

        //image 선언
        Reco_traveler_img1 = (ImageView) v.findViewById(R.id.reco_travelerImg1);
        Reco_traveler_img2 = (ImageView) v.findViewById(R.id.reco_travelerImg2);
    }

    public void onClickBtn1() {
    // 추천 리뷰 : 평점 순위 1, 2등 뽑아주기
    // 추천 여행기 기준 : 댓글 제일 많은거 10개 중에 2개만 랜덤으로 뽑아주기
        Reco_story_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StoryReadActivity.class);
                intent.putExtra("seq", 2);
                startActivity(intent);
            }
        });
        Reco_story_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StoryReadActivity.class);
                intent.putExtra("seq", 4);
                startActivity(intent);
            }
        });
        Reco_traveler_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserActivity.class);
                intent.putExtra("userSeq", 4);
                startActivity(intent);
            }
        });
        Reco_traveler_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserActivity.class);
                intent.putExtra("userSeq", 7);
                startActivity(intent);
            }
        });
        Reco_place_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReviewMainReadActivity.class);
                intent.putExtra("Location", "연세대");
                intent.putExtra("Evaluation", (float)4.5);
                startActivity(intent);
            }
        });
        Reco_place_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReviewMainReadActivity.class);
                intent.putExtra("Location", "숭실대");
                intent.putExtra("Evaluation", (float)4.49);
                startActivity(intent);
            }
        });
    }

    /*
    * readText(), readImg() 는
    * 서버로부터 "오늘의 추천 여행기", "추천 여행가","추천 여행기"를 얻어와서 뿌리는 함수!
    */

    public void readText() {
    }

    public void readImg() {
    }

  public void putDate()
  {
      //추천 여행가는 아직
      // TODO >> 추천 여행가 서버에서 받아와서 넣어줘야함
      getTravelRecordAll_Server();
      Reco_story_text1.setText(Today_Reco_Story_Text[0]);
      Reco_story_text2.setText(Today_Reco_Story_Text[1]);

      getTravelReviewAll_Server();
      Reco_place_text1.setText(Reco_Place_Text[0]);
      Reco_place_text2.setText(Reco_Place_Text[1]);
  }

    void getTravelRecordAll_Server()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "getTravleRecordAll_Server()");
        client.get("http://kibox327.cafe24.com/getTravelRecordList.do", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {         }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("travels") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for(int i=0; i<2; i++ ) {

                        JSONObject obj = (JSONObject)arr.get(i);

                        int seq  = (Integer)obj.get("seq");
                        int user_info_seq = (Integer)obj.get("user_info_seq");
                        String presentation_image = (String)obj.get("presentation_image");

                         Today_Reco_Story_Text[i] = (String)obj.get("text");

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

    void getTravelReviewAll_Server()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "getTravleReviewAll_Server()");
        client.get("http://kibox327.cafe24.com/travelReviewList.do", new AsyncHttpResponseHandler() {
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
                    for(int i=0; i<arr.length(); i++ ) {
                        /*
                        JSONObject obj = (JSONObject)arr.get(i);

                        int seq  = (Integer)obj.get("seq");
                        int user_info_seq = (Integer)obj.get("user_info_seq");
                        String presentation_image = (String)obj.get("presentation_image");
                        String text = (String)obj.get("text");
                        */
                        JSONObject obj = (JSONObject)arr.get(i);
                        Reco_Place_Text[i] = (String)obj.get("text");
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