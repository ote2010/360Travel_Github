package com.example.user.travel360;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class StoryWriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            Toast.makeText(this,"홈 아이콘 이벤트",Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.story_write,menu);
        return true;
    }
}