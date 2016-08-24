package com.example.user.travel360;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.travel360.RecyclerItemClickListener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

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

    RecyclerView[] recyclerView = new RecyclerView[50];
    int mRecyclerIndex;
    PhotoAdapter photoAdapter;
    LinearLayout container;
    Button textinsertButton;
    EditText[] editText = new EditText[50];
    int editTextCount = 0;
    int recyclerViewCount = 0;

    ArrayList<ArrayList <String>> selectedPhotos = new ArrayList <ArrayList<String>>();

    String storystring;
    int [] contentsSequence; // 이미지는 0, 텍스트는 1
    int contentsSeqIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_story_write);

        container = (LinearLayout) findViewById(R.id.container);
        textinsertButton = (Button) findViewById(R.id.textinsertButton);

        storystring = new String();
        contentsSequence = new int [50];

        findViewById(R.id.pickphotoButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                recyclerView[recyclerViewCount] = new RecyclerView(getApplicationContext());
                recyclerView[recyclerViewCount].setTag(recyclerViewCount);
                selectedPhotos.add(new ArrayList<String>());
                photoAdapter = new PhotoAdapter(getApplicationContext(), selectedPhotos.get(recyclerViewCount));

                recyclerView[recyclerViewCount].setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
                recyclerView[recyclerViewCount].setAdapter(photoAdapter);
                recyclerView[recyclerViewCount].addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new OnItemClickListener()
                {
                    @Override
                    public void getRecyclerView(RecyclerView view)
                    {
                        RecyclerView mRecyclerView = new RecyclerView(getApplicationContext());
                        mRecyclerView = view;
                        mRecyclerIndex = (int) mRecyclerView.getTag();
                    }

                    @Override
                    public void onItemClick(View view, int position)
                    {
                        PhotoPreview.builder()
                                .setPhotos(selectedPhotos.get(mRecyclerIndex))
                                .setCurrentItem(position)
                                .start(StoryWriteActivity.this);
                    }
                }));

                checkPermission(RequestCode.pickphotoButton);
                recyclerView[recyclerViewCount].setBackgroundColor(Color.rgb(255, 164, 78));
                container.addView(recyclerView[recyclerViewCount]);
                contentsSequence[contentsSeqIndex] = 0;
                contentsSeqIndex++;
            }
        });

        textinsertButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editText[editTextCount] = new EditText(getApplicationContext());
                editText[editTextCount].setBackgroundColor(Color.rgb(182, 252, 154));
                editText[editTextCount].setTextColor(Color.BLACK);
                container.addView(editText[editTextCount]);
                contentsSequence[contentsSeqIndex] = 1;
                contentsSeqIndex++;

                editText[editTextCount].requestFocus();

                editTextCount++;
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //if (resultCode == RESULT_OK &&
        //        (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE))
        {
            List<String> photos = null;
            if (data != null)
            {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.get(recyclerViewCount).clear();
            if (photos != null) // 업로드를 성공적으로 추가하고 돌아온 경우
            {
                selectedPhotos.get(recyclerViewCount).addAll(photos);
                /*
                // **********업로드 코드 ************** 여러개 연속 보낼땐 for문으로 감싸기
                String path = new String();
                path = selectedPhotos.get(recyclerViewCount).get(0);

                File myFile = new File(path);
                RequestParams params = new RequestParams();
                try
                {
                    params.put("image", myFile);
                }
                catch (FileNotFoundException e)
                {

                }

                params.put("seq", 1);

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
                        Toast.makeText(getApplicationContext(), "업로드 성공!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
                    {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        //Toast.makeText(getApplicationContext(), new String(errorResponse), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "업로드가 실패했습니다! 다시 시도해주세요!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRetry(int retryNo)
                    {
                        // called when request is retried
                    }
                });

                // ****************************************
                */
                recyclerViewCount++;
            }
            photoAdapter.notifyDataSetChanged();
        }

        if (resultCode == RESULT_CANCELED && (requestCode == PhotoPicker.REQUEST_CODE))
        {
            container.removeView(recyclerView[recyclerViewCount]);
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
            Toast.makeText(this, "No read storage permission! Cannot perform the action.", Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < contentsSeqIndex; i++)
        {
            switch(contentsSequence[i])
            {
                // 이미지
                case 0 :
                    storystring = storystring + "ImgGroup" + imgTemp + "\n";
                    for(int j = 0; j < selectedPhotos.get(imgTemp).size(); j++)
                    {
                        storystring = storystring + selectedPhotos.get(imgTemp).get(j) + "\n";
                    }
                    imgTemp++;
                    break;
                // 텍스트
                case 1 :
                    storystring = storystring + "TxtGroup" + textTemp + "\n";
                    storystring = storystring + editText[textTemp].getText() + "\n";
                    textTemp++;
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }
        if(id == R.id.nextPage){
            storyLoadToString();
            Intent intent = new Intent(getApplicationContext(), StoryWrite2Activity.class);
            intent.putExtra("write2", storystring);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_write, menu);
        return true;
    }
}
