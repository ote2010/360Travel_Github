package com.example.user.travel360.Story;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 2016-08-09.
 */
public class StoryReadActivity extends AppCompatActivity implements View.OnClickListener {

    final static int SERVER_CONNECT_ERR_LOOPCOUNT = 150000000;

    ArrayList <Integer> sequence = new ArrayList <Integer>(); // 불러올 본문의 순서. 1 : 글 0 : 사진이라고 임의로 가정. 그러니까 글 사진 사진 글 사진과 같은 순서임.
    LinearLayout container; // container에 모든 뷰들이 담긴다. 전체 틀.

    LinearLayout[] textLayout = new LinearLayout[50]; // 여행기에 추가하는 글 레이아웃 배열.
    LinearLayout[] imageLayout = new LinearLayout[50]; // 여행기에 추가하는 사진 레이아웃 배열.
    int textLayoutTotal = 0; // 글 레이아웃 전체 개수 변수(처음은 0. for문 돌면서 센다)
    int imageLayoutTotal = 0; // 이미지 레이아웃 전체 개수 변수(처음은 0. for문 돌면서 센다)

    int imgCount; // 이미지 레이아웃에서 포함되는 총 이미지 개수 변수. 임의로 일단 단일변수로 가정.
    Intent imgCountIntent; // 버튼 동작을 위한 인텐트

    //서버관련 코드
    ArrayList <Image> ImageList = new ArrayList <Image> ();
    ArrayList <String> TextList = new ArrayList <String> ();
    String title, text;
    String id, name, profile_image;
    int writerSeq = -1;
    long start_date_client, finish_date_client;
    TextView travelDateTextView;
    int imgGroupIndex = 0;

    ArrayList <ArrayList <Image>> ImageGrpList = new ArrayList <ArrayList <Image>> ();
    ArrayList <Image> threadImageList = new ArrayList <Image> ();

    ImageView [] threadImgView;

    // 댓글 관련 위젯
    public ListView listView;
    public CustomAdapter adapter;
    public ArrayList<ItemData> itemDatas = new ArrayList<ItemData>();
    public ItemData itemData;
    AlertDialog.Builder aDialog;
    AlertDialog ad;

    //유저정보
    ImageView travelerProfileImg;
    ImageButton travelerAddBtn, messageBtn;
    TextView travelReadUserID;

