package com.example.user.travel360.Fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.travel360.R;

/**
 * Created by user on 2016-08-03.
 */
public class ReviewListItemView extends RelativeLayout // 리스트 뷰 아이템을 위한 클래스
{
    TextView placeTextView;
    TextView evaluationTextView;

    public ReviewListItemView(Context context)
    {
        super(context);
        init(context);
    }

    public ReviewListItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_main_listviewitem, this, true);

        placeTextView = (TextView) findViewById(R.id.PlaceTextView);
        evaluationTextView = (TextView) findViewById(R.id.EvaluationTextView);
    }

    public void setPlace(String place)
    {
        placeTextView.setText(place);
    }

    public void setEvaluation(String grade)
    {
        evaluationTextView.setText(grade);
    }
}
