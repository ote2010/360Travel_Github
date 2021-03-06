package com.example.user.travel360.Story;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.travel360.Navigationdrawer.ApplicationController;
import com.example.user.travel360.R;
import com.example.user.travel360.Story.RecyclerItemClickListener.OnItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class StoryWriteActivity extends AppCompatActivity {

    enum RequestCode
    {
        pickphotoButton(R.id.pickphotoButton);

        @IdRes
        final int mViewId;
        RequestCode(@IdRes int viewId) {
            mViewId = viewId;
        }
    }

    ArrayList <RecyclerView> recyclerView = new ArrayList <RecyclerView> ();
    PhotoAdapter photoAdapter;
    LinearLayout container;
    Button textinsertButton;
    Button imageinsertButton;
    Button videoinsertButton;
    ArrayList <EditText> editText = new ArrayList <EditText> ();
    EditText editTitle;

    ArrayList <ArrayList <String>> selectedPhotos = new ArrayList <ArrayList<String>>();
    ArrayList <ArrayList <Integer>> imgSeqList = new ArrayList <ArrayList <Integer>>();

    String storystring;
    ArrayList <Integer> contentsSequence; // 이미지는 0, 텍스트는 1

    public static Activity write1Activity;

    //************* 서버 관련 코드 ****************
    static int imgSeq = -1;
    static int travelSeq = -1;
    static int userSeq = -1;
    //********************************************

    private void sendReadyVal()
    {
        RequestParams params = new RequestParams();
        params.put("seq", userSeq);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://kibox327.cafe24.com/writeRecordReady.do", params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                // called before request is started
                //Toast.makeText(getApplicationContext(), "START!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response)
            {
                // called when response HTTP status is "200 OK"
                //Toast.makeText(getApplicationContext(), new String(response), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "업로드 성공!", Toast.LENGTH_LONG).show();
                Log.d("sendReadyVal",new String(response));
                String res = new String(response); // travelSeq를 저장해야한다.!!!
                try {
                    JSONObject obj = new JSONObject(res);
                    String objStr =  obj.get("userDto") + "";
                    travelSeq = (int)obj.get("travelSeq");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
            {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //Toast.makeText(getApplicationContext(), new String(errorResponse), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "업로드가 실패했습니다! 다시 시도해주세요!", Toast.LENGTH_LONG).show();
                Log.d("sendReadyVal1", new String(errorResponse));
                Log.d("sendReadyVal2", new String(e.toString()));
                String res = new String(errorResponse);
            }

            @Override
            public void onRetry(int retryNo)
            {
                // called when request is retried
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar1));
        setContentView(R.layout.activity_story_write);

        write1Activity = StoryWriteActivity.this;

        container = (LinearLayout) findViewById(R.id.container);
        textinsertButton = (Button) findViewById(R.id.textinsertButton);
        imageinsertButton = (Button)findViewById(R.id.pickphotoButton);
        videoinsertButton = (Button) findViewById(R.id.pickVideoButton);
        editTitle = (EditText) findViewById(R.id.editTitle);

        storystring = new String();
        contentsSequence = new ArrayList <Integer> ();

        if(!ApplicationController.getInstance().getSeq().equals(null))
        {
            userSeq = Integer.valueOf(ApplicationController.getInstance().getSeq());
            Log.d("userSeq", String.valueOf(userSeq));
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!서버 코드!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        sendReadyVal();

        imageinsertButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final RecyclerView listItem = new RecyclerView (getApplicationContext());

                selectedPhotos.add(new ArrayList<String>());
                imgSeqList.add(new ArrayList<Integer>());
                photoAdapter = new PhotoAdapter(getApplicationContext(), selectedPhotos.get(recyclerView.size()));

                listItem.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
                listItem.setAdapter(photoAdapter);

                listItem.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        PhotoPreview.builder()
                                .setPhotos(selectedPhotos.get(recyclerView.indexOf(listItem)))
                                .setCurrentItem(position)
                                .start(StoryWriteActivity.this);
                    }

                    @Override
                    public void onItemLongClick()
                    {
                        AlertDialog.Builder alt_recycler = new AlertDialog.Builder(StoryWriteActivity.this);
                        alt_recycler.setMessage("선택한 이미지 레이아웃을 삭제하시겠습니까?").setCancelable(
                                false).setNegativeButton("네", new DialogInterface.OnClickListener() // 위치를 바꾸기 위해 서로 반대로
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                container.removeView(listItem);
                                selectedPhotos.remove(recyclerView.indexOf(listItem));
                                imgSeqList.remove(recyclerView.indexOf(listItem));

                                int temp = 0;
                                for(int i=0; i<contentsSequence.size(); i++)
                                {
                                    if(contentsSequence.get(i) == 0)
                                    {
                                        if(temp == recyclerView.indexOf(listItem))
                                        {
                                            contentsSequence.remove(i);
                                            break;
                                        }
                                        else
                                            temp++;
                                    }
                                }

                                recyclerView.remove(recyclerView.indexOf(listItem));
                            }
                        }).setPositiveButton("아니오", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        });

                        AlertDialog alert = alt_recycler.create();
                        alert.setTitle("삭제하기");
                        alert.setIcon(R.drawable.__picker_ic_delete_black_24dp);
                        alert.show();
                    }
                }));

                checkPermission(RequestCode.pickphotoButton);
                listItem.setBackgroundColor(Color.rgb(255, 164, 78));
                container.addView(listItem);
                recyclerView.add(listItem);
                contentsSequence.add(0);
            }
        });

        textinsertButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText listItem = new EditText(getApplicationContext());
                listItem.setBackgroundColor(Color.rgb(182, 252, 154));
                listItem.setTextColor(Color.BLACK);
                container.addView(listItem);

                listItem.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(final View v)
                    {
                        AlertDialog.Builder alt_recycler = new AlertDialog.Builder(StoryWriteActivity.this);
                        alt_recycler.setMessage("선택한 텍스트 레이아웃을 삭제하시겠습니까?").setCancelable(
                                false).setPositiveButton("아니오", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        }).setNegativeButton("네", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                container.removeView(listItem);

                                int temp = 0;
                                for (int i = 0; i < contentsSequence.size(); i++)
                                {
                                    if (contentsSequence.get(i) == 1)
                                    {
                                        if (temp == editText.indexOf(listItem))
                                        {
                                            contentsSequence.remove(i);
                                            break;
                                        } else
                                            temp++;
                                    }
                                }

                                editText.remove(editText.indexOf(listItem));
                            }
                        });

                        AlertDialog alert = alt_recycler.create();
                        alert.setTitle("삭제하기");
                        alert.setIcon(R.drawable.__picker_ic_delete_black_24dp);
                        alert.show();
                        return false;
                    }
                });

                contentsSequence.add(1);
                listItem.requestFocus();
                editText.add(listItem);
            }
        });

        videoinsertButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), VideoPickerActivity.class);
                startActivity(intent);
            }
        });
    }

    int imageUploadCheck = 0;
    void ImageUpload()
    {
        // **********업로드 코드 ************** 여러개 연속 보낼땐 for문으로 감싸기
        for(int i=0; i<selectedPhotos.get(recyclerView.size()-1).size(); i++)
        {
            String path = new String();
            path = selectedPhotos.get(recyclerView.size()-1).get(i);

            File myFile = new File(path);
            RequestParams params = new RequestParams();
            try
            {
                params.put("image", myFile);
            }
            catch (FileNotFoundException e)
            {

            }

            params.put("travelSeq", travelSeq);
            params.put("userSeq", userSeq);
            //params.put("userSeq", 1);
            params.put("groupSeq", recyclerView.size()-1);

            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://kibox327.cafe24.com/uploadImage.do", params, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    // called before request is started
                    //Toast.makeText(getApplicationContext(), "START!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response)
                {
                    // called when response HTTP status is "200 OK"
                    //Toast.makeText(getApplicationContext(), new String(response), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "업로드 성공!", Toast.LENGTH_LONG).show();
                    Log.d("ImageUpload",new String(response));
                    String res = new String(response);
                    try
                    {
                        JSONObject obj = new JSONObject(res);
                        String objStr =  obj.get("picture") + "";
                        JSONObject imgSeqJSON = new JSONObject(objStr);
                        imgSeq = (int)imgSeqJSON.get("seq"); // 나중에 구현해야 할 것 : 이 imgSeq를 삭제할때 서버로 보내줘야한다.....
                        Log.d("imgSeq", String.valueOf(imgSeq));
                        imgSeqList.get(recyclerView.size()-1).add(imgSeq);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("ImageUpload", "이미지 업로드 성공!");
                    imageUploadCheck = 1;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
                {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    //Toast.makeText(getApplicationContext(), new String(errorResponse), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "업로드가 실패했습니다! 다시 시도해주세요!", Toast.LENGTH_LONG).show();
                    Log.d("ImageUpload", "이미지 업로드 실패!");
                    if(recyclerView.size() != 0)
                    {
                        container.removeView(recyclerView.get(recyclerView.size() - 1));
                        recyclerView.remove(recyclerView.size() - 1);
                        selectedPhotos.remove(selectedPhotos.size() - 1);
                        contentsSequence.remove(contentsSequence.size() - 1);
                        imageUploadCheck = -1;
                    }
                }

                @Override
                public void onRetry(int retryNo)
                {
                    // called when request is retried
                }
            });
        }
        // ****************************************
    }

    void StoryStringUpload()
    {
        RequestParams params = new RequestParams();
        params.put("storystring", storystring);
        params.put("seq", travelSeq);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://kibox327.cafe24.com/writeComplete.do", params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                // called before request is started
                //Toast.makeText(getApplicationContext(), "START!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response)
            {
                // called when response HTTP status is "200 OK"
                //Toast.makeText(getApplicationContext(), new String(response), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "업로드 성공!", Toast.LENGTH_LONG).show();
                Log.d("StoryStringUpload", "텍스트 업로드 성공!");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
            {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //Toast.makeText(getApplicationContext(), new String(errorResponse), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "업로드가 실패했습니다! 다시 시도해주세요!", Toast.LENGTH_LONG).show();
                Log.d("StoryStringUpload", "텍스트 업로드 실패!");
            }

            @Override
            public void onRetry(int retryNo)
            {
                // called when request is retried
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE))
        {
            List<String> photos = null;
            if (data != null)
            {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.get(recyclerView.size()-1).clear();
            if (photos != null) // 업로드를 성공적으로 추가하고 돌아온 경우
            {
                selectedPhotos.get(recyclerView.size()-1).addAll(photos);
                //서버 코드 : 테스트 시에는 과도한 트래픽 막기위해 주석처리
                UploadProgressDialog task = new UploadProgressDialog();
                task.execute();

            }
            photoAdapter.notifyDataSetChanged();
        }

        if (resultCode == RESULT_OK && (requestCode == PhotoPreview.REQUEST_CODE))
        {
            List<String> photos = null;
            if (data != null)
            {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.get(recyclerView.size()-1).clear();
            if (photos != null)
            {
                selectedPhotos.get(recyclerView.size()-1).addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }

        if(resultCode == RESULT_OK && (requestCode == 4523))
        {
            Log.d("videoresult", "ok!!!");
        }

        if (resultCode == RESULT_CANCELED && (requestCode == PhotoPicker.REQUEST_CODE))
        {
            container.removeView(recyclerView.get(recyclerView.size()-1));
            recyclerView.remove(recyclerView.size()-1);
            selectedPhotos.remove(selectedPhotos.size()-1);
            contentsSequence.remove(contentsSequence.size()-1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay!
            onClick(RequestCode.values()[requestCode].mViewId);

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(this, "읽기 권한이 없어 수행할 수가 없습니다. 권한을 설정해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.CAMERA:
                // No need to explain to user as it is obvious
                return false;
            default:
                return true;
        }
    }

    private void checkPermission(@NonNull RequestCode requestCode) {

        int readStoragePermissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        boolean readStoragePermissionGranted = readStoragePermissionState != PackageManager.PERMISSION_GRANTED;
        boolean cameraPermissionGranted = cameraPermissionState != PackageManager.PERMISSION_GRANTED;

        if (readStoragePermissionGranted || cameraPermissionGranted)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
            {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else
            {
                String[] permissions;
                if (readStoragePermissionGranted && cameraPermissionGranted)
                {
                    permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
                }
                else
                {
                    permissions = new String[] {
                            readStoragePermissionGranted ? Manifest.permission.READ_EXTERNAL_STORAGE
                                    : Manifest.permission.CAMERA
                    };
                }
                ActivityCompat.requestPermissions(this, permissions, requestCode.ordinal());
            }

        }
        else
        {
            // Permission granted
            onClick(requestCode.mViewId);
        }
    }

    private void onClick(@IdRes int viewId) {

        switch (viewId) {
            case R.id.pickphotoButton: {
                //Intent intent = new Intent(MainActivity.this, PhotoPickerActivity.class);
                //PhotoPickerIntent.setPhotoCount(intent, 9);
                //PhotoPickerIntent.setColumn(intent, 4);
                //startActivityForResult(intent, REQUEST_CODE);
                PhotoPicker.builder()
                        .setPhotoCount(9) // 최대 몇개까지 고를 수 있는지
                        .setGridColumnCount(4) // 4개가 한 줄
                        .start(this);
                break;
            }
        }
    }

    public void storyLoadToString()
    {
        int imgTemp = 0;
        int textTemp = 0;
        for (int i = 0; i < contentsSequence.size(); i++)
        {
            switch(contentsSequence.get(i))
            {
                // 이미지
                case 0 :
                    storystring = storystring + "ImgGroup" + imgTemp + "\n";
                    /*
                    //이미지 절대 경로 붙이는 코드. 실제로는 필요없으므로 주석처리
                    for(int j = 0; j < selectedPhotos.get(imgTemp).size(); j++)
                    {
                        storystring = storystring + selectedPhotos.get(imgTemp).get(j) + "\n";
                    }
                    */
                    imgTemp++;
                    break;
                // 텍스트
                case 1 :
                    storystring = storystring + "TxtGroup" + textTemp + "\n";
                    storystring = storystring + editText.get(textTemp).getText() + "\n";
                    textTemp++;
                    break;
            }
        }
    }

    private class UploadProgressDialog extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog asyncDialog = new ProgressDialog(StoryWriteActivity.this);

        @Override
        protected void onPreExecute()
        {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("이미지를 업로드 중입니다. 잠시만 기다려주세요.");
            asyncDialog.setCanceledOnTouchOutside(false);

            asyncDialog.show();
            ImageUpload();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if (imageUploadCheck == 1)
            {
                asyncDialog.dismiss();
                Toast.makeText(getApplicationContext(), "업로드 성공!", Toast.LENGTH_SHORT).show();
                imageUploadCheck = 0;
            }
            else if (imageUploadCheck == -1)
            {
                asyncDialog.dismiss();
                Toast.makeText(getApplicationContext(), "업로드에 실패했습니다. 인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                imageUploadCheck = 0;
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            while(true)
            {
                if(imageUploadCheck != 0)
                    break;
            }
            return null;
        }
    }

    private int checkList()
    {
        if(contentsSequence.size() == 0)
            return -1;
        if(editTitle.getText().toString().equals(""))
            return -2;

        boolean mustOneImgCheck = false;
        for(int i=0; i<contentsSequence.size(); i++)
        {
            if(contentsSequence.get(i) == 0)
            {
                mustOneImgCheck = true;
                break;
            }
        }
        if(!mustOneImgCheck)
            return -3;

        return 1;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }
        if(id == R.id.nextPage){
            int errorMsg = checkList();
            if(errorMsg == 1)
            {
                storyLoadToString();
                Intent intent = new Intent(getApplicationContext(), StoryWrite2Activity.class);
                intent.putExtra("storystring", storystring);
                intent.putExtra("selectedPhotos", selectedPhotos);
                intent.putExtra("imgSeqList", imgSeqList);
                intent.putExtra("travelSeq", travelSeq);
                intent.putExtra("title", editTitle.getText().toString());
                StoryStringUpload();
                storystring = "";
                startActivity(intent);
                return true;
            }
            else if(errorMsg == -1)
            {
                Toast.makeText(getApplicationContext(), "여행기 내용을 작성해주세요!", Toast.LENGTH_LONG).show();
            }
            else if(errorMsg == -2)
            {
                Toast.makeText(getApplicationContext(), "여행기 제목을 작성해주세요!", Toast.LENGTH_LONG).show();
            }
            else if(errorMsg == -3)
            {
                Toast.makeText(getApplicationContext(), "사진을 최소 1개 이상 업로드해주세요!", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_write, menu);
        return true;
    }
}
