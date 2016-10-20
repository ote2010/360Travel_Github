package com.example.user.travel360.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.user.travel360.R;
import com.example.user.travel360.Story.StoryReadActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


public class Search_story_fragment extends Fragment {
    ViewGroup v;

    ScrollView storyFragmentScrollView;
    static int j = 0; // 여행기 컨테이너 배열의 인덱스. item이 한번에 두개 들어감
    View[] storyItemView = new View[50];
    ImageView[] storyImageView = new ImageView[50]; // 여행기 대표 이미지
    ImageView[] storyUserImage = new ImageView[50]; // 여행기 게시자 프로필 사진
    TextView[] storyUserName = new TextView[50]; // 여행기 작성자
    TextView[] storyTitle = new TextView[50]; // 여행기 제목
    LinearLayout[] storyBackgroundLayout = new LinearLayout[50]; // 여행기 프로필과 작성자, 제목의 배경이 되는 레이아웃
    LinearLayout[] storyUserNameTitleLayout = new LinearLayout[50]; // 작성자, 제목을 감싸는 레이아웃

    LinearLayout mainStoryContainer; // 여행기 메인 프래그먼트 화면 레이아웃
    LinearLayout[] storyContainer = new LinearLayout[50]; // 여행기 두 개씩 감싸고 있는 레이아웃

    boolean oddCheck = false; // oddCheck가 true면 게시글 개수가 홀수. 그러니까 마지막 게시글은 하나만 띄워야한다.

    //****서버 코드 *****/
    final static int REQUEST_PRESENTATION_IMAGE = 1111, REQUEST_USER_IMAGE = 2222;
    int seq;
    int user_info_seq;
    String presentation_image;
    String title;
    String name;
    String profile_image;
    long start_date_client, finish_date_client;
    int storyDayTotal = 0; // storyDayTotal : 그 날의 여행기 게시글 개수

    ArrayList <String> presentation_img_List = new ArrayList <String>();
    ArrayList <String> profile_img_List = new ArrayList <String> ();
    boolean uploading_check = false;

    Context context;
    int start_num = 0;
    final static int STORYLIST_ONECOUNT = 6;
    boolean data_flag = false;

    long start_time = 0, finish_time = 0;

    static String cate="-1", cateDetail="-1" ,sDate="-1", eDate="-1";

