package com.example.user.travel360.Story;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.user.travel360.CustomDialog.MainImgSelectDialog;
import com.example.user.travel360.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-08-24.
 */
public class StoryWrite2Activity extends AppCompatActivity
{
    LinearLayout uploadImgLayout, mainStoryImgLayout, reviewWriteLayout, travelDayLayout;
    ArrayList<ArrayList<String>> selectedPhotos = new ArrayList <ArrayList<String>>();
    ArrayList <RecyclerView> recyclerView = new ArrayList <RecyclerView> ();
    ImageView mainStoryImgAddButton, reviewWriteAddButton, travelDayAddButton;
    ArrayList <String> mergePhotos = new ArrayList <String> ();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_story_write2);

        uploadImgLayout = (LinearLayout) findViewById(R.id.uploadImgLayout);
        mainStoryImgLayout = (LinearLayout) findViewById(R.id.mainStoryImgLayout);
        reviewWriteLayout = (LinearLayout) findViewById(R.id.reviewWriteLayout);
        travelDayLayout = (LinearLayout) findViewById(R.id.travelDayLayout);
        mainStoryImgAddButton = (ImageView) findViewById(R.id.mainStoryImgAddButton);
        reviewWriteAddButton = (ImageView) findViewById(R.id.reviewWriteAddButton);
        travelDayAddButton = (ImageView) findViewById(R.id.travelDayAddButton);

        Intent intent = getIntent();
        String storystring = new String(intent.getExtras().getString("storystring"));
        Log.d("storystring", storystring);
        selectedPhotos = (ArrayList <ArrayList<String>>) intent.getExtras().get("selectedPhotos");

        mergeSelectedPhotos();

        //storystring 텍스트 확인 테스트용
        //TextView textView = (TextView) findViewById(R.id.textView8);
        //textView.setText(storystring);

        mainStoryImgAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MainImgSelectDialog.class);
                intent.putExtra("mergePhotos", mergePhotos);
                startActivity(intent);
            }
        });

        reviewWriteAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });

        travelDayAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });

        uploadedImgShowing();
    }

    private void mergeSelectedPhotos()
    {
        for(int i=0; i<selectedPhotos.size(); i++)
        {
            mergePhotos.addAll(selectedPhotos.get(i));
        }
    }

    private void uploadedImgShowing()
    {
        final RecyclerView listItem = new RecyclerView(getApplicationContext());
        PhotoAdapter photoAdapter = new PhotoAdapter(getApplicationContext(), mergePhotos);

        listItem.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        listItem.setAdapter(photoAdapter);

        listItem.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
            }

            @Override
            public void onItemLongClick()
            {
            }
        }));

        listItem.setBackgroundColor(Color.rgb(255, 164, 78));
        uploadImgLayout.addView(listItem);
        recyclerView.add(listItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }
        if(id == R.id.complete){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_write2, menu);
        return true;
    }
}
