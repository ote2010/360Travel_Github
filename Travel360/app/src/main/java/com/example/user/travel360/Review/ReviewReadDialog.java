package com.example.user.travel360;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ReviewReadDialog extends Dialog implements View.OnClickListener{

    Button Detail, Add_Traveler, Close;
    TextView Name, Date, Review_Text, Star_Num;
    ImageView Star;
    String a = "작년에 파리 갔다 왔습니다. 사실 여권도 없는데 뻥친거에요ㅠㅠ 그냥 리뷰 예시 쓰려구 이러구 있습니다ㅠㅠ 무슨 내용으로 리뷰를 적을까. 360Studio 파이팅!! 임베디드 소프트웨어 경진대회 대상이 목표입니다!!!! 전성일은 파호우 쿰척쿰척!!!!! 동영이는 카톡 답장좀해줘 제발!!!!!!";

    public ReviewReadDialog(Context context) {
        super(context);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_read_dialog);

        init();
     //   Review_Text.setText(a);
        Close.setOnClickListener(this);
        Add_Traveler.setOnClickListener(this);
        getTravleReview_Server();
    }

    public void init() {
        Detail = (Button) findViewById(R.id.dialog_datail_btn);
        Add_Traveler = (Button) findViewById(R.id.dialog_ad_traveler);
        Close = (Button) findViewById(R.id.dialog_close_btn);

        Name = (TextView) findViewById(R.id.dialog_user_name);
        Date = (TextView) findViewById(R.id.dialog_date);
        Star_Num = (TextView) findViewById(R.id.dialog_star_num);
        Review_Text = (TextView) findViewById(R.id.dialog_textview);

        Star = (ImageView) findViewById(R.id.dialog_star);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.dialog_ad_traveler :
                Toast.makeText(getContext(),"add travler",Toast.LENGTH_SHORT).show();
                addFriend_Server(1,3);
                break;

            case R.id.dialog_close_btn:
                dismiss();
                break;
        }
    }


    /************** 친구 (요청)추가 하기  ***********************/
    void addFriend_Server(int mySeq, int otherSeq) {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq, targetSeq 만 있으면 됩니다.
        params.put("seq",mySeq);
        params.put("targetSeq",otherSeq);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "addFriend_Server()");
        client.get("http://kibox327.cafe24.com/addFriend.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {  }
        });
    }

    void getTravleReview_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 reviewSeq 만 있으면 됩니다.
        params.put("reviewSeq", 1);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getTravleReview_Server()");
        client.get("http://kibox327.cafe24.com/getTravelReview.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {   }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                Log.d("ReviewDialo@", res);

                try {
                    JSONObject obj = new JSONObject(res);
                    String objStr =  obj.get("review") + "";
                    JSONObject review = new JSONObject(objStr);
                    String location = (String)review.get("location");
                    String text = (String)review.get("text");
                    // String user = (String)review.get("user");
                    // long write_date_client = (long)review.get("write_date_client");
             //  Name.setText((String)review.get("user"));
             //     Date.setText((String)review.get("write_date"));   무슨형인지 몰라서 오류남
                    Review_Text.setText(text);


                    Log.d("SUN",  "location : " +location);
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
            public void onRetry(int retryNo) {         }
        });
    }


}