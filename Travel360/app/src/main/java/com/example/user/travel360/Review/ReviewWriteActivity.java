package com.example.user.travel360.Review;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.Navigationdrawer.ApplicationController;
import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReviewWriteActivity extends Dialog {
// TODO >> 별점 입력하는 키가 없고 주소가 잘못된듯하다.

    ReviewWriteActivity dialog;
    String ReviewText;
    int Grade_num;

    EditText ReviewWrite;
    Button EnterBtn, CancelBtn;
    Spinner GradeSelect;
    String Grade;
    ImageView PlaceImg;
    ArrayAdapter<CharSequence> grade;
    String Location;
    Context mContext;
    LinearLayout starlayout;
    TextView dateTv;
    public ReviewWriteActivity(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ReviewWriteActivity(getContext());
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);

        // Intent intent=
        // Location = intent.getStringExtra("Location");
        setContentView(R.layout.activity_review_write);

        WindowManager.LayoutParams lp = getWindow().getAttributes( ) ;
        WindowManager wm = ((WindowManager)mContext.getApplicationContext().getSystemService(mContext.getApplicationContext().WINDOW_SERVICE)) ;
        lp.width =  (int)( wm.getDefaultDisplay().getWidth( ) * 0.95 );
        lp.height =  (int)( wm.getDefaultDisplay().getHeight( ) * 0.8 );

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,25, getContext().getResources().getDisplayMetrics());

        lp.y = px;
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setAttributes( lp ) ;
        init();
        onClickEvent();
        SelectGrade();
        //  writeReview_Server();

    }

    public void init() {

        GradeSelect = (Spinner) findViewById(R.id.grade_select);
        ReviewWrite = (EditText) findViewById(R.id.review_write);

        EnterBtn = (Button) findViewById(R.id.enter_btn);
        CancelBtn = (Button) findViewById(R.id.cancel_btn);

        starlayout = (LinearLayout)findViewById(R.id.starlayout);

        dateTv = (TextView)findViewById(R.id.review_write_date);

        long todaydate = System.currentTimeMillis(); // long 형의 현재시간
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        String datestr = df.format(todaydate);

        dateTv.setText(datestr);
    }

    public void onClickEvent() {
        EnterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO >>서버로 텍스트랑 별점, 입력한 사람의 정보(아이디 또는 seq) 보내기
                writeReview_Server();
                dismiss();
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void SelectGrade() {
        grade = ArrayAdapter.createFromResource(getContext(), R.array.grade, android.R.layout.simple_spinner_dropdown_item);
        GradeSelect.setAdapter(grade);
        GradeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Grade = grade.getItem(position) + "";
               // Toast.makeText(getContext(), Grade, Toast.LENGTH_SHORT).show();
                starlayout.removeAllViews();

                int mok = (int)(Double.parseDouble(Grade) / 1.0) ;
                double mod = Double.parseDouble(Grade) % 1.0 ;

                for(int i=0; i<mok; i++) // 별표 동적생성
                {
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    ImageView review_star = new ImageView(mContext);
                    review_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star));
                    layout.addView(review_star);
                    starlayout.addView(layout);
                }
                if(mod > 0){
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    ImageView review_star = new ImageView(mContext);
                    review_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.half_star));
                    layout.addView(review_star);
                    starlayout.addView(layout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void writeReview_Server() {
        long todaydate = System.currentTimeMillis(); // long 형의 현재시간
        RequestParams params = new RequestParams();
        // 기본 데이터
        String WriteText = ReviewWrite.getText().toString();
        String seq = ApplicationController.getInstance().getSeq();
        String text1 = ReviewWrite.getText().toString();
        String Location = ApplicationController.getInstance().getLocation1();
        params.put("userSeq", seq);
        params.put("text", text1);
        params.put("write_date_client", todaydate);
        params.put("location", Location);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStory_Server()");
        client.post("http://kibox327.cafe24.com/writeTravelReview.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN_확인", "Success // statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN@@", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }


}