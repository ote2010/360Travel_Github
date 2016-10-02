package com.example.user.travel360.Navigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travel360.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class JoinActivity extends Activity {

    ArrayList<Integer> contentsSequence;

    String gender, email, pw, name;
    TextView PWCH;
    Button BtnSumit;
    EditText EditEmail, EditPW, EditName, EditPWCh;
    RadioGroup radioGroup;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    ImageButton   selectProfile;
    ImageView ProfileImg;
    Uri uri = null;
    String path;


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
        EditPWCh = (EditText) findViewById(R.id.editPwCh);
        EditName = (EditText) findViewById(R.id.editName);
        PWCH = (TextView) findViewById(R.id.PWCH);
        //  EditBirth = (EditText) findViewById(R.id.editBirth);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ProfileImg = (ImageView) findViewById(R.id.profile);
        selectProfile = (ImageButton) findViewById(R.id.selectProfile);
    }


    public void onClickBtn() {
        selectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1001);

            }
        });

        BtnSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkEditBoxInput()) {
                    return;
                }

                Join_Server();


            }
        });
        EditPWCh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String PW = EditPW.getText().toString();
                String PWC = EditPWCh.getText().toString();
                if (PW.equals(PWC)) {
                    PWCH.setText("비밀번호가 일치합니다.");
                    PWCH.setTextColor(Color.GREEN);
                } else {
                    PWCH.setText("비밀번호가 일치하지 않습니다.");
                    PWCH.setTextColor(Color.RED);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private String checkRadioBtn() {
        int id = radioGroup.getCheckedRadioButtonId();
        RadioButton radioBtn = (RadioButton) findViewById(id);

        return radioBtn.getText().toString();
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
        } else if (uri == null) {
            Toast.makeText(getApplicationContext(), "프로필 사진을 선택하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //  Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());
                    uri = data.getData();
                    path = getPath(uri);
                    Log.d("@URI@", uri + "");

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ProfileImg.setImageBitmap(image_bitmap);

                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    void Join_Server() {
        //   long todaydate = System.currentTimeMillis(); // long 형의 현재시간
        RequestParams params = new RequestParams();
        // 기본 데이터
        String email1 = EditEmail.getText().toString();
        String PW1 = EditPW.getText().toString();
        String name1 = EditName.getText().toString();


        // File myFile = new File(uri+"");
        File myFile = new File(path);
        Log.d("SUN@@U", path);
        //  Log.d("SUN@@F", myFile.exists() + "");

        params.put("password", PW1);
        params.put("email", email1);
        params.put("id", email1);
        params.put("name", name1);
        // params.put("permission", true);
        try
        {
            params.put("image", myFile);
        }
        catch (FileNotFoundException e)
        {

        }

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStory_Server()");
        client.post("http://kibox327.cafe24.com/registUser.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN@@", "onSuccess // statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
                gender = checkRadioBtn();

                email = EditEmail.getText().toString();
                pw = EditPW.getText().toString();
                name = EditName.getText().toString();


                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                edit.putString("email", email);
                edit.putString("pw", pw);
                edit.putString("gender", gender);
                edit.commit();
                ApplicationController.getInstance().initSharedPreference();
                //    Toast.makeText(getApplicationContext(), ApplicationController.getInstance().getEmail() + "/" +ApplicationController.getInstance().getGender() + "/" + ApplicationController.getInstance().getPw(), Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN@@", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

}
