package com.example.user.travel360;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
Toast.makeText(getApplicationContext(),ApplicationController.getInstance().getEmail()+"/"+ApplicationController.getInstance().getPw(),Toast.LENGTH_SHORT).show();
                if(ApplicationController.getInstance().getEmail().equals(email) && ApplicationController.getInstance().getPw().equals(pw)){
                    Toast.makeText(getApplicationContext(),"끼야야야!!!!!!!!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
