package com.example.user.travel360;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.user.travel360.Dao.TravelRecordDto;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ServerConnectActivity extends AppCompatActivity {
    /*
        필요한 기능의 함수만 짤라서 가져다 사용하면 됩니다.
        RequestParams params 는 서버로 보내줘야 할 필수 데이터들입니다.
        stroy 전체 데이터를 제외한 모든 부분에 필수 데이터들이 있습니다.
        알맞게 채워서 보내주세요.

		연동 완료된 목록
		1. story 쓰기  : writeStory_Server()
		2. story 전체 데이터 받아오기 : getTravleRecordAll_Server()
		3. story 1개에 대한 데이터 받아오기 : getTravleRecord_Server()
		4. 댓글 쓰기 : writeComment_Server();
		5. 저장된 image 가져오기 : getImage_Server();
	*/

    /************************  story 쓰기  *********************/
    void writeStory_Server() {

        RequestParams params = new RequestParams();

        params.put("userSeq","3");
        params.put("seq", 3);
        params.put("text", "write text");
        params.put("title", "write title");
        params.put("presentation_image", 1);


        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStory_Server()");
        client.get("http://kibox327.cafe24.com/writeComplete.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d("SUN", "getData_Server() onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {        }
        });
    }


    /*****************  story 전체 데이터  **********************/

    void getTravleRecordAll_Server() {

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "getTravleRecordAll_Server()");
        client.get("http://kibox327.cafe24.com/getTravelRecordList.do", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {         }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("travels") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for(int i=0; i<arr.length(); i++ ) {

                        JSONObject obj = (JSONObject)arr.get(i);

                        int seq  = (Integer)obj.get("seq");
                        int user_info_seq = (Integer)obj.get("user_info_seq");
                        String presentation_image = (String)obj.get("presentation_image");
                        String text = (String)obj.get("text");



                       /* TravelRecordDto td = new TravelRecordDto();
                        td.setSeq((Integer)obj.get("seq"));
                        td.setUser_info_seq((Integer)obj.get("user_info_seq"));
                        td.setPresentation_image((String)obj.get("presentation_image"));
                        td.setText((String)obj.get("text"));*/
                    }

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
            public void onRetry(int retryNo) {          }
        });
    }



    /***********  story 당 (  여행기 1개에 대한 데이터 ) ***************/

    void getTravleRecord_Server() {

        RequestParams params = new RequestParams();
        params.put("seq", 1);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getData_Server()");
        client.get("http://kibox327.cafe24.com/getTravelRecord.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d("SUN", "getData_Server() onStart");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);

                try {
                    JSONObject obj = new JSONObject(res);

                    Log.d("SUN",  "travelRecordDto : " +obj.get("travelRecordDto"));
                    String objStr =  obj.get("travelRecordDto") + "";
                    JSONObject record = new JSONObject(objStr);
                    Log.d("SUN",  "record : " +record.get("evaluation"));
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


    /****************************  댓글 쓰기  *************************/
    void writeComment_Server() {
        RequestParams params = new RequestParams();
        params.put("seq",3);
        params.put("comment", "comment text");
        params.put("evaluation", 5);

        params.put("travelSeq", 3);

        params.put("UserSeq", 3);


        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStory_Server()");
        client.get("http://kibox327.cafe24.com/writeComment.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d("SUN", "getData_Server() onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {        }
        });
    }

    /***************  image 가져오기  *********************/

    public Bitmap byteArrayToBitmap(byte[] byteArray ) {  // byte -> bitmap 변환 및 반환
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return bitmap ;
    }


    void getImage_Server() {

        RequestParams params = new RequestParams();
        params.put("imageName", "20160903043004Screenshot_20160902-144102.png");
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getImage_Server()");
        client.get("http://kibox327.cafe24.com/Image.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // byteArrayToBitmap 를 통해 reponse로 받은 이미지 데이터 bitmap으로 변환
                Bitmap bitmap = byteArrayToBitmap(response);


                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {    }
        });
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_server_connect);
    }

}
