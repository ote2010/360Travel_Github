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

    enum RequestCode {
        pickphotoButton(R.id.pickphotoButton);

        @IdRes
        final int mViewId;
        RequestCode(@IdRes int viewId) {
            mViewId = viewId;
        }
    }

    //RecyclerView recyclerView;
    RecyclerView[] recyclerView = new RecyclerView[50];
    int mRecyclerIndex;
    PhotoAdapter photoAdapter;
    LinearLayout container;
    Button textinsertButton;
    EditText[] editText = new EditText[50];
    int editTextCount = 0;
    int recyclerViewCount = 0;

    ArrayList<ArrayList <String>> selectedPhotos = new ArrayList <ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_story_write);

        container = (LinearLayout) findViewById(R.id.container);
        textinsertButton = (Button) findViewById(R.id.textinsertButton);

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
            }
        });

        textinsertButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editText[editTextCount] = new EditText(getApplicationContext());
                editText[editTextCount].setBackgroundColor(Color.rgb(182, 252, 154));
                container.addView(editText[editTextCount]);
                editTextCount++;
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if (resultCode == RESULT_OK &&
        //        (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null)
            {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.get(recyclerViewCount).clear();
            if (photos != null) // 업로드를 성공적으로 추가하고 돌아온 경우
            {
                selectedPhotos.get(recyclerViewCount).addAll(photos);
                recyclerViewCount++;
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
                ActivityCompat.requestPermissions(this,
                        permissions,
                        requestCode.ordinal());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            //Toast.makeText(this,"홈 아이콘 이벤트",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        if(id == R.id.saveButton){
            Toast.makeText(this,"임시 저장 이벤트", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.nextPage){
            Toast.makeText(this,"다음 글쓰기 이벤트", Toast.LENGTH_SHORT).show();
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
