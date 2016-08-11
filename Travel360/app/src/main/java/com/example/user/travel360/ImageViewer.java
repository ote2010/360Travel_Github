package com.example.user.travel360;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.user.travel360.uk.co.senab.photoview.PhotoView;

/**
 * Created by user on 2016-08-11.
 */
public class ImageViewer extends AppCompatActivity
{
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

        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        SamplePagerAdapter adapter = new SamplePagerAdapter();
        mViewPager.setAdapter(adapter);
    }

    static class SamplePagerAdapter extends PagerAdapter
    {
        private static final int[] sDrawables = { R.drawable.testimg1, R.drawable.testimg2, R.drawable.hwiin,
                R.drawable.plane};

        @Override
        public int getCount() {
            return sDrawables.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageResource(sDrawables[position]);

            // Now just add PhotoView to ViewPager and return it
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
}
