package com.example.user.travel360.Review;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ReviewWriteActivity extends Dialog {


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
}
