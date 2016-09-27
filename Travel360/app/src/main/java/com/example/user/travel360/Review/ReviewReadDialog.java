package com.example.user.travel360.Review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.CustomList.CustomAdapter;
import com.example.user.travel360.CustomList.ItemData;
import com.example.user.travel360.Navigationdrawer.ApplicationController;
import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReviewReadDialog extends Dialog implements View.OnClickListener {
    //댓글
    AlertDialog.Builder aDialog;
    AlertDialog ad;
    public ListView listView;
    public CustomAdapter adapter;
    public ArrayList<ItemData> itemDatas = new ArrayList<ItemData>();

    Context mContext;
    Button Detail, Add_Traveler, Close, Comment;
    TextView Name, Date, Review_Text, Star_Num;
    ImageView Star;
    int userSeq = -1;
    String a = "작년에 파리 갔다 왔습니다. 사실 여권도 없는데 뻥친거에요ㅠㅠ 그냥 리뷰 예시 쓰려구 이러구 있습니다ㅠㅠ 무슨 내용으로 리뷰를 적을까. 360Studio 파이팅!! 임베디드 소프트웨어 경진대회 대상이 목표입니다!!!! 전성일은 파호우 쿰척쿰척!!!!! 동영이는 카톡 답장좀해줘 제발!!!!!!";

    public ReviewReadDialog(Context context) {
        super(context);
       this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_read_dialog);
        userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());

        init();
        //   Review_Text.setText(a);
        Close.setOnClickListener(this);
        Add_Traveler.setOnClickListener(this);
        Comment.setOnClickListener(this);
        getTravelReview_Server();
    }

    public void init() {
        Detail = (Button) findViewById(R.id.dialog_datail_btn);
        Add_Traveler = (Button) findViewById(R.id.dialog_ad_traveler);
        Close = (Button) findViewById(R.id.dialog_close_btn);
        Comment = (Button) findViewById(R.id.ReviewComment);

        Name = (TextView) findViewById(R.id.dialog_user_name);
        Date = (TextView) findViewById(R.id.dialog_date);
        Star_Num = (TextView) findViewById(R.id.dialog_star_num);
        Review_Text = (TextView) findViewById(R.id.dialog_textview);

        Star = (ImageView) findViewById(R.id.dialog_star);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_ad_traveler:
                Toast.makeText(getContext(), "add travler", Toast.LENGTH_SHORT).show();
                addFriend_Server(1, 3);
                break;

            case R.id.dialog_close_btn:
                dismiss();
                break;
            case R.id.ReviewComment:
                Toast.makeText(getContext(), "wefwefwefwef", Toast.LENGTH_SHORT).show();
                getComment_Server();
                break;
        }
    }


    /**************
     * 친구 (요청)추가 하기
     ***********************/
    void addFriend_Server(int mySeq, int otherSeq) {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq, targetSeq 만 있으면 됩니다.
        params.put("seq", mySeq);
        params.put("targetSeq", otherSeq);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "addFriend_Server()");
        client.get("http://kibox327.cafe24.com/addFriend.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);

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

    void getTravelReview_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 reviewSeq 만 있으면 됩니다.
        params.put("reviewSeq", 1);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getTravleReview_Server()");
        client.get("http://kibox327.cafe24.com/getTravelReview.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                Log.d("ReviewDialo@", res);

                try {
                    JSONObject obj = new JSONObject(res);
                    String objStr = obj.get("review") + "";
                    JSONObject review = new JSONObject(objStr);
                    String location = (String) review.get("location");
                    String text = (String) review.get("text");
                    // String user = (String)review.get("user");
                    // long write_date_client = (long)review.get("write_date_client");
                    //  Name.setText((String)review.get("user"));
                    //     Date.setText((String)review.get("write_date"));   무슨형인지 몰라서 오류남
                    Review_Text.setText(text);


                    Log.d("SUN", "location : " + location);
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

    void writeStoryComment_Server(String comment) {
        //   long todaydate = System.currentTimeMillis(); // long 형의 현재시간

        RequestParams params = new RequestParams();
        params.put("comment", "travle comment");
        params.put("evaluation", "1");
        params.put("travel_record_seq", "1");
        params.put("id", "a");
        //  params.put("write_date",todaydate);

        params.put("UserSeq", "1");
        params.put("travelSeq", "1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStoryComment_Server()");
        client.get("http://kibox327.cafe24.com/writeComment.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);

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

    void getComment_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 userSeq 만 있으면 됩니다.
        params.put("travelSeq", "1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getComment_Server()");
        client.get("http://kibox327.cafe24.com/getTravelCommentList.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflate.inflate(R.layout.comment_dialog, null);

                aDialog = new AlertDialog.Builder(getContext());
                aDialog.setView(layout);

                ad = aDialog.create();
                final EditText commentEt = (EditText) layout.findViewById(R.id.commentText_dialog);
                layout.findViewById(R.id.commentSentBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        writeStoryComment_Server(commentEt.getText().toString());
                        commentEt.setText("");

                    }
                });
                listView = (ListView) layout.findViewById(R.id.commnetList);
                adapter = new CustomAdapter(itemDatas, getContext());
                adapter.clear();
                listView.setAdapter(adapter);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("comment") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = (JSONObject) arr.get(i);
                        String comment = (String) obj.get("comment");
                        String com = new String(comment.getBytes("euc-kr"),"utf-8"); //8859_1

                        int evaluation = (int) obj.get("evaluation");
                        String id = (String) obj.get("id");
                        int seq = (int) obj.get("seq");
                        int user_info_seq = (int) obj.get("user_info_seq");
                        JSONObject write_date = (JSONObject) obj.get("write_date");
                        long time = (long) write_date.get("time");

                        java.util.Date date = new Date(time);
                        adapter.addListItem(id, comment);
                        listView.setSelection(i);
                        Log.d("SUN", "comment : " + comment + " , id : " + id + " , user_info_seq : " + user_info_seq + " , date : " + date + " , seq : " + seq);

                    }
                    ad.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN", "e : " + e.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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

}