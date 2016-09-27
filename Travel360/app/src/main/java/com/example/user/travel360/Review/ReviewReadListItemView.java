package com.example.user.travel360.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.travel360.R;

/**
 * Created by yoojeong on 2016-09-02.
 */
public class ReviewReadListItemView extends LinearLayout {

    ImageView Profile, Star;
    TextView Name, Text1, Date, StartNum;

    public ReviewReadListItemView(Context context) {
        super(context);
        init(context);
    }



    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_main_listviewitem, this, true);

        Profile = (ImageView) findViewById(R.id.Review_read_user_img);
        Name = (TextView)findViewById(R.id.Review_read_user_name);

        Text1 = (TextView)findViewById(R.id.Review_read_text);
        Date = (TextView)findViewById(R.id.Review_read_date);

        Star = (ImageView)findViewById(R.id.Review_read_star);
        StartNum = (TextView)findViewById(R.id.Review_read_starnum);

    }

    public void setName(String name) {
        Name.setText(name);
    }
    public void setText(String Text) {
        Text1.setText(Text);
    }
    public void setDate(String date) {
        Date.setText(date);
    }
    public void setStartNum(String num) {
        StartNum.setText(num);
    }
    public void setProfile(String name) {

    }
    public void setStar(String name) {

    }
}
