package com.example.user.travel360.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.R;


public class Main_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    View v;
    String string_story_text1, string_story_text2, string_traveler_text1, string_traveler_text2, string_place_text1, string_place_text2;

    TextView Reco_story_text1, Reco_story_text2, Reco_traveler_text1, Reco_traveler_text2, Reco_place_text1, Reco_place_text2;
    ImageView Reco_traveler_img1, Reco_traveler_img2;
    Button Reco_story_btn1, Reco_story_btn2, Reco_place_btn1, Reco_place_btn2;
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main_fragment, container, false);
        init();
        return v;
    }

    public void init() {
        //textView 선언
        Reco_story_text1 = (TextView) v.findViewById(R.id.reco_storytext1);
        Reco_story_text2 = (TextView) v.findViewById(R.id.reco_storytext2);
        Reco_traveler_text1 = (TextView) v.findViewById(R.id.reco_travelertext1);
        Reco_traveler_text2 = (TextView) v.findViewById(R.id.reco_travelertext2);
        Reco_place_text1 = (TextView) v.findViewById(R.id.reco_placeText1);
        Reco_place_text2 = (TextView) v.findViewById(R.id.reco_placeText2);

        //button 선언
        Reco_story_btn1 = (Button) v.findViewById(R.id.reco_storybtn1);
        Reco_story_btn2 = (Button) v.findViewById(R.id.reco_storybtn2);
        Reco_place_btn1 = (Button) v.findViewById(R.id.reco_placebtn1);
        Reco_place_btn2 = (Button) v.findViewById(R.id.reco_placebtn2);

        //image 선언
        Reco_traveler_img1 = (ImageView)v.findViewById(R.id.reco_travelerImg1);
        Reco_traveler_img2 = (ImageView)v.findViewById(R.id.reco_travelerImg2);

    }

    /*
    * readText(), readImg() 는
    * 서버로부터 "오늘의 추천 여행기", "추천 여행가","추천 여행기"를 얻어와서 뿌리는 함수!
    * */
    public void readText(){

    }
    public void readImg(){


    }


}