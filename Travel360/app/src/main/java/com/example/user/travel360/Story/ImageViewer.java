package com.example.user.travel360.Story;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.user.travel360.R;
import com.example.user.travel360.ShowVrActivity;
import com.example.user.travel360.uk.co.senab.photoview.PhotoView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by user on 2016-08-11.
 */
public class ImageViewer extends AppCompatActivity
{
    Button ImgVr;
    ImageView[] ViewerImg = new ImageView[10];
    Intent getIntent;
    int imgCount, imgIndex; // 로드할 이미지 개수, 이미지 인덱스
    String [] imgUriStringArray;
    Drawable [] imgDrawable;

    SamplePagerAdapter adapter;
    ViewerImgLoading task;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_imageviewer);

        getIntent = new Intent(this.getIntent());
        imgCount = getIntent.getIntExtra("imgCountIntent", -1);
        imgIndex = getIntent.getIntExtra("imgIndex", -1);
        imgUriStringArray = getIntent.getStringArrayExtra("imgUri");
        imgDrawable = new Drawable[imgCount];

        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new SamplePagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(10);
        mViewPager.setCurrentItem(imgIndex-1);

        ViewerImgLoading task = new ViewerImgLoading();
        task.execute();
        init();
    }
    public void init(){
        ImgVr = (Button)findViewById(R.id.ImgVr);
        ImgVr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowVrActivity.class);
                intent.putExtra("ImagePath","path" );
                startActivity(intent);
            }
        });
    }

    class SamplePagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount() {
            return imgDrawable.length;
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            //photoView.setImageResource(sDrawables[position]);
            photoView.setImageDrawable(imgDrawable[position]);

            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }

    private Drawable LoadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }

    private class ViewerImgLoading extends AsyncTask<String, Integer, Long>
    {
        @Override
        protected Long doInBackground(String... params)
        {
            for(int i = 0; i<imgUriStringArray.length; i++)
            {
                imgDrawable[i] = LoadImageFromWeb(imgUriStringArray[i]);
                publishProgress();
            }
             return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }
    }


    /*async http 라이브러리 사용법
    public void onButtonClicked(View v)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://kibox327.cafe24.com/login.do?id=a&password=1234", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                String byteToString = new String(responseBody,0,responseBody.length);
                test.setText(byteToString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
    */
}
