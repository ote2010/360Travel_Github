package com.example.user.travel360.Navigationdrawer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.CustomList.CustomAdapter;
import com.example.user.travel360.CustomList.ItemData;
import com.example.user.travel360.CustomList.MyWriteCustomAdapter;
import com.example.user.travel360.CustomList.MyWriteItemData;
import com.example.user.travel360.R;
import com.example.user.travel360.Story.StoryReadActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
   ImageButton addTravlerBtn, userHeartBtn;
    Button show_all, show_story, show_review;

    public ListView listView_all, listView_story, listView_review;
    public MyWriteCustomAdapter adapter_all, adapter_story, adapter_review;
    public ArrayList<MyWriteItemData> itemDatas_all = new ArrayList<MyWriteItemData>();
    public ArrayList<MyWriteItemData> itemDatas_story = new ArrayList<MyWriteItemData>();
    public ArrayList<MyWriteItemData> itemDatas_review = new ArrayList<MyWriteItemData>();

    boolean heart_flag = false;
    int mySeq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light_NoActionBar_TranslucentDecor);
        setContentView(R.layout.activity_user);
        mySeq = Integer.valueOf(ApplicationController.getInstance().getSeq());
        widgetInit();
        getTravleRecordAll_Server();
        getTravleReviewAll_Server();

        listView_story.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyWriteItemData  itemData_temp = (MyWriteItemData) adapter_story.getItem(position);
                String title, text, location, date;

                title = itemData_temp.context_title;
                text = itemData_temp.context_text;
                location = itemData_temp.context_location;
                date = itemData_temp.context_date;

                Toast.makeText(getApplicationContext(),title + "\n" + text + "\n" + date ,Toast.LENGTH_SHORT).show();
            }
        });
        listView_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyWriteItemData  itemData_temp = (MyWriteItemData) adapter_all.getItem(position);
                String title, text, location, date;

                title = itemData_temp.context_title;
                text = itemData_temp.context_text;
                location = itemData_temp.context_location;
                date = itemData_temp.context_date;

                Toast.makeText(getApplicationContext(),title + "\n" + text + "\n" + date ,Toast.LENGTH_SHORT).show();
            }
        });
        listView_review.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyWriteItemData  itemData_temp = (MyWriteItemData) adapter_review.getItem(position);
                String title, text, location, date;

                title = itemData_temp.context_title;
                text = itemData_temp.context_text;
                location = itemData_temp.context_location;
                date = itemData_temp.context_date;

                Toast.makeText(getApplicationContext(),title + "\n" + text + "\n" + date ,Toast.LENGTH_SHORT).show();
            }
        });


        // 임시 데이터
        adapter_all.addListItem("all", "text","location","date");
        adapter_all.addListItem("all1", "text1","location1","date1");
