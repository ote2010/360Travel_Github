package com.example.user.travel360;

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

public class JoinActivity extends AppCompatActivity {
    String gender, email, pw, name;
    Button BtnSumit;
    EditText EditEmail, EditPW, EditName, EditBirth;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();
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

        gender = checkRadioBtn();
        email = EditBirth.getText().toString();
        pw = EditPW.getText().toString();
        name = EditName.getText().toString();

        BtnSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (email.equals("") || pw.equals("")
                        ||name.equals("") || EditBirth.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "정보를 다 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else {

                    ApplicationController.getInstance().setEmail(email);
                    ApplicationController.getInstance().setPw(pw);
                    ApplicationController.getInstance().setGender(gender);
                    finish();

                }*/
                ApplicationController.getInstance().setEmail(email);
                ApplicationController.getInstance().setPw(pw);
                ApplicationController.getInstance().setGender(gender);
                finish();
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

}
