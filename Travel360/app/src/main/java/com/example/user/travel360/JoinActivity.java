package com.example.user.travel360;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.util.regex.Pattern;

//import retrofit.Call;
//import retrofit.Callback;
//import retrofit.Response;
//import retrofit.Retrofit;

public class JoinActivity extends Activity {
    String gender, email, pw, name;
    Button BtnSumit;
    EditText EditEmail, EditPW, EditName, EditBirth;
    RadioGroup radioGroup;
    SharedPreferences pref;
    SharedPreferences.Editor edit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();
        initSharedPre();

        onClickBtn();

    }

    public void init() {

        BtnSumit = (Button) findViewById(R.id.btnSubmit);
        EditEmail = (EditText) findViewById(R.id.editEmail);
        EditPW = (EditText) findViewById(R.id.editPw);
        EditName = (EditText) findViewById(R.id.editName);
        EditBirth = (EditText) findViewById(R.id.editBirth);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

    }

    public void onClickBtn() {





        BtnSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gender = checkRadioBtn();
                email = EditEmail.getText().toString();
                pw = EditPW.getText().toString();
                name = EditName.getText().toString();

                if (!checkEditBoxInput()) {
                    return;
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                edit.putString("email", email);
                edit.putString("pw", pw);
                edit.putString("gender", gender);
                edit.commit();
                ApplicationController.getInstance().initSharedPreference();
                //    Toast.makeText(getApplicationContext(), ApplicationController.getInstance().getEmail() + "/" +ApplicationController.getInstance().getGender() + "/" + ApplicationController.getInstance().getPw(), Toast.LENGTH_SHORT).show();



            }
        });

    }

    private String checkRadioBtn() {
        int id = radioGroup.getCheckedRadioButtonId();
        RadioButton radioBtn = (RadioButton) findViewById(id);

        return radioBtn.getText().toString();
        /*
        switch (id) {
            case R.id.radioBtnMale:
                return radioBtn.getText().toString();
                break;
            case R.id.radioBtnFemail:
                return radioBtn.getText().toString();
                break;
        }
        */
    }
    private void initSharedPre() {
        //SharedPreferences 초기화
        pref = getSharedPreferences("login", 0);
        edit = pref.edit();
    }
    private boolean checkEditBoxInput() {
        if (TextUtils.isEmpty(EditEmail.getText().toString())) {
            Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(EditName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(EditPW.getText().toString())) {
            Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(EditBirth.getText().toString())) {
            Toast.makeText(getApplicationContext(), "생일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
