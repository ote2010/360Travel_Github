package com.example.user.travel360;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by user on 2016-08-24.
 */
public class StoryWrite2Activity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_story_write2);

        Intent intent = getIntent();
        String storystring = new String(intent.getExtras().getString("write2"));

        TextView textView = (TextView) findViewById(R.id.textView8);
        textView.setText(storystring);
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
