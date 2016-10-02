package com.example.user.travel360.Review;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.CustomList.CustomAdapter;
import com.example.user.travel360.CustomList.ItemData;
import com.example.user.travel360.Navigationdrawer.ApplicationController;
import com.example.user.travel360.Navigationdrawer.LoginActivity;
import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReviewReadDialog extends Dialog implements View.OnClickListener {

    // 위젯
    ImageButton add_travler, dialog_close;
    LinearLayout starlayout;
    TextView review_dateTV, user_nameTV, review_evTV, review_contextTV;

    // 넘어오는 데이터
    Context mContext;
    double writeEvl;
    int userSeq;
    String wrteDate, writeText;

    public ReviewReadDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public ReviewReadDialog(Context context, int user_info_seq, String write_date, String text, double evl) {
        super(context);
        this.mContext = context;
        userSeq = user_info_seq;
        wrteDate = write_date;
        writeText = text;
        writeEvl = evl;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());


        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_review_read_dialog);

        WindowManager.LayoutParams lp = getWindow().getAttributes( ) ;
        WindowManager wm = ((WindowManager)mContext.getApplicationContext().getSystemService(mContext.getApplicationContext().WINDOW_SERVICE)) ;
        lp.width =  (int)( wm.getDefaultDisplay().getWidth( ) * 0.95 );
        lp.height =  (int)( wm.getDefaultDisplay().getHeight( ) * 0.8 );

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,25, getContext().getResources().getDisplayMetrics());

        lp.y = px;
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setAttributes( lp ) ;


        init();
        getUserInfo_Server(userSeq);


    }

    public void init() {
        add_travler = (ImageButton) findViewById(R.id.add_travler);
        add_travler.setOnClickListener(this);
        dialog_close = (ImageButton) findViewById(R.id.dialog_close);
        dialog_close.setOnClickListener(this);

        starlayout =  (LinearLayout) findViewById(R.id.starlayout);

        for(int i=0; i<5; i++) // 별표 동적생성
        {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            ImageView review_star = new ImageView(mContext);
            review_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star));
            layout.addView(review_star);
            starlayout.addView(layout);
        }

        review_dateTV = (TextView) findViewById(R.id.review_dateTV);
        user_nameTV = (TextView) findViewById(R.id.user_nameTV);
        review_evTV = (TextView) findViewById(R.id.review_evTV);
        review_contextTV = (TextView) findViewById(R.id.review_contextTV);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_travler:
                Toast.makeText(getContext(), "add travler", Toast.LENGTH_SHORT).show();
                addFriend_Server(1, 3);
                break;

            case R.id.dialog_close:
                dismiss();
                break;
        }
    }


    /**************
     * 친구 (요청)추가 하기
     ***********************/
    void addFriend_Server(int mySeq, int otherSeq) {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq, targetSeq 만 있으면 됩니다.
        params.put("seq", mySeq);
        params.put("targetSeq", otherSeq);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "addFriend_Server()");
        client.get("http://kibox327.cafe24.com/addFriend.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    /************* 사용자 정보 **************/
    void getUserInfo_Server(int seq) {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq 만 있으면 됩니다.
        params.put("seq",seq);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getUserInfo_Server()");
        client.get("http://kibox327.cafe24.com/getUserInfo.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try{
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("userDto") + "";
                    JSONObject obj = new JSONObject(objStr);

                    String id = (String)obj.get("id");
                    String name = (String)obj.get("name");
                    String profile_image = (String)obj.get("profile_image");
                    Log.d("SUN", "profile_image : "+profile_image);

                    review_dateTV.setText(wrteDate);
                    user_nameTV.setText(name);
                    review_evTV .setText(writeEvl+"");
                    review_contextTV .setText(writeText);


                }catch (JSONException e){

                }
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