    int storySeq = -1, userSeq = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar1));
        setContentView(R.layout.activity_story_read);

        Intent intent = getIntent();
        storySeq = intent.getExtras().getInt("seq");

        Log.d("storySeq", String.valueOf(storySeq));

        container = (LinearLayout) findViewById(R.id.container);
        travelDateTextView = (TextView) findViewById(R.id.travelDateTextView);
        imgCountIntent = new Intent(getApplicationContext(), ImageViewer.class);

        travelerProfileImg = (ImageView)findViewById(R.id.travelerProfileImg);
        travelerAddBtn = (ImageButton)findViewById(R.id.travelerAddBtn);
        messageBtn = (ImageButton)findViewById(R.id.messageBtn);
        travelReadUserID = (TextView)findViewById(R.id.travelReadUserID);

        travelerAddBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String LoginFlag = ApplicationController.getInstance().getEmail();

                boolean check = (LoginFlag + "").equals(null + "");

                if (check)
                {
                    Toast.makeText(getApplicationContext(), "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(writerSeq == -1)
                    {
                        Toast.makeText(getApplicationContext(), "아직 서버에서 정보를 불러오지 못했습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());
                        Toast.makeText(getApplicationContext(), "친구 등록이 완료되었습니다!", Toast.LENGTH_LONG).show();
                        addFriend_Server(userSeq, writerSeq);
                    }
                }
            }
        });

        findViewById(R.id.commentBtn).setOnClickListener(this);

        getTravelRecord_Server(storySeq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
            return true;
        }

        if (id == R.id.menuButton) {
            //Toast.makeText(this, "메뉴 버튼 이벤트", Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < sequence.size(); i++) {
            if (sequence.get(i) == 1) // 글
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
        textLayoutTextView.setText(TextList.get(textLayoutTotal));

        container.addView(textLayout[textLayoutTotal]);

        textLayoutTotal++;
    }

    // 여행기 사진 메소드
    public void storyImageLoad()
    {
        imgCount = 0;
        for(int i=0; i<ImageList.size(); i++)
        {
            if(ImageList.get(i).getPicture_group_seq() == imgGroupIndex)
            {
                imgCount++;
            }
        }
        imgGroupIndex++;
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;

        LayoutInflater imageLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (imgCount > 4) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_morethan4pic, null);
            TextView otherimgCount = (TextView) imageLayout[imageLayoutTotal].findViewById(R.id.otherimgCount);
            otherimgCount.setText("+" + String.valueOf(imgCount - 3));
            imageLayout[imageLayoutTotal].setLayoutParams(new LinearLayout.LayoutParams(width, width));
        } else if (imgCount == 4) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_4pic, null);
            imageLayout[imageLayoutTotal].setLayoutParams(new LinearLayout.LayoutParams(width, width));
        } else if (imgCount == 3) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_3pic, null);
            imageLayout[imageLayoutTotal].setLayoutParams(new LinearLayout.LayoutParams(width, width));
        } else if (imgCount == 2) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_2pic, null);
            ViewGroup.LayoutParams par = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            par.width = getResources().getDisplayMetrics().widthPixels;
            par.height = par.width;
            imageLayout[imageLayoutTotal].setLayoutParams(par);
        } else if (imgCount == 1) {
            imageLayout[imageLayoutTotal] = (LinearLayout) imageLayoutInflater.inflate(R.layout.story_1pic, null);
            imageLayout[imageLayoutTotal].setLayoutParams(new LinearLayout.LayoutParams(width, width));
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
                if(ApplicationController.getInstance().getSeq() == null)
                    Toast.makeText(getApplicationContext(), "로그인이 필요한 서비스입니다.", Toast.LENGTH_LONG).show();
                else
                {
                    userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());
                    getComment_Server();
                }

                break;

            case R.id.commentSentBtn:
                //writeStoryComment_Server();
                break;
        }
    }

    class BitmapAndIndex
    {
        Bitmap bitmap;
        int index;

        public BitmapAndIndex(Bitmap bitmap, int index)
        {
            this.bitmap = bitmap;
            this.index = index;
        }

        public Bitmap getBitmap()
        {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap)
        {
            this.bitmap = bitmap;
        }

        public int getIndex()
        {
            return index;
        }

        public void setIndex(int index)
        {
            this.index = index;
        }
    }

    private class ImageLoadingTask extends AsyncTask <Integer, Integer, BitmapAndIndex> {
        @Override
        protected BitmapAndIndex doInBackground(Integer... params) {
            Bitmap bitmap = null;
            try
            {
                URL ImageUrl = new URL("http://kibox327.cafe24.com/Image.do?imageName=" + threadImageList.get(params[0]).getPicture_loc());
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();

                bitmap = BitmapFactory.decodeStream(is);
                Log.d("ImageLoadingTask", String.valueOf(params[0]) + ": success");
            }
            catch(IOException e)
            {
                e.printStackTrace();
                Log.d("ImageLoadingTask", String.valueOf(params[0]) + ": fail");
            }
            BitmapAndIndex bitmapAndIndex = new BitmapAndIndex(bitmap, params[0]);

            return bitmapAndIndex;
        }

        @Override
        protected void onPostExecute(BitmapAndIndex bitmapAndIndex)
        {
            if (bitmapAndIndex != null)
            {
                if(threadImgView[bitmapAndIndex.getIndex()] != null)
                {
                    threadImgView[bitmapAndIndex.getIndex()].setImageBitmap(bitmapAndIndex.getBitmap());
                    Log.d("ImageLoadingTask", String.valueOf(bitmapAndIndex.getIndex()) + ": setBitmap success");
                }
            }

            Log.d("multiThreading", String.valueOf(bitmapAndIndex.getIndex()) + "번 쓰레드 작업끝!");
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
        }
    }

    AsyncHttpClient client;
    void getTravelRecord_Server(int storySeq) {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq 만 있으면 됩니다.
        params.put("seq", storySeq);

        client = new AsyncHttpClient();

        Log.d("getTravleRecord_Server", "getData_Server()");
        client.get("http://kibox327.cafe24.com/getTravelRecord.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d("getTravleRecord_Server", "getData_Server() onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //Log.d("getTravleRecord_Server", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);

                try {
                    JSONObject obj = new JSONObject(res);

                    Log.d("getTravleRecord_Server", "images : " + obj.get("images"));
                    Log.d("getTravleRecord_Server", "travelRecordDto : " + obj.get("travelRecordDto"));
                    Log.d("getTravleRecord_Server", "travel : " + obj.get("travel"));

                    String imagesStr = obj.get("images") + "";
                    JSONArray imagesArr = new JSONArray(imagesStr);

                    String dateStr = obj.get("travelRecordDto") + "";
                    JSONObject dateStrObj = new JSONObject(dateStr);

                    start_date_client = (long)dateStrObj.get("start_date_client");
                    finish_date_client = (long)dateStrObj.get("finish_date_client");
                    Log.d("start_date_client", "start_date_client : " + String.valueOf(start_date_client));
                    Log.d("finish_date_client", "finish_date_client : " + String.valueOf(finish_date_client));


                    SimpleDateFormat format = new SimpleDateFormat("yy년 MM월 dd일 E요일");
                    travelDateTextView.setText("여행 시작일 : " + format.format(start_date_client) + "\n여행 종료일 : " + format.format(finish_date_client));

                    for (int i = 0; i < imagesArr.length(); i++)
                    {
                        JSONObject imageObj = (JSONObject) imagesArr.get(i);
                        int picture_group_seq = (int)imageObj.get("picture_group_seq");
                        String picture_loc = (String)imageObj.get("picture_loc");
                        int seq = (int)imageObj.get("seq");

                        Log.d("ImageList", "picture_group_seq : " + String.valueOf(picture_group_seq));
                        Log.d("ImageList", "picture_loc : " + picture_loc);
                        Log.d("ImageList", "seq : " + String.valueOf(seq));

                        Image image = new Image(picture_group_seq, picture_loc, seq);
                        ImageList.add(image);
                    }

                    String travelStr = obj.get("travel") + "";
                    JSONObject travelStrObj = new JSONObject(travelStr);

                    title = (String)travelStrObj.get("title");
                    text = (String)travelStrObj.get("text");

                    String userinfo = travelStrObj.get("userinfo") + "";
                    JSONObject userinfoObj = new JSONObject(userinfo);

                    id = (String)userinfoObj.get("email");
                    name = (String)userinfoObj.get("name");
                    profile_image = (String)userinfoObj.get("profile_image");
                    writerSeq = (int)userinfoObj.get("seq");

                    travelReadUserID.setText(id+" ("+name+")");
                    getImage_Server(profile_image);

                    Log.d("travelStrObj", "title : " + title);
                    Log.d("travelStrObj", "text : " + text);

                    String[] values = text.split("\n");

                    for(int i = 0; i<values.length; i++)
                    {
                        if(values[i].contains("ImgGroup"))
                        {
                            sequence.add(0);
                            Log.d("sequence", String.valueOf(sequence.get(sequence.size()-1)));
                        }
                        else if(values[i].contains("TxtGroup"))
                        {
                            sequence.add(1);
                            TextList.add(values[i+1]);
                            Log.d("sequence", String.valueOf(sequence.get(sequence.size()-1)));
                            Log.d("TextList", TextList.get(TextList.size()-1));
                        }
                        else
                            continue;
                    }

                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle(title);

                    storyLoad();

                    int grpSeqTemp = 0;
                    ArrayList <Image> temp = new ArrayList <Image> ();
                    for(int i = 0; i<ImageList.size(); i++)
                    {
                        if(ImageList.get(i).getPicture_group_seq() == grpSeqTemp)
                        {
                            temp.add(ImageList.get(i));
                        }
                        else
                        {
                            ImageGrpList.add((ArrayList <Image>)temp.clone());
                            temp.clear();
                            temp.add(ImageList.get(i));
                            grpSeqTemp++;
                        }
                        if(i == ImageList.size()-1)
                        {
                            ImageGrpList.add(temp);
                        }
                    }

                    for(int i = 0; i<ImageGrpList.size(); i++)
                    {
                        if(ImageGrpList.get(i).size() > 4)
                        {
                            for(int j = 4; j<ImageGrpList.get(i).size(); j++)
                            {
                                if(j==4)
                                {
                                    Image dummy = new Image(-1, "dummy", -1);
                                    ImageGrpList.get(i).set(4, dummy);
                                    continue;
                                }
                                ImageGrpList.get(i).remove(j);
                            }
                        }
                    }

                    int imageTaskCount = 0;
                    for(int i = 0; i < ImageGrpList.size(); i++)
                    {
                        if(ImageGrpList.get(i).size() > 4)
                            imageTaskCount += 4;
                        else
                            imageTaskCount += ImageGrpList.get(i).size();
                    }
                    Log.d("imageTaskCount", String.valueOf(imageTaskCount));

                    threadImgView = new ImageView [imageTaskCount];

                    int imgtaskcountindex = 0;
                    int count = 0;
                    for(int i = 0; i<ImageGrpList.size(); i++)
                    {
                        while(true)
                        {
                            if(imageLayout[i] != null)
                                break;
                            else
                                count++;

                            if(count == SERVER_CONNECT_ERR_LOOPCOUNT) // 무한루프방지
                            {
                                Toast.makeText(getApplicationContext(), "서버와 연결 장애가 발생했습니다!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        if(ImageGrpList.get(i).size() > 4)
                        {
                            threadImgView[imgtaskcountindex] = (ImageView)imageLayout[i].findViewById(R.id.morepic4Viewer1);
                            threadImgView[imgtaskcountindex+1] = (ImageView)imageLayout[i].findViewById(R.id.morepic4Viewer2);
                            threadImgView[imgtaskcountindex+2] = (ImageView)imageLayout[i].findViewById(R.id.morepic4Viewer3);
                            threadImgView[imgtaskcountindex+3] = (ImageView)imageLayout[i].findViewById(R.id.morepic4Viewer4);

                            threadImgView[imgtaskcountindex].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "4개이상 : 1");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 1);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+1].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "4개이상 : 2");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 2);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+2].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "4개이상 : 3");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 3);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+3].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "4개이상 : 4");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 4);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            imgtaskcountindex += 4;
                            ImageGrpList.get(i).remove(4);
                        }
                        else if(ImageGrpList.get(i).size() == 4)
                        {
                            threadImgView[imgtaskcountindex] = (ImageView)imageLayout[i].findViewById(R.id.pic4Viewer1);
                            threadImgView[imgtaskcountindex+1] = (ImageView)imageLayout[i].findViewById(R.id.pic4Viewer2);
                            threadImgView[imgtaskcountindex+2] = (ImageView)imageLayout[i].findViewById(R.id.pic4Viewer3);
                            threadImgView[imgtaskcountindex+3] = (ImageView)imageLayout[i].findViewById(R.id.pic4Viewer4);

                            threadImgView[imgtaskcountindex].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "4개 : 1");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 1);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+1].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "4개 : 2");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 2);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+2].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "4개 : 3");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 3);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+3].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "4개 : 4");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 4);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });

                            imgtaskcountindex += 4;
                        }
                        else if(ImageGrpList.get(i).size() == 3)
                        {
                            threadImgView[imgtaskcountindex] = (ImageView)imageLayout[i].findViewById(R.id.pic3Viewer1);
                            threadImgView[imgtaskcountindex+1] = (ImageView)imageLayout[i].findViewById(R.id.pic3Viewer2);
                            threadImgView[imgtaskcountindex+2] = (ImageView)imageLayout[i].findViewById(R.id.pic3Viewer3);

                            threadImgView[imgtaskcountindex].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "3개 : 1");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 1);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+1].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "3개 : 2");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 2);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+2].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "3개 : 3");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 3);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            imgtaskcountindex += 3;
                        }
                        else if(ImageGrpList.get(i).size() == 2)
                        {
                            threadImgView[imgtaskcountindex] = (ImageView)imageLayout[i].findViewById(R.id.pic2Viewer1);
                            threadImgView[imgtaskcountindex+1] = (ImageView)imageLayout[i].findViewById(R.id.pic2Viewer2);

                            threadImgView[imgtaskcountindex].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "2개 : 1");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 1);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            threadImgView[imgtaskcountindex+1].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "2개 : 2");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 2);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            imgtaskcountindex += 2;
                        }
                        else if(ImageGrpList.get(i).size() == 1)
                        {
                            threadImgView[imgtaskcountindex] = (ImageView)imageLayout[i].findViewById(R.id.pic1Viewer1);
                            threadImgView[imgtaskcountindex].setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Log.d("threadImgView", "1개 : 1");
                                    imgCountIntent.putExtra("imgCount", ImageList.size());
                                    imgCountIntent.putExtra("imgIndex", 1);
                                    imgCountIntent.putExtra("imgList", ImageList);
                                    startActivity(imgCountIntent);
                                }
                            });
                            imgtaskcountindex++;
                        }
                    }

                    for(int i = 0; i<ImageGrpList.size(); i++)
                    {
                        threadImageList.addAll(ImageGrpList.get(i));
                    }

                    for(int i = 0; i < imageTaskCount; i++)
                    {
                        ImageLoadingTask task = new ImageLoadingTask();
                        task.execute(i);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Log.d("getTravleRecord_Server",  "e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("getTravleRecord_Server", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }


            @Override
            public void onRetry(int retryNo)
            {
                Log.d("getTravleRecord_Server", "onRetry");
            }
        });
    }

    /***********
     * 여행기 댓글 리스트
     ***************/
    void getComment_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 userSeq 만 있으면 됩니다.
        params.put("travelSeq", storySeq);

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
                        String comment = new String(com.getBytes("utf-8"),"utf-8"); //8859_1
                        
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
                travelerProfileImg.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {    }
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

        params.put("userSeq", userSeq);
        params.put("travelSeq", storySeq);
        //params.put("userSeq", 1);
        //params.put("travelSeq", 1);

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

    /************** 친구 (요청)추가 하기  ***********************/
    void addFriend_Server(int mySeq, int otherSeq) {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq, targetSeq 만 있으면 됩니다.
        params.put("seq", mySeq);
        params.put("targetSeq", otherSeq);
        Log.d("SUN", "mySeq : " + String.valueOf(mySeq));
        Log.d("SUN", "otherSeq : " + String.valueOf(otherSeq));

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
}
