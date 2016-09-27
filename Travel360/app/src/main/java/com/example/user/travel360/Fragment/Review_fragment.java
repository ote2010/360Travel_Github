package com.example.user.travel360.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.travel360.R;
import com.example.user.travel360.Review.ReviewMainReadActivity;


public class Review_fragment extends Fragment {
    ViewGroup v;

    LinearLayout mainReviewContainer; // 추가할 리뷰 프레그먼트 레이아웃
    ImageView[] reviewMainImage = new ImageView[10]; // 리뷰 순위권 메인 이미지 뷰 - 버튼 동작 설정해야함
    Button[] reviewImageButton = new Button[10]; // 리뷰의 간략화 정보 버튼 - 버튼 동작 설정해야함
    ImageView[] medalIcon = new ImageView[10]; // 메달 아이콘 이미지뷰
    TextView[] rankPlaceTextView = new TextView[10]; // 순위권 여행지 리뷰의 장소 텍스트뷰
    TextView[] rankEvaluationTextView = new TextView[10]; // 순위권 장소의 별점 텍스트뷰
    View[] rankItem = new View[10]; // 순위권 여행지 리뷰의 뷰

    ListView listView; // 순위권 밖의 리스트 뷰
    ReviewAdapter adapter; // 리스트 뷰를 위한 어댑터

    //****순위권 밖 리스트 아이템을 위한 문자열 배열. 서버에서 받아온다고 가정******
    String [] names = {"Soongsil Univ", "Anyang", "Uiwang", "Gwachun"};
    String [] ages = {"2.6", "1.9", "1.8", "1.7"};
    //*************************************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = (ViewGroup)inflater.inflate(R.layout.fragment_review_fragment, container, false);
        mainReviewContainer = (LinearLayout)v.findViewById(R.id.mainReviewContainer);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());

        for(int i=0; i<3; i++)
        {
            rankItem[i] = inflater.inflate(R.layout.review_main_rankitem, v, false); // 추가할 순위권 여행지 뷰 inflate

            // 메달 아이콘, 여행지 장소 텍스트, 여행지 별점 ID 불러오기
            medalIcon[i] = (ImageView) rankItem[i].findViewById(R.id.medalIcon);
            rankPlaceTextView[i] = (TextView) rankItem[i].findViewById(R.id.rankPlaceTextview);
            rankEvaluationTextView[i] = (TextView) rankItem[i].findViewById(R.id.rankEvaluationTextview);
            reviewMainImage[i] = (ImageView) rankItem[i].findViewById(R.id.reviewMainImage);
            reviewImageButton[i] = (Button) rankItem[i].findViewById(R.id.reviewImageButton);

            // i가 0이면 1등, 1이면 2등, 2이면 3등
            if(i==0)
            {
                medalIcon[i].setImageResource(R.drawable.goldmedal);
            }
            else if(i==1)
            {
                medalIcon[i].setImageResource(R.drawable.silvermedal);
            }
            else if(i==2)
            {
                medalIcon[i].setImageResource(R.drawable.blonzemedal);
            }

            //***********이 부분에서 서버에서 받아와서 바꿔주면 된다!!!!!***********
            reviewMainImage[i].setImageResource(R.drawable.testimg1);
            rankPlaceTextView[i].setText("Sangdodong");
            rankEvaluationTextView[i].setText("4.8");

            reviewMainImage[i].setOnClickListener(new ImageView.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // ***이 부분에서 서버에게 정보를 요청하는 코드가 필요함***
                    startActivity(new Intent(getActivity(), ReviewMainReadActivity.class));
                }
            });
            //*******************************************************************

            mainReviewContainer.addView(rankItem[i]); // 추가해주기
        }

        // 리스트 뷰 설정
        listView = new ListView(getActivity().getApplicationContext());

        // **이 부분에서 리스트뷰가 일부분만 보이는 문제를 해결하기 위해 리스트 뷰의 높이를 임의로 설정해준다**
        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 1000, 1);
        listView.setLayoutParams(params);

        adapter= new ReviewAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // ***이 부분에서 서버에게 정보를 요청하는 코드가 필요함***
                startActivity(new Intent(getActivity(), ReviewMainReadActivity.class));
            }
        });

        mainReviewContainer.addView(listView);

        super.onActivityCreated(savedInstanceState);
    }

    // 리스트 뷰에 적용되는 어댑터 클래스. 이부분에서 리스트 아이템을 적용해준다.
    class ReviewAdapter extends BaseAdapter
    {
        @Override
        public int getCount() // 리스트뷰 아이템 개수 가져오기
        {
            return 4;
        }

        @Override
        public Object getItem(int position) // 아이템 가져오기
        {
            return names[position];
        }

        @Override
        public long getItemId(int position) // 아이템 ID 가져오기
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) // 뷰 가져오기
        {
            ReviewListItemView view = new ReviewListItemView(getActivity().getApplicationContext());
            view.setPlace(names[position]);
            view.setEvaluation(ages[position]);
            return view;
        }


    }
}
