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
import com.example.user.travel360.Vr.ShowVrActivity;
import com.example.user.travel360.uk.co.senab.photoview.PhotoView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2016-08-11.
 */
public class ImageViewer extends AppCompatActivity
{
    Button ImgVr;
    ImageView[] ViewerImg = new ImageView[10];
    Intent getIntent;
    int imgCount, imgIndex; // 로드할 이미지 개수, 이미지 인덱스
    ArrayList <Image> ImageList;
    Drawable [] imgDrawable;

    SamplePagerAdapter adapter;
    ViewerImgLoading task;

    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_imageviewer);

        getIntent = new Intent(this.getIntent());
        imgCount = getIntent.getIntExtra("imgCount", -1);
        imgIndex = getIntent.getIntExtra("imgIndex", -1);
        ImageList = (ArrayList <Image>)getIntent.getSerializableExtra("imgList");
        imgDrawable = new Drawable[imgCount];

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new SamplePagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(10);
        mViewPager.setCurrentItem(imgIndex - 1);

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
                intent.putExtra("index", mViewPager.getCurrentItem());
                intent.putExtra("imgList", ImageList);
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
            for(int i = 0; i<ImageList.size(); i++)
            {
                imgDrawable[i] = LoadImageFromWeb("http://kibox327.cafe24.com/Image.do?imageName=" + ImageList.get(i).getPicture_loc());
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
}
