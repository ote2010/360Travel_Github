package com.example.user.travel360.Navigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.travel360.R;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity {
    Button BtnJoin, BtnLogin;
    EditText EditEmail, EditPW;
    ImageView ProfileImg;

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
        ProfileImg = (ImageView) findViewById(R.id.profile1);

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
                final String email = EditEmail.getText().toString();
                String pw = EditPW.getText().toString();
                RequestParams params = new RequestParams();
                params.put("id", email);
                params.put("password", pw);
                AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://kibox327.cafe24.com/login.do", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        Log.d("@@@", "111");
                        // called before request is started
                        //Toast.makeText(getApplicationContext(), "START!", Toast.LENGTH_SHORT).show();


                    }


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        //   Toast.makeText(getApplicationContext(), new String(response), Toast.LENGTH_LONG).show();
                        //   Log.d("seq&&", JSONP(new String(response)));
                        Log.d("SUN@@", "success");
                        String seq = JSONP(new String(response));
                        Log.d("SEQ", "login SEQ : " + String.valueOf(seq));
                        ApplicationController.getInstance().setSeq(seq);
                        //   Log.d("seq@@", ApplicationController.getInstance().getSeq());
                        ApplicationController.getInstance().setEmail(email);
                        // ApplicationController.getInstance().setLoginFlag(true);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        //  Toast.makeText(getApplicationContext(), new String(errorResponse), Toast.LENGTH_LONG).show();
                        Log.d("SUN@@", "Fail");
                        Toast.makeText(getApplicationContext(), "아이디 또는 패스워트가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });


            }
        });

    }



    public String JSONP(String a) {

        String seq = null;

        try {
            JSONObject obj = new JSONObject(a);
            String objStr = obj.get("userDto") + "";
            JSONObject data = new JSONObject(objStr);
            seq = data.get("seq") + "";
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return seq;
    }


}
