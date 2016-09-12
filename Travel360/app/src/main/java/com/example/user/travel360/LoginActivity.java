package com.example.user.travel360;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.methods.HttpPost;


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
                        // Log.d("seq@@", JSONP(new String(response)));
                        String seq = JSONP(new String(response));
                        ApplicationController.getInstance().setSeq(seq);
                        Log.d("seq@@", ApplicationController.getInstance().getSeq());
                        ApplicationController.getInstance().setEmail(email);
                        ApplicationController.getInstance().setLoginFlag(true);
                        finish();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        //  Toast.makeText(getApplicationContext(), new String(errorResponse), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String JSONP(String a) {

        String seq = null;

        try {
            JSONObject obj = new JSONObject(a);
            String objStr = obj.get("userDto") + "";
            JSONObject data = new JSONObject(objStr);
            seq = data.get("seq")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return seq;
    }


}
