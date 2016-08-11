package com.example.user.travel360;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by user on 2016-08-09.
 */
public class StoryReadActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_story_read);

        LinearLayout pic2container = (LinearLayout) findViewById(R.id.pic2container);
        LinearLayout pic3container = (LinearLayout) findViewById(R.id.pic3container);
        LinearLayout pic4container = (LinearLayout) findViewById(R.id.pic4container);
        LinearLayout morethanpic4container = (LinearLayout) findViewById(R.id.morethanpic4container);

        ViewGroup.LayoutParams pms1 = pic2container.getLayoutParams();
        ViewGroup.LayoutParams pms2 = pic3container.getLayoutParams();
        ViewGroup.LayoutParams pms3 = pic4container.getLayoutParams();
        ViewGroup.LayoutParams pms4 = morethanpic4container.getLayoutParams();

        pms1.width = getResources().getDisplayMetrics().widthPixels;
        pms1.height = pms1.width;
        pms2.width = getResources().getDisplayMetrics().widthPixels;
        pms2.height = pms2.width;
        pms3.width = getResources().getDisplayMetrics().widthPixels;
        pms3.height = pms3.width;
        pms4.width = getResources().getDisplayMetrics().widthPixels;
        pms4.height = pms4.width;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            //Toast.makeText(this, "홈 아이콘 이벤트", Toast.LENGTH_SHORT).show();
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
}
