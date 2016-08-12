package com.example.user.travel360;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.user.travel360.uk.co.senab.photoview.PhotoView;

/**
 * Created by user on 2016-08-11.
 */
public class ImageViewer extends AppCompatActivity
{
    ImageView[] ViewerImg = new ImageView[10];
    Intent getIntent;
    int imgCount, imgIndex; // 로드할 이미지 개수, 이미지 인덱스

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_imageviewer);

        /* 테스트 코드. 이미지 뷰를 사용했을 때
        ImageView mImageView = (ImageView) findViewById(R.id.ViewerImage);

        Drawable bitmap = getResources().getDrawable(R.drawable.testimg1);
        mImageView.setImageDrawable(bitmap);

        PhotoViewAttacher mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.update();
        */
        getIntent = new Intent(this.getIntent());
        imgCount = getIntent.getIntExtra("imgCountIntent", -1);
        imgIndex = getIntent.getIntExtra("imgIndex", -1);

        //ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        //SamplePagerAdapter adapter = new SamplePagerAdapter();
        //mViewPager.setAdapter(adapter);
    }

    class SamplePagerAdapter extends PagerAdapter
    {
        int[] sDrawables = new int[imgCount];

        @Override
        public int getCount() {
            return sDrawables.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            for(int i = 0; i < imgCount; i++)
            {
                // 추후 서버 구축이 완료되면 Url을 통해서 Drawable을 얻어올 계획.

            }

            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageResource(sDrawables[position]);

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

    /*
    public void ImageUpload()
    {
        /* 서버 개발이 완료되면 활용할 코드. 현재는 그냥 가정해서 일일히 ImageViewer에 할당해주는 것으로 한다.
        for(int i = 0; i < imgCount; i++)
        {
            ImageViewer[i] = new ImageView(getApplicationContext());

            // *** 서버에서 순차적으로 이미지를 받아오는 코드를 이 부분에서 작성하여 ImageViewer[i]에 할당해준다. ***
            ImageViewer[i].setImageResource(R.drawable.testimg1);
            // ***********************************************************************************************
        }
        */

    /*
        // !!!!! 가정해서 작성하는 코드 (실제로는 이렇게 안하고 위에서 처럼 할거임 !!!!!
        for(int i = 0; i < imgCount; i++)
        {
            ViewerImg[i] = new ImageView(getApplicationContext());
        }

        ViewerImg[0].setImageResource(R.drawable.testimg1);
        ViewerImg[1].setImageResource(R.drawable.testimg2);
        ViewerImg[2].setImageResource(R.drawable.hwiin);
        ViewerImg[3].setImageResource(R.drawable.testimg1);
        ViewerImg[4].setImageResource(R.drawable.testimg2);
        ViewerImg[5].setImageResource(R.drawable.hwiin);
        ViewerImg[6].setImageResource(R.drawable.testimg1);
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
    */
}
