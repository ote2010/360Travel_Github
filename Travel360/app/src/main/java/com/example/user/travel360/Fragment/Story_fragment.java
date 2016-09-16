package com.example.user.travel360.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.travel360.R;
import com.example.user.travel360.Story.StoryReadActivity;
import com.example.user.travel360.Story.StoryWriteActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class Story_fragment extends Fragment {
    ViewGroup v;


    static int j=0; // 여행기 컨테이너 배열의 인덱스. item이 한번에 두개 들어감
    View[] storyItemView = new View[50];
    ImageView[] storyImageView = new ImageView[50]; // 여행기 대표 이미지
    Button[] storyImageButton = new Button[50]; // 대표 이미지 오른쪽 하단 작은 버튼
    ImageView[] storyUserImage = new ImageView[50]; // 여행기 게시자 프로필 사진
    TextView[] storyUserName = new TextView[50]; // 여행기 작성자
    TextView[] storyTitle = new TextView[50]; // 여행기 제목
    ArrayList<TextView> storyDateList = new ArrayList <TextView> ();
    LinearLayout[] storyBackgroundLayout = new LinearLayout[50]; // 여행기 프로필과 작성자, 제목의 배경이 되는 레이아웃
    LinearLayout[] storyUserNameTitleLayout = new LinearLayout[50]; // 작성자, 제목을 감싸는 레이아웃

    LinearLayout mainStoryContainer; // 여행기 메인 프래그먼트 화면 레이아웃
    LinearLayout[] storyContainer = new LinearLayout[50]; // 여행기 두 개씩 감싸고 있는 레이아웃

    boolean oddCheck = false; // oddCheck가 true면 게시글 개수가 홀수. 그러니까 마지막 게시글은 하나만 띄워야한다.

    //****서버 코드 *****/
    final static int REQUEST_PRESENTATION_IMAGE = 1111, REQUEST_USER_IMAGE = 2222;
    static int seq ;
    static int user_info_seq;
    static String presentation_image;
    static String title;
    static String name;
    static String profile_image;
    static int storyDayTotal = 0; // storyDayTotal : 그 날의 여행기 게시글 개수

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v = (ViewGroup) inflater.inflate(R.layout.fragment_story_fragment, container, false);
        mainStoryContainer = (LinearLayout)v.findViewById(R.id.mainStoryContainer);

        //글쓰기 버튼 동작 코드
        Button writeButton = (Button)v.findViewById(R.id.writeButton);
        writeButton.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                //Toast.makeText(getActivity(),"ㅎㅇ",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), StoryWriteActivity.class));
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getTravleRecordAll_Server();

        super.onActivityCreated(savedInstanceState);
    }

    /*****************  story 전체 데이터  **********************/

    void getTravleRecordAll_Server() {

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "getTravleRecordAll_Server()");
        client.get("http://kibox327.cafe24.com/getTravelRecordList.do", new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response)
            {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                try
                {
                    JSONObject object = new JSONObject(res);
                    String objStr = object.get("travels") + "";
                    JSONArray arr = new JSONArray(objStr);
                    storyDayTotal = arr.length();

                    if (storyDayTotal % 2 == 1)
                        oddCheck = true;

                    for (int i = 0; i < storyDayTotal; i++)
                    {
                        JSONObject obj = (JSONObject) arr.get(i);

                        seq = (Integer) obj.get("seq");
                        presentation_image = (String) obj.get("presentation_image");
                        title = (String) obj.get("title");

                        JSONObject obj2 = (JSONObject) obj.get("userinfo");
                        name = (String) obj2.get("name");
                        profile_image = (String) obj2.get("profile_image");

                        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

                        // 올릴 여행기 아이템 하나를 inflate합니다.
                        storyItemView[i] = inflater.inflate(R.layout.story_main_item, v, false);

                        // Id를 받아옵니다. 버튼으로 사용하는 것들 1)storyImageView 2)storyImageButton 3)storyUserImage
                        storyImageView[i] = (ImageView) storyItemView[i].findViewById(R.id.storyImageView); // 클릭시 여행기 본문
                        storyImageButton[i] = (Button) storyItemView[i].findViewById(R.id.storyImageButton); // 클릭시 간략화된 여행기 정보
                        storyBackgroundLayout[i] = (LinearLayout) storyItemView[i].findViewById(R.id.storyBackgroundLayout);
                        storyUserImage[i] = (ImageButton) storyItemView[i].findViewById(R.id.storyUserImage); // 클릭시 트레블러 프로필
                        storyUserNameTitleLayout[i] = (LinearLayout) storyItemView[i].findViewById(R.id.storyUserNameTitleLayout);
                        storyUserName[i] = (TextView) storyItemView[i].findViewById(R.id.storyUserName);
                        storyTitle[i] = (TextView) storyItemView[i].findViewById(R.id.storyTitle);
                        TextView storyDateItem = (TextView) storyItemView[i].findViewById(R.id.storyDate);
                        storyDateList.add(storyDateItem);

                        //******서버에서 받아온 정보로 수정 **********
                        //storyImageView[i].setImageResource(R.drawable.testimg1);
                        getImage_Server(presentation_image, REQUEST_PRESENTATION_IMAGE, i);
                        getImage_Server(profile_image, REQUEST_USER_IMAGE, i);
                        storyUserName[i].setText(name);
                        storyTitle[i].setText(title);

                        storyImageView[i].setOnClickListener(new ImageView.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                //***서버에서 정보를 받아오는 코드를 작성하고, StoryReadActivity로 전달한다.***
                                startActivity(new Intent(getActivity(), StoryReadActivity.class));
                            }
                        });

                        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

                        if (i % 2 == 0)
                        {
                            // 왼쪽 여행기의 경우입니다. 두 아이템 여행기를 감싸줄 storyContainer 레이아웃을 동적으로 생성하고 속성을 지정합니다.
                            itemParams.rightMargin = 2;
                            itemParams.leftMargin = 4;
                            storyContainer[j] = new LinearLayout(getActivity().getApplicationContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.bottomMargin = 15;
                            storyContainer[j].setLayoutParams(params);
                        } else
                        {
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

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Log.d("SUN", "e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo)
            {
            }
        });
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray, final int index) {  // byte -> bitmap 변환 및 반환
        //리사이즈 코드
        int targetW = storyImageView[index].getWidth();
        int targetH = storyImageView[index].getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        int photoW  = options.outHeight;
        int photoH  = options.outWidth;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        return bitmap;
    }

    void getImage_Server(String imageName, final int RequestCode, final int index) {

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
                Bitmap bitmap = byteArrayToBitmap(response, index);

                if(RequestCode == REQUEST_PRESENTATION_IMAGE)
                {
                    storyImageView[index].setImageBitmap(bitmap);
                }
                else if(RequestCode == REQUEST_USER_IMAGE)
                {
                    storyUserImage[index].setImageBitmap(bitmap);
                }

                Log.d("getImage_Server", "statusCode : " + statusCode + " , response : " +  new String(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("getImage_Server", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {    }
        });
    }
}
