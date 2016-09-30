package com.example.user.travel360.Navigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.CustomList.MyWriteCustomAdapter;
import com.example.user.travel360.CustomList.MyWriteItemData;
import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView userProfileImg; // 사용자 프로필 이미지
    ImageButton addTravlerBtn, userHeartBtn;
    Button  show_story, show_review;
    TextView userIdTv;

    public ListView  listView_story, listView_review;
    public MyWriteCustomAdapter  adapter_story, adapter_review;
    public ArrayList<MyWriteItemData> itemDatas_story = new ArrayList<MyWriteItemData>();
    public ArrayList<MyWriteItemData> itemDatas_review = new ArrayList<MyWriteItemData>();

    boolean heart_flag = false;
    int MY_SEQ = -1;
    int HERE_SEQ = -1;
    int ONSELF_FLAG = -1; // 본인확인 플래그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light_NoActionBar_TranslucentDecor);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        HERE_SEQ = intent.getExtras().getInt("userSeq", -1);

        // 본인확인
        try{
            MY_SEQ = Integer.valueOf(ApplicationController.getInstance().getSeq());
            if(MY_SEQ == HERE_SEQ)
                ONSELF_FLAG = 1; // 들어온 사용자 화면이 본인 일 때
        }catch (Exception e){
            Log.d("SUN", "MY_SEQ is null");
            // 임시로....intent 값 받으면 삭제
            MY_SEQ=1;
            ONSELF_FLAG = 0;
        }

        widgetInit();
        getTravleRecordAll_Server();
        getTravleReviewAll_Server();
        getUserInfo_Server(MY_SEQ);



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
        adapter_story.clear();

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
         RequestParams params = new RequestParams();
         params.put("location", "Paris");
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "getTravleReviewAll_Server()");
        client.get("http://kibox327.cafe24.com//travelReviewList.do",params ,new AsyncHttpResponseHandler() {
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
                        int user_info_seq  = (Integer)obj.get("user_info_seq");
                        String text = (String)obj.get("text");
                        String location = (String)obj.get("location");
                        long write_date = (long)obj.get("write_date_client");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                        String writedate = df.format(write_date);
                        adapter_review.addListItem(location, text,"",writedate );

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


    /************* 사용자 정보 **************/
    void getUserInfo_Server(int userSeq) {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq 만 있으면 됩니다.
        params.put("seq",userSeq);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getUserInfo_Server()");
        client.get("http://kibox327.cafe24.com/getUserInfo.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try{
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("userDto") + "";
                    JSONObject obj = new JSONObject(objStr);

                    String id = (String)obj.get("id");
                    String name = (String)obj.get("name");
                    String profile_image = (String)obj.get("profile_image");
                    getImage_Server(profile_image);
                    Log.d("SUN", "profile_image : "+profile_image);
                    userIdTv.setText(id+" ("+name+")");

                }catch (JSONException e){

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {  }
        });
    }


    /***************  image 가져오기  *********************/

    public Bitmap byteArrayToBitmap(byte[] byteArray ) {  // byte -> bitmap 변환 및 반환
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return bitmap ;
    }


    void getImage_Server(String imageName) {

        RequestParams params = new RequestParams();
        // 보내는 data는 imageName 만 있으면 됩니다.
        params.put("imageName", imageName);
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getImage_Server()");
        client.get("http://kibox327.cafe24.com/Image.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // byteArrayToBitmap 를 통해 reponse로 받은 이미지 데이터 bitmap으로 변환
                Bitmap bitmap = byteArrayToBitmap(response);
                userProfileImg.setImageBitmap(bitmap);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {    }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.show_story:
                show_story.setBackgroundColor(getResources().getColor(R.color.selectColor));
                show_review.setBackgroundColor(getResources().getColor(R.color.noSelectColor));
                listView_story.setVisibility(View.VISIBLE);
                listView_review.setVisibility(View.GONE);
                break;

            case R.id.show_review:
                show_story.setBackgroundColor(getResources().getColor(R.color.noSelectColor));
                show_review.setBackgroundColor(getResources().getColor(R.color.selectColor));
                listView_story.setVisibility(View.GONE);
                listView_review.setVisibility(View.VISIBLE);
                break;

            case R.id.addTravlerBtn:
                if(ONSELF_FLAG==0){ // 로그인 & 본인이 아닐 때
                    Toast.makeText(getApplicationContext(), "add travler", Toast.LENGTH_SHORT).show();
                    addFriend_Server(MY_SEQ, HERE_SEQ);
                }
                break;

            case R.id.userHeartBtn:

                if(ONSELF_FLAG==0){ // 로그인 & 본인이 아닐 때
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
                }
                break;
            case R.id.userProfileImg:
                Toast.makeText(getApplicationContext(),"사진 선택",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    void widgetInit(){
        userIdTv= (TextView) findViewById(R.id.userId);

        userProfileImg = (ImageView) findViewById(R.id.userProfileImg);
        userProfileImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1001);
            }
        });

        addTravlerBtn = (ImageButton) findViewById(R.id.addTravlerBtn);
        addTravlerBtn.setOnClickListener(this);
        userHeartBtn = (ImageButton) findViewById(R.id.userHeartBtn);
        userHeartBtn.setOnClickListener(this);

        listView_story = (ListView)findViewById(R.id.mywrite_story);
        adapter_story = new MyWriteCustomAdapter(itemDatas_story, UserActivity.this);
        listView_story.setAdapter(adapter_story);

        listView_review = (ListView)findViewById(R.id.mywrite_review);
        adapter_review = new MyWriteCustomAdapter(itemDatas_review, UserActivity.this);
        listView_review.setAdapter(adapter_review);

        show_story = (Button)findViewById(R.id.show_story);
        show_story.setOnClickListener(this);
        show_review = (Button)findViewById(R.id.show_review);
        show_review.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1001)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                try
                {
                    Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    userProfileImg.setImageBitmap(image);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
