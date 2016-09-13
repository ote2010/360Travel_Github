package com.example.user.travel360.Story;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.CustomList.CustomAdapter;
import com.example.user.travel360.CustomList.ItemData;
import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 2016-08-09.
 */
public class StoryReadActivity extends AppCompatActivity implements View.OnClickListener {
    /*
    프로세스 정리
    1) 표현할 이미지 개수 imgCount를 서버에서 받아온다.
    2) imgCount가 4개 이상, 4개, 3개, 2개, 1개 일 때의 경우에 따라 동적으로 코드에서 만들어준다.
    3) 받아온게 사진인지 그림인지. 판단해서 순차적으로 보여줘야한다.
    */

    int[] sequence = {1, 0, 0, 1, 0}; // 불러올 본문의 순서. 1 : 글 0 : 사진이라고 임의로 가정. 그러니까 글 사진 사진 글 사진과 같은 순서임.
    LinearLayout container; // container에 모든 뷰들이 담긴다. 전체 틀.
    String storyText;

    LinearLayout[] textLayout = new LinearLayout[50]; // 여행기에 추가하는 글 레이아웃 배열.
    LinearLayout[] imageLayout = new LinearLayout[50]; // 여행기에 추가하는 사진 레이아웃 배열.
    int textLayoutTotal = 0; // 글 레이아웃 전체 개수 변수(처음은 0. for문 돌면서 센다)
    int imageLayoutTotal = 0; // 이미지 레이아웃 전체 개수 변수(처음은 0. for문 돌면서 센다)

    int stringLayoutCount, imgLayoutCount; // 여행기에서 글이 몇 개인지. 이미지가 몇 개인지 받아옴.
    int imgCount; // 이미지 레이아웃에서 포함되는 총 이미지 개수 변수. 임의로 일단 단일변수로 가정.
    Intent imgCountIntent; // 버튼 동작을 위한 인텐트

    String[] imgUriArray;

    //쓰레드 작업을 위한 변수 (이미지를 웹에서 받아오는 작업은 백그라운드에서 진행해야해서)
    Bitmap[] bitmapImg = new Bitmap[10]; // 웹 URL -> 비트맵 으로 저장하기 위한 비트맵 배열
    ImageLoadingTask task; // 백그라운드 쓰레드 동작을 위한 Asynctask 클래스 객체


