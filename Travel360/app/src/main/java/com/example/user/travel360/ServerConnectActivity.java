package com.example.user.travel360;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ServerConnectActivity extends AppCompatActivity {
    /* 한글 인코딩 방법
        String com = (String) obj.get("comment"); // json 객체 값 받아서
        String comment = new String(com.getBytes("utf-8"),"utf-8"); // 변환
   */

     /*
        필요한 기능의 함수만 짤라서 가져다 사용하면 됩니다.
        RequestParams params 는 서버로 보내줘야 할 필수 데이터들입니다.
       알맞게 채워서 보내주세요.

		연동 완료된 목록

		0. stroy 쓰기전 확인 : writeStoryRead_Server()
		1. story 쓰기  : writeStory_Server()
		2. story 전체 데이터 받아오기 : getTravleRecordAll_Server()
		                --> story 검색
		3. story 1개에 대한 데이터 받아오기 : getTravleRecord_Server()
		4. story 댓글 쓰기 : writeStoryComment_Server()
        5. story 댓글 리스트 : getComment_Server()

		6. 저장된 image 가져오기 : getImage_Server(),
		7. 사용자 정보 가져오기 : getUserInfo_Server()

		8. 친구 (요청)추가 하기  : addFriend_Server()
		9. 친구 수락 하기 : acceptFriend_Server()
		10. 내 친구 수 : getCountFriends_Server()
		11. 내 친구 목록 : getFriendsList_Server()

		12. review 전체 데이터 : getTravleReviewAll_Server()
		13. review 1개 데이터 : getTravleReview_Server()
		14. review 쓰기 :  writeReview_Server()
		15. review 댓글 쓰기 : writeReviewComment_Server()
		16. review 댓글 리스트 : getReviewComment_Server()
		17. review 랭킹 : showReviewRanking_Server();
	*/

    /************** stroy 쓰기전 확인 ******************/
    void writeStoryRead_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 userSeq 만 있으면 됩니다.
        params.put("seq","2");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStoryRead_Server()");
        client.get("http://kibox327.cafe24.com/writeRecordReady.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

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
            public void onRetry(int retryNo) {  }
        });
    }

    /************************  story 쓰기  *********************/
    void writeStory_Server() {

        RequestParams params = new RequestParams();
        // 기본 데이터
        params.put("userSeq","3");
        params.put("seq", 3); // travelseq를 넣어서 보내주면 된다.
        params.put("text", "write text");
        params.put("title", "write title");
        params.put("presentation_image", 1);
        // 추가 데이터 Dao -> TravelRecordDto 참조

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
        /*
            //  기간검색
             SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
             Date start_date = null, finish_date =  null;
             long start_time = 0,finish_time = 0;
             try {
                 start_date = df.parse("2016.07.01");
                 start_time = start_date.getTime();

                 finish_date = df.parse("2016.10.01");
                 finish_time = finish_date.getTime();

                 Log.d("SUN", "start_time : " + start_time + " ,  finish_time : " + finish_time);
             } catch (ParseException e) {
                 e.printStackTrace();
             }
             params.put("start_date_client", start_time);
             params.put("finish_date_client", finish_time);
         */

        /*
             // 위치 검색
             double longitude, latitude;
             params.put("longitude", longitude);
             params.put("latitude", latitude);
             params.put("distenceFlag",true);
          */

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
        // 보내는 data는 seq 만 있으면 됩니다.
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


    /****************************  여행기 댓글 쓰기  *************************/
    void writeStoryComment_Server() {
        //   long todaydate = System.currentTimeMillis(); // long 형의 현재시간

        RequestParams params = new RequestParams();
        params.put("comment","travle comment");
        params.put("evaluation","1");
        params.put("travel_record_seq","1");
        params.put("id","a");
        //  params.put("write_date",todaydate);

        params.put("UserSeq","1");
        params.put("travelSeq","1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStoryComment_Server()");
        client.get("http://kibox327.cafe24.com/writeComment.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

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
            public void onRetry(int retryNo) {  }
        });
    }


    /***************  image 가져오기  *********************/

    public Bitmap byteArrayToBitmap(byte[] byteArray ) {  // byte -> bitmap 변환 및 반환
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return bitmap ;
    }


    void getImage_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 imageName 만 있으면 됩니다.
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

    /*********** 여행기 댓글 리스트  ***************/
    void getComment_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 userSeq 만 있으면 됩니다.
        params.put("travelSeq","1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getComment_Server()");
        client.get("http://kibox327.cafe24.com/getTravelCommentList.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("comment") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for(int i=0; i<arr.length(); i++ ) {
                        JSONObject obj = (JSONObject)arr.get(i);
                        String comment = (String)obj.get("comment");
                        int evaluation = (int)obj.get("evaluation");
                        String id = (String)obj.get("id");
                        int seq = (int)obj.get("seq");
                        int user_info_seq = (int)obj.get("user_info_seq");
                        JSONObject write_date = (JSONObject)obj.get("write_date");

                        long time  = (long)write_date.get("time");
                        Date date = new Date(time);

                        Log.d("SUN", "comment : "+comment + " , id : " + id + " , user_info_seq : " + user_info_seq + " , date : "+ date + " , seq : "+seq);

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
            public void onRetry(int retryNo) {  }
        });
    }

    /************* 사용자 정보 **************/
    void getUserInfo_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq 만 있으면 됩니다.
        params.put("seq","1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getUserInfo_Server()");
        client.get("http://kibox327.cafe24.com/getUserInfo.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try{
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("userDto") + "";
                    JSONObject obj = new JSONObject(objStr);

                    String id = (String)obj.get("id");
                    String name = (String)obj.get("name");
                    String profile_image = (String)obj.get("profile_image");
                    Log.d("SUN", "profile_image : "+profile_image);


                }catch (JSONException e){

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {  }
        });
    }

    /************** 친구 추가 하기  ***********************/
    void addFriend_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq, targetSeq 만 있으면 됩니다.
        params.put("seq","1");
        params.put("targetSeq","2");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "addFriend_Server()");
        client.get("http://kibox327.cafe24.com/addFriend.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

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
            public void onRetry(int retryNo) {  }
        });
    }

    /************   친구 수락 하기  ****************/
    void acceptFriend_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq, targetSeq 만 있으면 됩니다.
        params.put("seq","2");
        params.put("targetSeq","1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "acceptFriend_Server()");
        client.get("http://kibox327.cafe24.com/acceptFriend.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

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
            public void onRetry(int retryNo) {  }
        });
    }

    /**************  내 친구 수  **************/
    void getCountFriends_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq 만 있으면 됩니다.
        params.put("seq","1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getCountFriends_Server()");
        client.get("http://kibox327.cafe24.com/getCountFriends.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try{
                    JSONObject object = new JSONObject(res);
                    int friendsCount = (int)object.get("friendsCount");
                    Log.d("SUN","count : "+friendsCount);

                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("SUN",  "e : " + e.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {  }
        });
    }

    /*****************  내 친구 목록  ******************/
    void getFriendsList_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 seq 만 있으면 됩니다.
        params.put("seq","1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getFriendsList_Server()");
        client.get("http://kibox327.cafe24.com/friendsList.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try{
                    JSONObject object = new JSONObject(res);
                    String arrayStr = object.get("userDtoList") + "";
                    JSONArray array = new JSONArray(arrayStr);

                    for(int i=0; i<array.length(); i++)
                    {
                        JSONObject friend = (JSONObject)array.get(i);
                        String id = (String)friend.get("id");
                        int seq = (int)friend.get("seq");
                        Log.d("SUN", "id : "+id + " , seq : "+ seq);

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Log.d("SUN",  "e : " + e.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SUN", "onFailure // statusCode : " + statusCode + " , headers : " + headers.toString() + " , error : " + error.toString());
            }

            @Override
            public void onRetry(int retryNo) {  }
        });
    }



    /*****************  review 전체 데이터  **********************/

    void getTravleReviewAll_Server() {

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("SUN", "getTravleReviewAll_Server()");
        client.get("http://kibox327.cafe24.com//travelReviewList.do", new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {         }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));

                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("reviews") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for(int i=0; i<arr.length(); i++ ) {
                        /*
                        JSONObject obj = (JSONObject)arr.get(i);

                        int seq  = (Integer)obj.get("seq");
                        int user_info_seq = (Integer)obj.get("user_info_seq");
                        String presentation_image = (String)obj.get("presentation_image");
                        String text = (String)obj.get("text");
                        */
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


    /*****************  review 1개 데이터  **********************/

    void getTravleReview_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 reviewSeq 만 있으면 됩니다.
        params.put("reviewSeq", 1);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getTravleReview_Server()");
        client.get("http://kibox327.cafe24.com/getTravelReview.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {   }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);

                try {
                    JSONObject obj = new JSONObject(res);
                    String objStr =  obj.get("review") + "";
                    JSONObject review = new JSONObject(objStr);
                    String location = (String)review.get("location");
                    String text = (String)review.get("text");
                    // String user = (String)review.get("user");
                    long write_date_client = (long)review.get("write_date_client");

                    Log.d("SUN",  "location : " +location);
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


    /************************  review 쓰기  *********************/
    void writeReview_Server() {
        long todaydate = System.currentTimeMillis(); // long 형의 현재시간
        RequestParams params = new RequestParams();
       /*
        // 기본 데이터
        String WriteText = ReviewWrite.getText().toString();
        String seq = ApplicationController.getInstance().getSeq();
        String text1 =  ReviewWrite.getText().toString();

        params.put("userSeq", seq);
        params.put("text", text1);
        params.put("write_date_client", todaydate);
        params.put("location", "Paris");
        */
        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStory_Server()");
        client.post("http://kibox327.cafe24.com/writeTravelReview.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN_확인", "Success // statusCode : " + statusCode + " , response : " + new String(response));
                String res = new String(response);
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


    /****************************  리뷰 댓글 쓰기  *************************/
    void writeReviewComment_Server() {
        long todaydate = System.currentTimeMillis(); // long 형의 현재시간

        RequestParams params = new RequestParams();
        params.put("comment","review comment");
        params.put("evaluation","1");
        params.put("reviewSeq","1");
        params.put("userSeq","1");
        params.put("write_date_client",todaydate);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getComment_Server()");
        client.get("http://kibox327.cafe24.com/writeReviewComment.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }

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
            public void onRetry(int retryNo) {  }
        });
    }

    /*********** 리뷰 댓글 리스트  ***************/
    void getReviewComment_Server() {

        RequestParams params = new RequestParams();
        // 보내는 data는 reviewSeq 만 있으면 됩니다.
        params.put("reviewSeq","1");

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "getReviewComment_Server()");
        client.get("http://kibox327.cafe24.com/getReviewCommentList.do", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {  }


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);
                try {
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("comment") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for(int i=0; i<arr.length(); i++ ) {
                        JSONObject obj = (JSONObject)arr.get(i);
                         /* String comment = (String)obj.get("comment");
                        int evaluation = (int)obj.get("evaluation");
                        String id = (String)obj.get("id");
                        int seq = (int)obj.get("seq");
                        int user_info_seq = (int)obj.get("user_info_seq");
                        JSONObject write_date = (JSONObject)obj.get("write_date");

                        long time  = (long)write_date.get("time");
                        Date date = new Date(time);

                        Log.d("SUN", "comment : "+comment + " , id : " + id + " , user_info_seq : " + user_info_seq + " , date : "+ date + " , seq : "+seq);
                        */
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
            public void onRetry(int retryNo) {  }
        });
    }


    /************************  리뷰 랭킹  *********************/
    void showReviewRanking_Server() {

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d("SUN", "writeStory_Server()");
        client.get("http://kibox327.cafe24.com/travelReviewRankingList.do",  new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("SUN", "statusCode : " + statusCode + " , response : " +  new String(response));
                String res = new String(response);

                try {
                    JSONObject object = new JSONObject(res);
                    String objStr =  object.get("reviews") + "";
                    JSONArray arr = new JSONArray(objStr);
                    for(int i=0; i<arr.length(); i++ ) {
                        JSONObject obj = (JSONObject)arr.get(i);
                        //float evaluation = (float)obj.get("evaluation");
                        String location = (String)obj.get("location");
                        String text  = (String)obj.get("text");
                        int userseq  = (int)obj.get("seq");
                        long start_date = (long)obj.get("write_date_client");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                        String start = df.format(start_date);
                        Log.d("SUN", "evaluation : "+ " " + " , location : " + location + " , text : " + text + " , userseq : "+ userseq + " , start : "+start  );

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
            public void onRetry(int retryNo) {        }
        });
    }









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_server_connect);
    }

}
