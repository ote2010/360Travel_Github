package com.example.user.travel360;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


//
//import retrofit.Call;
//import retrofit.Callback;
//import retrofit.Response;
//import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {
    Button BtnJoin, BtnLogin;
    EditText EditEmail, EditPW;
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        onClickBtn();
    }


    public void init() {
        BtnJoin = (Button) findViewById(R.id.btnJoin);
        BtnLogin = (Button) findViewById(R.id.btnLogin);
        EditEmail = (EditText) findViewById(R.id.editEmail);
        EditPW = (EditText) findViewById(R.id.editPw);
    }

    public void onClickBtn() {
        BtnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EditEmail.getText().toString();
                String pw = EditPW.getText().toString();

                if (ApplicationController.getInstance().getEmail().equals(email) && ApplicationController.getInstance().getPw().equals(pw)) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();

                    edit.putBoolean("LoginFlag", true);
                    //ApplicationController.getInstance().setLoginFlag(true);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "아이디나 패스워드가 옳지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
