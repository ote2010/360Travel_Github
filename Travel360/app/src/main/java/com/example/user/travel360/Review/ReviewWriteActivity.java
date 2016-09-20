package com.example.user.travel360.Review;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

    public ReviewWriteActivity(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ReviewWriteActivity(getContext());
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_review_write);
        init();
        onClickEvent();
        SelectGrade();
    }

    public void init() {

        GradeSelect = (Spinner) findViewById(R.id.grade_select);
        ReviewWrite = (EditText) findViewById(R.id.review_write);

        EnterBtn = (Button) findViewById(R.id.enter_btn);
        CancelBtn = (Button) findViewById(R.id.cancel_btn);
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
                Toast.makeText(getContext(), Grade, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void writeReview_Server() {
        //   long todaydate = System.currentTimeMillis(); // long 형의 현재시간
        RequestParams params = new RequestParams();
        // 기본 데이터
        String WriteText = ReviewWrite.getText().toString();
        params.put("user_seq","3");
        params.put("text", WriteText);
        // params.put("write_date", todaydate);
        //  params.put("location", "korea");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStory_Server()");
        client.get("http://kibox327.cafe24.com/writeTravelReview.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN@@", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Fail", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {        }
        });
    }





}