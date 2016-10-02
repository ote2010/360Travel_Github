package com.example.user.travel360.Review;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ReviewReadDialog extends Dialog implements View.OnClickListener {

    Context mContext;
    // String userSeq = ApplicationController.getInstance().getSeq();
    ImageButton add_travler, dialog_close;
   // ImageView review_star;
    LinearLayout starlayout;

    public ReviewReadDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.activity_review_read_dialog);
        //  userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());

        WindowManager.LayoutParams lp = getWindow().getAttributes( ) ;
        WindowManager wm = ((WindowManager)mContext.getApplicationContext().getSystemService(mContext.getApplicationContext().WINDOW_SERVICE)) ;
        lp.width =  (int)( wm.getDefaultDisplay().getWidth( ) * 0.95 );
        lp.height =  (int)( wm.getDefaultDisplay().getHeight( ) * 0.8 );

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,25, getContext().getResources().getDisplayMetrics());

        lp.y = px;
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setAttributes( lp ) ;


        init();
        getTravelReview_Server();
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


    /*****************  review 1개 데이터  **********************/

    void getTravelReview_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 reviewSeq 만 있으면 됩니다.
        params.put("reviewSeq", 1);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getTravleReview_Server()");
        client.get("http://kibox327.cafe24.com/getTravelReview.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {   }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);

                try {
                    JSONObject obj = new JSONObject(res);
                    String objStr =  obj.get("review") + "";
                    JSONObject review = new JSONObject(objStr);
                    String location = (String)review.get("location");
                    String text = (String)review.get("text");
                    // String user = (String)review.get("user");
                    long write_date_client = (long)review.get("write_date_client");

                    Log.d("SUN",  "location : " +location);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SUN",  "e : " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }


            @Override
            public void onRetry(int retryNo) {         }
        });
    }

}