    // 댓글 관련 위젯
    public ListView listView;
    public CustomAdapter adapter;
    public ArrayList<ItemData> itemDatas = new ArrayList<ItemData>();
    public ItemData itemData;
    AlertDialog.Builder aDialog;
    AlertDialog ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_story_read);

        container = (LinearLayout) findViewById(R.id.container);
        task = new ImageLoadingTask();

        // 여행기 사진, 이미지가 몇개인지 받아오는 코드 필요!!! 일단 사진은 총 7개라고 가정.
        imgCount = 7;
        stringLayoutCount = 2;
        imgLayoutCount = 3;

        //*****여기서 서버에서 이미지 URL, 텍스트를 받아오는 코드를 작성***************
        storyText = "메밀꽃 필 무렵\n마지막 잎새가 어쩌구저쩌구 쏼라쏼라 끼야호 으아아앙ㄴ마ㅐ아ㅐㅏㅐ바재압ㅈ";

        storyLoad(); // 여행기 틀을 로드하는 메소드

        //이미지 URL : 1 파리 2 서울 3 프라하 4 리우데자네이루 5 뉴욕 6 만리장성 7 런던
        imgUriArray = new String[]{"http://www.gaviota.kr/xe/files/attach/images/163/900/003/PARIS_111001_14.jpg"
                , "http://cfd.tourtips.com/@cms_600/2015081735/gjexj7/%EC%84%9C%EC%9A%B8_%EA%B4%91%ED%99%94%EB%AC%B8%EC%9D%B4%EC%88%9C%EC%8B%A0%EB%8F%99%EC%83%81_MT(2).JPG"
                , "http://cfile28.uf.tistory.com/image/161C0E484D996432071D6C"
                , "http://cfile215.uf.daum.net/image/2278674F539FAD9C24F377"
                , "http://www.languagebookings.com/uploads/img/up/new_york_1.jpg"
                , "http://cfile214.uf.daum.net/image/2422054951ADACDD34188E"
                , "http://www.glion.co.kr/wp-content/themes/glion2/images/img_sub_top/banner-glion-life-in-london-page1280.jpg"};

        task.execute(imgUriArray);
        //*************************************************************************

        imgCountIntent = new Intent(getApplicationContext(), ImageViewer.class);

        findViewById(R.id.commentBtn).setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //onBackPressed();
            //Toast.makeText(this, "홈 아이콘 이벤트", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        //*** To 선아 *** 이부분 ****************
        if (id == R.id.travelerAddButton)
        {
            return true;
        }
        //*** To 선아 *** 이부분 ****************

        if (id == R.id.menuButton) {
            Toast.makeText(this, "메뉴 버튼 이벤트", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_read, menu);
        return true;
    }

    // 여행기 로드 메소드 : 글인지 사진인지에 따라서 storyTextLoad, storyImageLoad 메소드로 갈림
    public void storyLoad() {
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] == 1) // 글
            {
                storyTextLoad();
            } else // 사진
            {
                storyImageLoad();
            }
        }
    }

    // 여행기 글 로드 메소드
    public void storyTextLoad() {
        LayoutInflater textLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        textLayout[textLayoutTotal] = (LinearLayout) textLayoutInflater.inflate(R.layout.story_textview, null);
        TextView textLayoutTextView = (TextView) textLayout[textLayoutTotal].findViewById(R.id.storyTextView);
        textLayoutTextView.setText(storyText);

        container.addView(textLayout[textLayoutTotal]);

        textLayoutTotal++;
    }

    // 여행기 사진 메소드
    public void storyImageLoad() {
        LayoutInflater imageLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (imgCount > 4) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_morethan4pic, null);
            TextView otherimgCount = (TextView) imageLayout[imageLayoutTotal].findViewById(R.id.otherimgCount);
            otherimgCount.setText("+" + String.valueOf(imgCount - 3));
        } else if (imgCount == 4) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_4pic, null);
        } else if (imgCount == 3) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_3pic, null);
        } else if (imgCount == 2) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_2pic, null);
            ViewGroup.LayoutParams par = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            par.width = getResources().getDisplayMetrics().widthPixels;
            par.height = par.width;
            imageLayout[imageLayoutTotal].setLayoutParams(par);
        } else if (imgCount == 1) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_1pic, null);
        } else {
            Toast.makeText(getApplicationContext(), "Image Loading Error", Toast.LENGTH_LONG).show();
            return;
        }
        container.addView(imageLayout[imageLayoutTotal]);
        imageLayoutTotal++;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commentBtn:
                getComment_Server();
                break;

            case R.id.commentSentBtn:

               // writeStoryComment_Server();
                break;
        }


    }

    // 이미지 웹 상에서 불러오기 위한 AsyncTask 쓰레드 클래스
    private class ImageLoadingTask extends AsyncTask<String, Integer, Long> {
        //실제 스레드 작업을 작성하는 곳이며 execute에서 전달한 params 인수를 사용할 수 있다.
        //백그라운드에서 파라미터로 URL을 받아서 비트맵으로 이미지를 저장한다.
        @Override
        protected Long doInBackground(String... params) {
            try {
                // 지금은 그냥 모든 이미지 레이아웃의 이미지 개수 imgCount=7로 가정하고 짠 코드. 나중에 imgCount가 계속 바뀌면 그때 코드 바꿔야함
                int temp;
                if (imgCount >= 4) // 이미지가 4개 이상이면 그냥 4개만 이미지 표현해주면 됨!
                {
                    temp = 4;
                } else {
                    temp = imgCount;
                }

                for (int i = 0; i < temp; i++) {
                    URL ImageUrl = new URL(params[i]);
                    HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();

                    bitmapImg[i] = BitmapFactory.decodeStream(is);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //doInBackground 작업의 리턴값을 파라미터로 받으며 작업이 끝났음을 알리는 작업을 작성한다.
        @Override
        protected void onPostExecute(Long aLong) {
            for (int i = 0; i < imgLayoutCount; i++) {
                if (imgCount > 4) {
                    ImageView morepic4Viewer1 = (ImageView) imageLayout[i].findViewById(R.id.morepic4Viewer1);
                    ImageView morepic4Viewer2 = (ImageView) imageLayout[i].findViewById(R.id.morepic4Viewer2);
                    ImageView morepic4Viewer3 = (ImageView) imageLayout[i].findViewById(R.id.morepic4Viewer3);
                    ImageView morepic4Viewer4 = (ImageView) imageLayout[i].findViewById(R.id.morepic4Viewer4);

                    morepic4Viewer1.setImageBitmap(bitmapImg[0]);
                    morepic4Viewer2.setImageBitmap(bitmapImg[1]);
                    morepic4Viewer3.setImageBitmap(bitmapImg[2]);
                    morepic4Viewer4.setImageBitmap(bitmapImg[3]);

                    morepic4Viewer1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imgCountIntent.putExtra("imgCountIntent", imgCount);
                            imgCountIntent.putExtra("imgIndex", 1);
                            imgCountIntent.putExtra("imgUri", imgUriArray);
                            startActivity(imgCountIntent);
                        }
                    });
                    morepic4Viewer2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imgCountIntent.putExtra("imgCountIntent", imgCount);
                            imgCountIntent.putExtra("imgIndex", 2);
                            imgCountIntent.putExtra("imgUri", imgUriArray);
                            startActivity(imgCountIntent);
                        }
                    });
                    morepic4Viewer3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imgCountIntent.putExtra("imgCountIntent", imgCount);
                            imgCountIntent.putExtra("imgIndex", 3);
                            imgCountIntent.putExtra("imgUri", imgUriArray);
                            startActivity(imgCountIntent);
                        }
                    });
                    morepic4Viewer4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imgCountIntent.putExtra("imgCountIntent", imgCount);
                            imgCountIntent.putExtra("imgIndex", 4);
                            imgCountIntent.putExtra("imgUri", imgUriArray);
                            startActivity(imgCountIntent);
                        }
                    });
                } else if (imgCount == 4) {
                    ImageView pic4Viewer1 = (ImageView) imageLayout[i].findViewById(R.id.pic4Viewer1);
                    ImageView pic4Viewer2 = (ImageView) imageLayout[i].findViewById(R.id.pic4Viewer2);
                    ImageView pic4Viewer3 = (ImageView) imageLayout[i].findViewById(R.id.pic4Viewer3);
                    ImageView pic4Viewer4 = (ImageView) imageLayout[i].findViewById(R.id.pic4Viewer4);

                    pic4Viewer1.setImageBitmap(bitmapImg[0]);
                    pic4Viewer2.setImageBitmap(bitmapImg[1]);
                    pic4Viewer3.setImageBitmap(bitmapImg[2]);
                    pic4Viewer4.setImageBitmap(bitmapImg[3]);
                } else if (imgCount == 3) {
                    ImageView pic3Viewer1 = (ImageView) imageLayout[i].findViewById(R.id.pic3Viewer1);
                    ImageView pic3Viewer2 = (ImageView) imageLayout[i].findViewById(R.id.pic3Viewer2);
                    ImageView pic3Viewer3 = (ImageView) imageLayout[i].findViewById(R.id.pic3Viewer3);

                    pic3Viewer1.setImageBitmap(bitmapImg[0]);
                    pic3Viewer2.setImageBitmap(bitmapImg[1]);
                    pic3Viewer3.setImageBitmap(bitmapImg[2]);
                } else if (imgCount == 2) {
                    ImageView pic2Viewer1 = (ImageView) imageLayout[i].findViewById(R.id.pic2Viewer1);
                    ImageView pic2Viewer2 = (ImageView) imageLayout[i].findViewById(R.id.pic2Viewer2);

                    pic2Viewer1.setImageBitmap(bitmapImg[0]);
                    pic2Viewer2.setImageBitmap(bitmapImg[1]);
                } else if (imgCount == 1) {
                    ImageView pic1Viewer1 = (ImageView) imageLayout[i].findViewById(R.id.pic1Viewer1);

                    pic1Viewer1.setImageBitmap(bitmapImg[0]);
                }
            }
        }

        //doInBackground 시작 전에 호출되어 UI 스레드에서 실행된다. 주로 로딩바나 Progress 같은 동작 중임을 알리는 작업을 작성한다.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //publishProgress()를 통해 호출되며 UI 스레드에서 실행된다. 파일 내려받는다고 치면 그때 퍼센티지 표시 작업 같은 걸 작성한다.
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    /***********
     * 여행기 댓글 리스트
     ***************/
    void getComment_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 userSeq 만 있으면 됩니다.
        params.put("travelSeq", "1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getComment_Server()");
        client.get("http://kibox327.cafe24.com/getTravelCommentList.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {   }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflate.inflate(R.layout.comment_dialog, null);

                aDialog = new AlertDialog.Builder(StoryReadActivity.this);
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
                adapter = new CustomAdapter(itemDatas, StoryReadActivity.this);
                adapter.clear();
                listView.setAdapter(adapter);

                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("comment") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = (JSONObject) arr.get(i);
                        String com = (String) obj.get("comment");
                        String comment = new String(com.getBytes("euc-kr"),"utf-8"); //8859_1
                        
                        int evaluation = (int) obj.get("evaluation");
                        String id = (String) obj.get("id");
                        int seq = (int) obj.get("seq");
                        int user_info_seq = (int) obj.get("user_info_seq");
                        JSONObject write_date = (JSONObject) obj.get("write_date");
                        long time = (long) write_date.get("time");

                        adapter.addListItem(id, comment); 
                        listView.setSelection(i);
                        Log.d("SUN", "comment : " + comment + " , id : " + id + " , user_info_seq : " + user_info_seq + " , time : " + time + " , seq : " + seq);

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
            public void onRetry(int retryNo) {     }
        });
    }


    /****************************
     * 여행기 댓글 쓰기
     *************************/
    void writeStoryComment_Server(String comment) {
           long todaydate = System.currentTimeMillis(); // long 형의 현재시간

        RequestParams params = new RequestParams();
        params.put("comment", comment);
        params.put("evaluation", "1");
        params.put("travel_record_seq", "1");
        params.put("write_date_client",todaydate);

        params.put("UserSeq", "1");
        params.put("travelSeq", "1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStoryComment_Server()");
        client.get("http://kibox327.cafe24.com/writeComment.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {     }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                ad.cancel();
                Toast.makeText(StoryReadActivity.this,"댓글이 등록되었습니다.",Toast.LENGTH_SHORT).show();
                getComment_Server();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {   }
        });
    }
}