//       adapter_story.addListItem("story", "text","location","date");
//        adapter_story.addListItem("story", "text1","location1","date1");
//        adapter_review.addListItem("review", "text","location","date");
//        adapter_review.addListItem("review", "text1","location1","date1");



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





    /*****************  story 전체 데이터  **********************/
    void getTravleRecordAll_Server() {
        adapter_all.clear();

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
                    for(int i=0; i<arr.length(); i++ ) {

                        JSONObject obj = (JSONObject)arr.get(i);

                        int seq  = (Integer)obj.get("seq");
                        int user_info_seq = (Integer)obj.get("user_info_seq");
                        String presentation_image = (String)obj.get("presentation_image");
                        String text = (String)obj.get("text");
                        String title = (String)obj.get("title");
                        long start_date = (long)obj.get("start_date_client");
                        long finish_date = (long)obj.get("finish_date_client");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                        String start = df.format(start_date);
                        String finish = df.format(finish_date);
                        adapter_story.addListItem(title, text,"location",start + " ~ " + finish);

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

    /*****************  review 전체 데이터  **********************/
    void getTravleReviewAll_Server() {
        adapter_review.clear();

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
                    for(int i=0; i<arr.length(); i++ ) {

                        JSONObject obj = (JSONObject)arr.get(i);

                        int seq  = (Integer)obj.get("seq");
                        int user_info_seq = (Integer)obj.get("user_info_seq");
                        String presentation_image = (String)obj.get("presentation_image");
                        String text = (String)obj.get("text");
                        String title = (String)obj.get("title");
                        long start_date = (long)obj.get("start_date_client");
                        long finish_date = (long)obj.get("finish_date_client");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                        String start = df.format(start_date);
                        String finish = df.format(finish_date);
                        adapter_review.addListItem(title, text,"location",start + " ~ " + finish);

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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.show_all:
                show_all.setBackgroundColor(getResources().getColor(R.color.selectColor));
                show_story.setBackgroundColor(getResources().getColor(R.color.noSelectColor));
                show_review.setBackgroundColor(getResources().getColor(R.color.noSelectColor));
                listView_all.setVisibility(View.VISIBLE);
                listView_story.setVisibility(View.GONE);
                listView_review.setVisibility(View.GONE);
                break;

            case R.id.show_story:
                show_all.setBackgroundColor(getResources().getColor(R.color.noSelectColor));
                show_story.setBackgroundColor(getResources().getColor(R.color.selectColor));
                show_review.setBackgroundColor(getResources().getColor(R.color.noSelectColor));
                listView_all.setVisibility(View.GONE);
                listView_story.setVisibility(View.VISIBLE);
                listView_review.setVisibility(View.GONE);
                break;

            case R.id.show_review:
                show_all.setBackgroundColor(getResources().getColor(R.color.noSelectColor));
                show_story.setBackgroundColor(getResources().getColor(R.color.noSelectColor));
                show_review.setBackgroundColor(getResources().getColor(R.color.selectColor));
                listView_all.setVisibility(View.GONE);
                listView_story.setVisibility(View.GONE);
                listView_review.setVisibility(View.VISIBLE);
                break;

            case R.id.addTravlerBtn:
                Toast.makeText(getApplicationContext(), "add travler", Toast.LENGTH_SHORT).show();
                addFriend_Server(mySeq, 4);
                break;

            case R.id.userHeartBtn:
                if(heart_flag){
                    userHeartBtn.setImageDrawable(getResources().getDrawable(R.drawable.empty_heart));
                    Toast.makeText(getApplicationContext(),"좋아요 취소",Toast.LENGTH_SHORT).show();
                    heart_flag = false;
                }
                else{
                    userHeartBtn.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                    Toast.makeText(getApplicationContext(),"좋아요",Toast.LENGTH_SHORT).show();
                    heart_flag = true;
                }
                break;
        }
    }

    void widgetInit(){
        addTravlerBtn = (ImageButton) findViewById(R.id.addTravlerBtn);
        addTravlerBtn.setOnClickListener(this);
        userHeartBtn = (ImageButton) findViewById(R.id.userHeartBtn);
        userHeartBtn.setOnClickListener(this);

        listView_all = (ListView)findViewById(R.id.mywrite_all);
        adapter_all = new MyWriteCustomAdapter(itemDatas_all, UserActivity.this);
        listView_all.setAdapter(adapter_all);

        listView_story = (ListView)findViewById(R.id.mywrite_story);
        adapter_story = new MyWriteCustomAdapter(itemDatas_story, UserActivity.this);
        listView_story.setAdapter(adapter_story);

        listView_review = (ListView)findViewById(R.id.mywrite_review);
        adapter_review = new MyWriteCustomAdapter(itemDatas_review, UserActivity.this);
        listView_review.setAdapter(adapter_review);

        show_all = (Button)findViewById(R.id.show_all);
        show_all.setOnClickListener(this);
        show_story = (Button)findViewById(R.id.show_story);
        show_story.setOnClickListener(this);
        show_review = (Button)findViewById(R.id.show_review);
        show_review.setOnClickListener(this);


    }
}
