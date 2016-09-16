package com.example.user.travel360.Story;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.user.travel360.ApplicationController;
import com.example.user.travel360.CustomDialog.MainImgSelectDialog;
import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 2016-08-24.
 */
public class StoryWrite2Activity extends AppCompatActivity
{
    final static int MAIN_IMG_DIALOG_REQCODE = 1234;

    LinearLayout uploadImgLayout, mainStoryImgLayout, reviewWriteLayout, travelDayLayout;
    ArrayList<ArrayList<String>> selectedPhotos = new ArrayList <ArrayList<String>>();
    ArrayList<ArrayList<Integer>> imgSeqList = new ArrayList <ArrayList<Integer>>();
    ArrayList <RecyclerView> recyclerView = new ArrayList <RecyclerView> ();
    ImageView mainStoryImgAddButton, reviewWriteAddButton, travelDayAddButton;
    ArrayList <String> mergePhotos = new ArrayList <String> ();
    ArrayList <Integer> mergeImgSeqList = new ArrayList <Integer> ();
    String storystring;
    int travelSeq = -1;

    // ****서버에 보낼때 필요한 변수 ****
    static int selectedMainImgSeq = -1;
    static int userSeq = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_story_write2);

        uploadImgLayout = (LinearLayout) findViewById(R.id.uploadImgLayout);
        mainStoryImgLayout = (LinearLayout) findViewById(R.id.mainStoryImgLayout);
        reviewWriteLayout = (LinearLayout) findViewById(R.id.reviewWriteLayout);
        travelDayLayout = (LinearLayout) findViewById(R.id.travelDayLayout);
        mainStoryImgAddButton = (ImageView) findViewById(R.id.mainStoryImgAddButton);
        reviewWriteAddButton = (ImageView) findViewById(R.id.reviewWriteAddButton);
        travelDayAddButton = (ImageView) findViewById(R.id.travelDayAddButton);

        if(ApplicationController.getInstance().getSeq() != null)
        {
            userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());
            Log.d("userSeq", String.valueOf(userSeq));
        }

        Intent intent = getIntent();
        storystring = new String(intent.getExtras().getString("storystring"));
        travelSeq = intent.getExtras().getInt("travelSeq");
        Log.d("storystring", storystring);
        selectedPhotos = (ArrayList <ArrayList<String>>) intent.getExtras().get("selectedPhotos");
        imgSeqList = (ArrayList <ArrayList<Integer>>) intent.getExtras().get("imgSeqList");

        mergeSelectedPhotos();

        //storystring 텍스트 확인 테스트용
        //TextView textView = (TextView) findViewById(R.id.textView8);
        //textView.setText(storystring);

        mainStoryImgAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainImgSelectDialog.class);
                intent.putExtra("mergePhotos", mergePhotos);
                intent.putExtra("mergeImgSeqList", mergeImgSeqList);
                startActivityForResult(intent, MAIN_IMG_DIALOG_REQCODE);
            }
        });

        reviewWriteAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });

        travelDayAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });

        uploadedImgShowing();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && (requestCode == MAIN_IMG_DIALOG_REQCODE))
        {
            selectedMainImgSeq = (int)data.getExtras().getInt("selectedMainImgSeq");
        }
    }

    private void mergeSelectedPhotos()
    {
        for(int i=0; i<selectedPhotos.size(); i++)
        {
            mergePhotos.addAll(selectedPhotos.get(i));
        }

        for(int i=0; i<imgSeqList.size(); i++)
        {
            mergeImgSeqList.addAll(imgSeqList.get(i));
        }
    }

    private void uploadedImgShowing()
    {
        final RecyclerView listItem = new RecyclerView(getApplicationContext());
        PhotoAdapter photoAdapter = new PhotoAdapter(getApplicationContext(), mergePhotos);

        listItem.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        listItem.setAdapter(photoAdapter);

        listItem.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
            }

            @Override
            public void onItemLongClick()
            {
            }
        }));

        listItem.setBackgroundColor(Color.rgb(255, 164, 78));
        uploadImgLayout.addView(listItem);
        recyclerView.add(listItem);
    }

    private void storyWriteComplete()
    {
        RequestParams params = new RequestParams();

        params.put("userSeq", userSeq);
        params.put("seq", travelSeq);
        params.put("text", storystring);
        params.put("title", "write title");
        params.put("presentation_image", selectedMainImgSeq);
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("storyWriteComplete", "writeStory_Server()");
        client.get("http://kibox327.cafe24.com/writeComplete.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d("storyWriteComplete", "getData_Server() onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("storyWriteComplete", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("storyWriteComplete", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {        }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            finish();
            return true;
        }
        if(id == R.id.complete)
        {
            storyWriteComplete();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_write2, menu);
        return true;
    }

}
