package com.example.user.travel360.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.travel360.R;
import com.example.user.travel360.StoryReadActivity;
import com.example.user.travel360.StoryWriteActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Story_fragment extends Fragment {
    ViewGroup v;

    int storyDayTotal=0; // storyDayTotal : 그 날의 여행기 게시글 개수
    static int j=0; // 여행기 컨테이너 배열의 인덱스. item이 한번에 두개 들어감
    View[] storyItemView = new View[100];
    ImageView[] storyImageView = new ImageView[100]; // 여행기 대표 이미지
    Button[] storyImageButton = new Button[100]; // 대표 이미지 오른쪽 하단 작은 버튼
    ImageView[] storyUserImage = new ImageView[100]; // 여행기 게시자 프로필 사진
    TextView[] storyUserName = new TextView[100]; // 여행기 작성자
    TextView[] storyTitle = new TextView[100]; // 여행기 제목
    LinearLayout[] storyBackgroundLayout = new LinearLayout[100]; // 여행기 프로필과 작성자, 제목의 배경이 되는 레이아웃
    LinearLayout[] storyUserNameTitleLayout = new LinearLayout[100]; // 작성자, 제목을 감싸는 레이아웃

    LinearLayout mainStoryContainer; // 여행기 메인 프래그먼트 화면 레이아웃
    LinearLayout[] storyContainer = new LinearLayout[100]; // 여행기 두 개씩 감싸고 있는 레이아웃

    boolean oddCheck = false; // oddCheck가 true면 게시글 개수가 홀수. 그러니까 마지막 게시글은 하나만 띄워야한다.

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
        writeButton.setOnClickListener(new Button.OnClickListener() {
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
        storyDayTotal = 5; // 서버에서 데이터를 가져왔다고 가정. 0은 오늘. storyDayTotal은 오늘 올라갈 여행기 게시글 수. 5개

        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

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

            // Id를 받아옵니다. 버튼으로 사용하는 것들 1)storyImageView 2)storyImageButton 3)storyUserImage
            storyImageView[i] = (ImageView)storyItemView[i].findViewById(R.id.storyImageView); // 클릭시 여행기 본문
            storyImageButton[i] = (Button) storyItemView[i].findViewById(R.id.storyImageButton); // 클릭시 간략화된 여행기 정보
            storyBackgroundLayout[i] = (LinearLayout) storyItemView[i].findViewById(R.id.storyBackgroundLayout);
            storyUserImage[i] = (ImageButton) storyItemView[i].findViewById(R.id.storyUserImage); // 클릭시 트레블러 프로필
            storyUserNameTitleLayout[i] = (LinearLayout) storyItemView[i].findViewById(R.id.storyUserNameTitleLayout);
            storyUserName[i] = (TextView) storyItemView[i].findViewById(R.id.storyUserName);
            storyTitle[i] = (TextView) storyItemView[i].findViewById(R.id.storyTitle);

            //******************이 부분에서 서버에서 받아온 정보로 바꿔줍니다.******************************************
            // 현재는 그냥 일괄적으로 동일한 정보로 해줬습니다.
            storyImageView[i].setImageResource(R.drawable.testimg1);
            storyUserName[i].setText("전성일");
            storyTitle[i].setText("파오후쿰척쿰척");

            storyImageView[i].setOnClickListener(new ImageView.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //***서버에서 정보를 받아오는 코드를 작성하고, StoryReadActivity로 전달한다.***
                    startActivity(new Intent(getActivity(), StoryReadActivity.class));
                }
            });
            //******************************************************************************************************

            // 여행기 아이템 하나의 레이아웃 속성을 지정합니다. 1번째 para : width 2번재 para : height 3번째 para : layout_weight
            // 참고로 layout_weight는 레이아웃의 비율입니다. 1:1 반반씩 가지게 만들려고 1로 줍니다.
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

            if(i % 2 == 0)
            {
                // 왼쪽 여행기의 경우입니다. 두 아이템 여행기를 감싸줄 storyContainer 레이아웃을 동적으로 생성하고 속성을 지정합니다.
                itemParams.rightMargin = 2;
                itemParams.leftMargin = 4;
                storyContainer[j] = new LinearLayout(getActivity().getApplicationContext());
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

        super.onActivityCreated(savedInstanceState);
    }

    public void init(){


    }


}
