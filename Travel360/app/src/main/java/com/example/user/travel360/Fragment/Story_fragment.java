package com.example.user.travel360.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.travel360.R;


public class Story_fragment extends Fragment {
    View v;

    int day=0, storyDayTotal=0;
    int i=0; // 배열의 인덱스
    ImageView[] storyImageView = new ImageView[100]; // 여행기 대표 이미지
    Button[] storyImageButton = new Button[100]; // 대표 이미지 오른쪽 하단 작은 버튼
    ImageView[] storyUserImage = new ImageView[100]; // 여행기 게시자 프로필 사진
    TextView[] storyUserName = new TextView[100]; // 여행기 작성자
    TextView[] storyTitle = new TextView[100]; // 여행기 제목
    Layout[] storyBackgroundLayout = new Layout[100]; // 여행기 프로필과 작성자, 제목의 배경이 되는 레이아웃

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_story_fragment, container, false);

        day=0; storyDayTotal=5; // day=0 오늘이라고 가정. storyDayTotal은 하루의 게시글로 올라갈 여행기 개수. 5개라고 가정

        //오늘의 날짜에 따라 날짜 텍스트 설정하기
        TextView dayTextView = (TextView)v.findViewById(R.id.dayTextView);

        //만약 게시글이 오늘부터 시작된다면(day==0)
        if(day == 0)
        {
            String today = "2016.07.29";
            dayTextView.setText(today);
        }

        //게시글 포팅하기
        LinearLayout storyContainer = (LinearLayout)v.findViewById(R.id.storyContainer);

        //포팅할 story_main_item.xml를 inflate. 두 개씩 보여줄 것이기 때문에 일단 두개를 inflate.
        View storyItemView1 = (View)inflater.inflate(R.layout.story_main_item, null);
        LinearLayout storyItemLayout1 = (LinearLayout)v.findViewById(R.id.storyItemLayout);
        View storyItemView2 = (View)inflater.inflate(R.layout.story_main_item, null);
        LinearLayout storyItemLayout2 = (LinearLayout)v.findViewById(R.id.storyItemLayout);

        // story_main_item.xml의 뷰 id 속성 가져오기
        ImageView storyImageView = (ImageView)v.findViewById(R.id.storyImageView);
        Button storyImageButton = (Button)v.findViewById(R.id.storyImageButton);
        LinearLayout storyBackgroundLayout = (LinearLayout)v.findViewById(R.id.storyBackgroundLayout);
        ImageButton storyUserImage = (ImageButton)v.findViewById(R.id.storyUserImage);
        LinearLayout storyUserNameTitleLayout = (LinearLayout)v.findViewById(R.id.storyUserNameTitleLayout);
        TextView storyUserName = (TextView)v.findViewById(R.id.storyUserName);
        TextView storyTitle = (TextView)v.findViewById(R.id.storyTitle);

        //레이아웃 속성 지정하기. weight를 모두 1로 줘서 각각이 화면 반반씩 차지하게 구성한다.
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        //미리 정의해두었던 storyContainer에 addView해준다.
        storyContainer.addView(storyItemView1, params1);
        storyContainer.addView(storyItemView2, params2);

        /*
        Button btn = (Button)v.findViewById(R.id.st_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("@@@", "여행기");
            }
        });
        */
        return v;
    }

    public void init(){


    }



}