    public void search_location( String cate, String cateDetail ,String sDate, String eDate){
        this.cate = cate;
        this.cateDetail = cateDetail;
        this.sDate = sDate;
        this.eDate = eDate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
       // getTravelRecordAll_Server();
        super.onActivityCreated(savedInstanceState);
        //Log.d("FRAG_ACIT","3");
        Log.d("FRAG_ACIT",  this.cate + " , " +   this.cateDetail + " , " + this.sDate + " , " + this.eDate );
            if(cate.equals("0") || cate.equals("2")) {
                if (cateDetail.equals("0"))// 내 근처
                {
                    Log.d("FRAG_ACIT", "내 근처");

                    RequestParams params = new RequestParams();
                    params.put("longitude", Double.parseDouble(sDate));
                    params.put("latitude", Double.parseDouble(eDate));
                    params.put("distenceFlag", true);

                    getTravelRecordAll_Server(params);
                } else if (cateDetail.equals("1"))// 기간
                {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");

                    Date start_date = null, finish_date =  null;
                    try{
                        start_date = df.parse(sDate);
                        start_time = start_date.getTime();

                        finish_date = df.parse(eDate);
                        finish_time = finish_date.getTime();
                        Log.d("FRAG_ACIT", "기간 : " +  start_time + " , " + finish_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    RequestParams params = new RequestParams();
                    params.put("start_date_client", start_time);
                    params.put("finish_date_client", finish_time);

                    getTravelRecordAll_Server(params);
                }
            }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Log.d("FRAG_ACIT","1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Log.d("FRAG_ACIT","2");
        context = getContext();

        v = (ViewGroup) inflater.inflate(R.layout.fragment_story_fragment, container, false);
        mainStoryContainer = (LinearLayout) v.findViewById(R.id.mainStoryContainer);
        storyFragmentScrollView = (ScrollView) v.findViewById(R.id.storyFragmentScrollView);

        scrollViewBottomObserver();

        //글쓰기 버튼 동작 코드
        FloatingActionButton writeButton = (FloatingActionButton) v.findViewById(R.id.writeButton);
        writeButton.setVisibility(View.INVISIBLE);

        return v;
    }

    public void scrollViewBottomObserver() {
        storyFragmentScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged()
            {
                int scrollViewPos = storyFragmentScrollView.getScrollY();
                int TextView_lines = storyFragmentScrollView.getChildAt(0).getBottom() - storyFragmentScrollView.getHeight();
                if (TextView_lines == scrollViewPos && TextView_lines != 0)
                {
                    if (!data_flag && !uploading_check)
                    {
                        Log.d("scrollBottomOb", "scrollViewBottomObserver check");

                        RequestParams params = new RequestParams();
                        params.put("start_date_client", start_time);
                        params.put("finish_date_client", finish_time);
                        getTravelRecordAll_Server(params);
                    }
                }
            }
        });
    }

    /*****************
     * story 전체 데이터
     **********************/

    void getTravelRecordAll_Server(RequestParams params ) {

        //RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("start_num", start_num);
        params.put("number", STORYLIST_ONECOUNT);
       // params.put("start_date_client", start_time);
       // params.put("finish_date_client", finish_time);

        //Log.d("FRAG_ACIT", "getTravleRecordAll_Server()");
        client.get("http://kibox327.cafe24.com/getTravelRecordList.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("FRAG_ACIT", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                presentation_img_List.clear();
                profile_img_List.clear();
                uploading_check = true;
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("travels") + "";
                    JSONArray arr = new JSONArray(objStr);
                    storyDayTotal = arr.length();
                    if (storyDayTotal <= 0) {
                        data_flag = true;
                    }

                    if (storyDayTotal % 2 == 1)
                        oddCheck = true;

                    for (int i = 0; i < storyDayTotal; i++) {
                        JSONObject obj = (JSONObject) arr.get(i);

                        seq = (Integer) obj.get("seq");
                        Log.d("SEQ", String.valueOf(seq));
                        presentation_image = (String) obj.get("presentation_image");
                        presentation_img_List.add(presentation_image);
                        title = (String) obj.get("title");
                        start_date_client = (long) obj.get("start_date_client");
                        finish_date_client = (long) obj.get("finish_date_client");
                        JSONObject obj2 = (JSONObject) obj.get("userinfo");
                        name = (String) obj2.get("name");
                        profile_image = (String) obj2.get("profile_image");
                        profile_img_List.add(profile_image);
                       // Log.d("FRAG_ACIT",start_date_client + " , " +finish_date_client);
                        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

                        // 올릴 여행기 아이템 하나를 inflate합니다.
                        storyItemView[i] = inflater.inflate(R.layout.story_main_item, v, false);

                        // Id를 받아옵니다. 버튼으로 사용하는 것들 1)storyImageView 2)storyImageButton 3)storyUserImage
                        storyImageView[i] = (ImageView) storyItemView[i].findViewById(R.id.storyImageView); // 클릭시 여행기 본문
                        storyBackgroundLayout[i] = (LinearLayout) storyItemView[i].findViewById(R.id.storyBackgroundLayout);
                        storyUserImage[i] = (ImageView) storyItemView[i].findViewById(R.id.storyUserImage); // 클릭시 트레블러 프로필
                        storyUserNameTitleLayout[i] = (LinearLayout) storyItemView[i].findViewById(R.id.storyUserNameTitleLayout);
                        storyUserName[i] = (TextView) storyItemView[i].findViewById(R.id.storyUserName);
                        storyTitle[i] = (TextView) storyItemView[i].findViewById(R.id.storyTitle);
                        TextView storyDateItem = (TextView) storyItemView[i].findViewById(R.id.storyDate);
                        storyImageView[i].setTag(seq);

                        //서버에서 받아온 name, title로 수정
                        storyUserName[i].setText(name);
                        storyTitle[i].setText(title);

                        //날짜 바꾸기
                        Date start_date = new Date(start_date_client);
                        Date finish_date = new Date(finish_date_client);
                        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");
                        storyDateItem.setText(format.format(start_date) + "~" + format.format(finish_date));

                        //***서버에서 정보를 받아오는 코드를 작성하고, StoryReadActivity로 전달한다.***
                        storyImageView[i].setOnClickListener(new ImageView.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int storySeq = (int) v.getTag();
                                Intent intent = new Intent(getActivity(), StoryReadActivity.class);
                                intent.putExtra("seq", storySeq);
                                Log.d("seq", String.valueOf(storySeq));
                                startActivity(intent);
                            }
                        });

                        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

                        if (i % 2 == 0) {
                            // 왼쪽 여행기의 경우입니다. 두 아이템 여행기를 감싸줄 storyContainer 레이아웃을 동적으로 생성하고 속성을 지정합니다.
                            itemParams.rightMargin = 2;
                            itemParams.leftMargin = 4;
                            storyContainer[j] = new LinearLayout(getActivity().getApplicationContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.bottomMargin = 15;
                            storyContainer[j].setLayoutParams(params);
                        } else {
                            // 오른쪽 여행기의 경우입니다. 앞서 왼쪽 여행기에서 필요한 레이아웃 들을 동적으로 생성해줘서 딱히 해줄 과정은 없고 속성만 지정해줍니다.
                            itemParams.rightMargin = 4;
                            itemParams.leftMargin = 2;
                        }

                        // 속성을 지정해주고, 앞서 만들어두었던 storyContainer에 addView 해줍니다.
                        storyItemView[i].setLayoutParams(itemParams);
                        storyContainer[j].addView(storyItemView[i]);

                        if (i % 2 == 1) // i가 홀수 일때만 들어온다.
                        {
                            mainStoryContainer.addView(storyContainer[j]);
                            j++;
                        } else if (oddCheck && i == storyDayTotal - 1) // i가 짝수이고, oddCheck가 true, i가 마지막 index일 경우
                        {
                            // i가 짝수이고, 여행기 게시글 개수가 odd(홀수)이고, i의 값이 storyDayTotal-1 인 경우(즉, for문에서 마지막으로 돌때) 경우 입니다.
                            View invisibleBlock = new View(getActivity().getApplicationContext());

                            // 레이아웃 비율을 맞춰줘야하니까 속성 그대로 맞춰주고
                            LinearLayout.LayoutParams invisibleItemparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                            invisibleBlock.setLayoutParams(invisibleItemparams);

                            // addView 해줍니다.
                            storyContainer[j].addView(invisibleBlock);
                            mainStoryContainer.addView(storyContainer[j]);
                            j++;
                        }
                    }

                    for(int i = 0; i < storyDayTotal; i++)
                    {
                        ImageLoadingTask task = new ImageLoadingTask();
                        task.execute(i);
                    }

                    start_num += STORYLIST_ONECOUNT;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("FRAG_ACIT", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray, final int index) {  // byte -> bitmap 변환 및 반환
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

    private int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    private int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private class PresentationAndProfile
    {
        Bitmap presentationImage;
        Bitmap profileImage;
        int index;

        public PresentationAndProfile()
        {
        }

        public PresentationAndProfile(Bitmap presentationImage, Bitmap profileImage, int index)
        {
            this.presentationImage = presentationImage;
            this.profileImage = profileImage;
            this.index = index;
        }

        public Bitmap getPresentationImage()
        {
            return presentationImage;
        }

        public void setPresentationImage(Bitmap presentationImage)
        {
            this.presentationImage = presentationImage;
        }

        public Bitmap getProfileImage()
        {
            return profileImage;
        }

        public void setProfileImage(Bitmap profileImage)
        {
            this.profileImage = profileImage;
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

    PresentationAndProfile image = null;
    private class ImageLoadingTask extends AsyncTask <Integer, Void, PresentationAndProfile>
    {
        @Override
        protected void onPostExecute(PresentationAndProfile imagePara)
        {
            if(imagePara != null)
            {
                storyImageView[imagePara.getIndex()].setImageBitmap(imagePara.getPresentationImage());
                storyUserImage[imagePara.getIndex()].setImageBitmap(imagePara.getProfileImage());
                Log.d("TAK", "이미지 적용 끝 : " + String.valueOf(imagePara.getIndex()));
            }
            if(imagePara != null && imagePara.getIndex() == 5)
            {
                uploading_check = false;
            }

            image = null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected PresentationAndProfile doInBackground(Integer... params)
        {
            Bitmap presentation_bitmap = null;
            Bitmap profile_bitmap = null;

            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            //******리사이즈 코드*******
            int presentationImgViewWidth = width / 2;
            int presentationImgViewHeight = presentationImgViewWidth;
            int profileImgViewWidth = dpToPx(context, 50);
            int profileImgViewHeight = dpToPx(context, 50);

            try
            {
                URL PresentationUrl = new URL("http://kibox327.cafe24.com/Image.do?imageName=" + presentation_img_List.get(params[0]) + "&resize=true&width="
                        + presentationImgViewWidth + "&height=" + presentationImgViewHeight);
                HttpURLConnection conn1 = (HttpURLConnection) PresentationUrl.openConnection();
                conn1.setDoInput(true);
                conn1.connect();

                InputStream is1 = conn1.getInputStream();

                presentation_bitmap = BitmapFactory.decodeStream(is1);

                URL ProfileUrl = new URL("http://kibox327.cafe24.com/Image.do?imageName=" + profile_img_List.get(params[0]) + "&resize=true&width="
                        + profileImgViewWidth + "&height=" + profileImgViewHeight);
                HttpURLConnection conn2 = (HttpURLConnection) ProfileUrl.openConnection();
                conn2.setDoInput(true);
                conn2.connect();

                InputStream is2 = conn2.getInputStream();

                profile_bitmap = BitmapFactory.decodeStream(is2);

                image = new PresentationAndProfile(presentation_bitmap, profile_bitmap, params[0]);
                Log.d("TAK", "이미지 로딩 끝 : " + String.valueOf(params[0]));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            return image;
        }
    }

    void getImage_Server(String imageName, final int RequestCode, final int index) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //******리사이즈 코드*******
        int presentationImgViewWidth = width / 2;
        int presentationImgViewHeight = presentationImgViewWidth;
        int profileImgViewWidth = dpToPx(context, 50);
        int profileImgViewHeight = dpToPx(context, 50);

        RequestParams params = new RequestParams();
        // 보내는 data는 imageName 만 있으면 됩니다.
        params.put("imageName", imageName);
        params.put("resize", true);
        if (RequestCode == REQUEST_PRESENTATION_IMAGE) {
            params.put("width", String.valueOf(presentationImgViewWidth));
            params.put("height", String.valueOf(presentationImgViewHeight));
        } else if (RequestCode == REQUEST_USER_IMAGE) {
            params.put("width", String.valueOf(profileImgViewWidth));
            params.put("height", String.valueOf(profileImgViewHeight));
        }

        params.setHttpEntityIsRepeatable(true);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getImage_Server()");
        client.get("http://kibox327.cafe24.com/Image.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // byteArrayToBitmap 를 통해 reponse로 받은 이미지 데이터 bitmap으로 변환
                Bitmap bitmap = byteArrayToBitmap(response, index);

                if (RequestCode == REQUEST_PRESENTATION_IMAGE) {
                    storyImageView[index].setImageBitmap(bitmap);
                } else if (RequestCode == REQUEST_USER_IMAGE) {
                    storyUserImage[index].setImageBitmap(bitmap);
                }

                Log.d("getImage_Server", "statusCode : " + statusCode + " , response : " + new String(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Log.d("getImage_Server", "onFailure // statusCode : " + statusCode + ", headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }
}