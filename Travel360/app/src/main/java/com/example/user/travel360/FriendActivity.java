package com.example.user.travel360;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.travel360.CustomList.CustomAdapter;
import com.example.user.travel360.CustomList.ItemData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FriendActivity extends AppCompatActivity {

    public ListView listView;
    public CustomAdapter adapter;
    public ArrayList<ItemData> itemDatas = new ArrayList<ItemData>();
    public ItemData itemData;
    AlertDialog.Builder aDialog;
    AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        listView = (ListView) findViewById(R.id.friendList);
        adapter = new CustomAdapter(itemDatas, FriendActivity.this);

        getFriendsList_Server();
        adapter.clear();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemData itemData_temp = (ItemData) adapter.getItem(position);
                 String travleld = itemData_temp.comment_id;
                 String travleName = itemData_temp.comment_txt;
                Toast.makeText(FriendActivity.this, "id : "+ travleld + " / name : " + travleName, Toast.LENGTH_SHORT).show();
            }
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
                        String name = (String)friend.get("name");
                        int seq = (int)friend.get("seq");
                        Log.d("SUN", "id : "+id + " , seq : "+ seq);
                        adapter.addListItem(id, name);
                        listView.setSelection(i);
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
}
