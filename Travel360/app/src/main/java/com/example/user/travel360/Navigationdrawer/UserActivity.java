package com.example.user.travel360.Navigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.travel360.CustomList.MyWriteCustomAdapter;
import com.example.user.travel360.CustomList.MyWriteItemData;
import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
   ImageButton addTravlerBtn;
    Button show_all, show_story, show_review;

    public ListView listView_all, listView_story, listView_review;
    public MyWriteCustomAdapter adapter_all, adapter_story, adapter_review;
    public ArrayList<MyWriteItemData> itemDatas_all = new ArrayList<MyWriteItemData>();
    public ArrayList<MyWriteItemData> itemDatas_story = new ArrayList<MyWriteItemData>();
    public ArrayList<MyWriteItemData> itemDatas_review = new ArrayList<MyWriteItemData>();

    int seq = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light_NoActionBar_TranslucentDecor);
        setContentView(R.layout.activity_user);

        //전달받는 seq
        Intent intent = getIntent();
        seq = intent.getExtras().getInt("seq");

        show_all = (Button)findViewById(R.id.show_all);
        show_all.setOnClickListener(this);
        show_story = (Button)findViewById(R.id.show_story);
        show_story.setOnClickListener(this);
        show_review = (Button)findViewById(R.id.show_review);
        show_review.setOnClickListener(this);

        listView_all = (ListView)findViewById(R.id.mywrite_all);
        adapter_all = new MyWriteCustomAdapter(itemDatas_all, UserActivity.this);
        listView_all.setAdapter(adapter_all);

        listView_story = (ListView)findViewById(R.id.mywrite_story);
        adapter_story = new MyWriteCustomAdapter(itemDatas_story, UserActivity.this);
        listView_story.setAdapter(adapter_story);

        listView_review = (ListView)findViewById(R.id.mywrite_review);
        adapter_review = new MyWriteCustomAdapter(itemDatas_review, UserActivity.this);
        listView_review.setAdapter(adapter_review);


        // 임시 데이터
        adapter_all.addListItem("all", "text","location","date");
        adapter_all.addListItem("all1", "text1","location1","date1");
        adapter_story.addListItem("story", "text","location","date");
        adapter_story.addListItem("story", "text1","location1","date1");
        adapter_review.addListItem("review", "text","location","date");
        adapter_review.addListItem("review", "text1","location1","date1");

        addTravlerBtn = (ImageButton) findViewById(R.id.addTravlerBtn);
        addTravlerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "add travler", Toast.LENGTH_SHORT).show();
                addFriend_Server(1, 4);
            }
        });
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
        }
    }
}
