package com.example.user.travel360;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by user on 2016-08-09.
 */
public class StoryReadActivity extends AppCompatActivity
{
    /*
    프로세스 정리
    1) 표현할 이미지 개수 imgCount를 서버에서 받아온다.
    2) imgCount가 4개 이상, 4개, 3개, 2개, 1개 일 때의 경우에 따라 동적으로 코드에서 만들어준다.
    3) 받아온게 사진인지 그림인지. 판단해서 순차적으로 보여줘야한다.
    */

    int [] sequence = {1, 0, 0, 1, 0}; // 불러올 본문의 순서. 1 : 글 0 : 사진이라고 가정. 그러니까 글 사진 사진 글 사진과 같은 순서임.
    LinearLayout container;
    String storyText = "메밀꽃 필 무렵\n마지막 잎새가 어쩌구저쩌구 쏼라쏼라 끼야호 으아아앙ㄴ마ㅐ아ㅐㅏㅐ바재압ㅈ";

    LinearLayout [] textLayout = new LinearLayout[10];
    int textLayoutCount = 0;

    int imgCount;
    Intent imgCountIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_story_read);

        container = (LinearLayout) findViewById(R.id.container);

        storyUpload();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        if(id == R.id.menuButton){
            Toast.makeText(this,"메뉴 버튼 이벤트", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_read, menu);
        return true;
    }



    public void storyUpload()
    {
        LayoutInflater textLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < sequence.length; i++)
        {
            if(sequence[i] == 1) // 글
            {
                textLayout[textLayoutCount] = (LinearLayout) textLayoutInflater.inflate(R.layout.story_textview, null);
                TextView textLayoutTextView = (TextView) textLayout[textLayoutCount].findViewById(R.id.storyTextView);
                textLayoutTextView.setText(storyText);

                storyTextUpload(textLayout[textLayoutCount]);
                textLayoutCount++;
            }
            else // 사진
            {
                storyImageUpload();
            }
        }
    }

    public void storyTextUpload(LinearLayout textLayout)
    {
        container.addView(textLayout);
    }

    public void storyImageUpload()
    {

    }
}
