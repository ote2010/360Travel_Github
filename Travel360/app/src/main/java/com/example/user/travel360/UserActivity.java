package com.example.user.travel360;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class UserActivity extends AppCompatActivity {
    LinearLayout v;

    int storyDayTotal=0; // storyDayTotal : 그 날의 여행기 게시글 개수
    static int i=0; // 여행기 item 배열의 인덱스
    static int j=0; // 여행기 컨테이너 배열의 인덱스. item이 한번에 두개 들어감
    View[] storyItemView = new View[30];
    ImageView[] storyImageView = new ImageView[30]; // 여행기 대표 이미지
    Button[] storyImageButton = new Button[30]; // 대표 이미지 오른쪽 하단 작은 버튼
    ImageView[] storyUserImage = new ImageView[30]; // 여행기 게시자 프로필 사진
    TextView[] storyUserName = new TextView[30]; // 여행기 작성자
    TextView[] storyTitle = new TextView[30]; // 여행기 제목
    LinearLayout[] storyBackgroundLayout = new LinearLayout[30]; // 여행기 프로필과 작성자, 제목의 배경이 되는 레이아웃
    LinearLayout[] storyUserNameTitleLayout = new LinearLayout[30]; // 작성자, 제목을 감싸는 레이아웃

    LinearLayout mainStoryContainer; // 여행기 메인 프래그먼트 화면 레이아웃
    LinearLayout[] storyContainer = new LinearLayout[30]; // 여행기 두 개씩 감싸고 있는 레이아웃

    boolean oddCheck = false; // oddCheck가 true면 게시글 개수가 홀수. 그러니까 마지막 게시글은 하나만 띄워야한다.

    Button addTravlerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.activity_user);

        storyDayTotal = 5; // 서버에서 데이터를 가져왔다고 가정. 0은 오늘. storyDayTotal은 오늘 올라갈 여행기 게시글 수. 5개
        v = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_user, null);
        mainStoryContainer = (LinearLayout)v.findViewById(R.id.userLogLayout);

        addTravlerBtn = (Button)v.findViewById(R.id.addTravlerBtn);
        addTravlerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"add travler",Toast.LENGTH_SHORT).show();
                addFriend_Server(1,4);
            }
        });



        LayoutInflater inflater = getLayoutInflater().from(getApplicationContext());

        // 현재 날짜 라벨도 동적으로 붙여줍니다.
        View timeText = inflater.inflate(R.layout.time_label, v, false);
        TextView timeTextView = (TextView) timeText.findViewById(R.id.timeLabelTextView);

        //현재 날짜를 받아와서 setText해주는 코드입니다.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        Calendar now = Calendar.getInstance();
        String currentDate = formatter.format(now.getTime());
        timeTextView.setText("        " + currentDate);

        // 현재 날짜로 setText된 날짜 라벨을 mainStoryContainer에 addView 해줍니다.
        mainStoryContainer.addView(timeText);

        // 현재 날짜에 올릴 여행기가 홀수개인지 짝수개인지 판별합니다. 홀수개면 마지막에 하나만 띄워줘야합니다.
        if(storyDayTotal % 2 == 1)
            oddCheck = true;

        // 올릴 여행기 개수만큼 for문을 돌립니다.
        for(int i = 0; i < storyDayTotal; i++)
        {
            // 올릴 여행기 아이템 하나를 inflate합니다.
            storyItemView[i] = inflater.inflate(R.layout.story_main_item, v, false);

            // Id를 받아옵니다.
            storyImageView[i] = (ImageView)storyItemView[i].findViewById(R.id.storyImageView);
            storyImageButton[i] = (Button) storyItemView[i].findViewById(R.id.storyImageButton);
            storyBackgroundLayout[i] = (LinearLayout) storyItemView[i].findViewById(R.id.storyBackgroundLayout);
            storyUserImage[i] = (ImageView) storyItemView[i].findViewById(R.id.storyUserImage);
            storyUserNameTitleLayout[i] = (LinearLayout) storyItemView[i].findViewById(R.id.storyUserNameTitleLayout);
            storyUserName[i] = (TextView) storyItemView[i].findViewById(R.id.storyUserName);
            storyTitle[i] = (TextView) storyItemView[i].findViewById(R.id.storyTitle);

            //******************이 부분에서 서버에서 받아온 정보로 바꿔줍니다.******************************************
            // 현재는 그냥 일괄적으로 동일한 정보로 해줬습니다.
            storyImageView[i].setImageResource(R.drawable.testimg1);
            storyUserName[i].setText("전성일");
            storyTitle[i].setText("파오후쿰척쿰척");
            //******************************************************************************************************

            // 여행기 아이템 하나의 레이아웃 속성을 지정합니다. 1번째 para : width 2번재 para : height 3번째 para : layout_weight
            // 참고로 layout_weight는 레이아웃의 비율입니다. 1:1 반반씩 가지게 만들려고 1로 줍니다.
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

            if(i % 2 == 0)
            {
                // 왼쪽 여행기의 경우입니다. 두 아이템 여행기를 감싸줄 storyContainer 레이아웃을 동적으로 생성하고 속성을 지정합니다.
                itemParams.rightMargin = 2;
                itemParams.leftMargin = 4;
                storyContainer[j] = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 15;
                storyContainer[j].setLayoutParams(params);
            }
            else
            {
                // 오른쪽 여행기의 경우입니다. 앞서 왼쪽 여행기에서 필요한 레이아웃 들을 동적으로 생성해줘서 딱히 해줄 과정은 없고 속성만 지정해줍니다.
                itemParams.rightMargin = 4;
                itemParams.leftMargin = 2;
            }

            // 속성을 지정해주고, 앞서 만들어두었던 storyContainer에 addView 해줍니다.
            storyItemView[i].setLayoutParams(itemParams);
            storyContainer[j].addView(storyItemView[i]);

            if(i % 2 == 1) // i가 홀수 일때만 들어온다.
            {
                mainStoryContainer.addView(storyContainer[j]);
                j++;
            }
            else if(oddCheck && i == storyDayTotal-1) // i가 짝수이고, oddCheck가 true, i가 마지막 index일 경우
            {
                // i가 짝수이고, 여행기 게시글 개수가 odd(홀수)이고, i의 값이 storyDayTotal-1 인 경우(즉, for문에서 마지막으로 돌때) 경우 입니다.
                View invisibleBlock = new View(getApplicationContext());

                // 레이아웃 비율을 맞춰줘야하니까 속성 그대로 맞춰주고
                LinearLayout.LayoutParams invisibleItemparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                invisibleBlock.setLayoutParams(invisibleItemparams);

                // addView 해줍니다.
                storyContainer[j].addView(invisibleBlock);
                mainStoryContainer.addView(storyContainer[j]);
                j++;
            }
        }

        setContentView(v);

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


}
