package com.example.user.travel360.CustomDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.travel360.R;
import com.example.user.travel360.Story.PhotoAdapter;
import com.example.user.travel360.Story.RecyclerItemClickListener;

import java.util.ArrayList;

public class MainImgSelectDialog extends Activity
{
    ArrayList <String> mergePhotos = new ArrayList <String> ();
    RecyclerView recyclerView;
    Button yesButton;
    Button noButton;
    private static boolean main_img_selected = false;
    private static int checked_img_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setFinishOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_mainimgselect);

        Intent intent = getIntent();
        mergePhotos = (ArrayList <String>)intent.getExtras().get("mergePhotos");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        yesButton = (Button) findViewById(R.id.yesButton);
        noButton = (Button) findViewById(R.id.noButton);

        checked_img_count = 0;
        main_img_selected = false;

        uploadedImgShowing();
    }

    private void uploadedImgShowing()
    {
        PhotoAdapter photoAdapter = new PhotoAdapter(getApplicationContext(), mergePhotos);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if(!main_img_selected && view.getAlpha() == 1.0f)
                {
                    view.setAlpha(0.6f);
                    main_img_selected = true;
                    checked_img_count++;
                    Log.d("checked_img_count", String.valueOf(checked_img_count));
                }
                else if(main_img_selected && view.getAlpha() == 0.6f)
                {
                    view.setAlpha(1.0f);
                    main_img_selected = false;
                    checked_img_count--;
                    Log.d("checked_img_count", String.valueOf(checked_img_count));
                }
            }

            @Override
            public void onItemLongClick()
            {

            }
        }));

        recyclerView.setBackgroundColor(Color.rgb(255, 164, 78));
    }

    public void yesButtonOnClick(View v)
    {
        if(checked_img_count != 1)
        {
            Toast.makeText(getApplicationContext(), "대표 이미지를 선택해주세요!", Toast.LENGTH_SHORT).show();
        }
        else if(checked_img_count == 1)
        {
            onBackPressed();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "정상적인 수행이 아닙니다.", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getApplicationContext(), "확인 버튼", Toast.LENGTH_LONG).show();
    }

    public void noButtonOnClick(View v)
    {
        //Toast.makeText(getApplicationContext(), "취소 버튼", Toast.LENGTH_LONG).show();
        onBackPressed();
    }
}
