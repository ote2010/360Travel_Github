package com.example.user.travel360.Vr;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.user.travel360.R;
import com.example.user.travel360.Story.Image;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowVrActivity extends Activity {
    VrPanoramaView panoWidgetView;
    View v;

    ImageLoaderTask backgroundImageLoaderTask;

    Intent getIntent;
    ArrayList <Image> ImageList = new ArrayList <Image> ();
    int index;
    Bitmap bitmap;
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vr);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getIntent = new Intent(this.getIntent());
        ImageList = (ArrayList<Image>)getIntent.getSerializableExtra("imgList");
        index = getIntent.getIntExtra("index", -1);

        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    URL ImageUrl = new URL("http://kibox327.cafe24.com/Image.do?imageName=" + ImageList.get(index).getPicture_loc());
                    HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();

                    bitmap = BitmapFactory.decodeStream(is);
                    check = true;
                }

                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();

        panoWidgetView = (VrPanoramaView) findViewById(R.id.pano_view1);
    }

    @Override
    public void onPause() {
        panoWidgetView.pauseRendering();
        super.onPause();
    }

    @Override
    public void onResume() {
        loadPanoImage();
        panoWidgetView.resumeRendering();

        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Destroy the widget and free memory.
        panoWidgetView.shutdown();
        super.onDestroy();
    }
    private synchronized void loadPanoImage() {
        Log.d("Load@", "in mathod");
        ImageLoaderTask task = backgroundImageLoaderTask;
        if (task != null && !task.isCancelled()) {
            // Cancel any task from a previous loading.
            Log.d("Load@", "Task.cancel(true)");
            task.cancel(true);
        }
        Log.d("Load@", "out loop");
        // pass in the name of the image to load from assets.
        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO; // option 을 TYPE_STEREO_OVER_UNDE로 되어있었는데 이렇게 하면 위아래가 안보임

        // use the name of the image in the assets/ directory.
        String panoImageName = "pano.jpg";
        Log.d("Load@", "image name");
        // create the task passing the widget view and call execute to start.
        task = new ImageLoaderTask(panoWidgetView, viewOptions, panoImageName);
        task.execute(this.getAssets());
        backgroundImageLoaderTask = task;
    }

    public class ImageLoaderTask extends AsyncTask<AssetManager, Void, Bitmap> {
        String TAG = "ImageLoaderTask";
        String assetName;
        WeakReference<VrPanoramaView> viewReference;
        VrPanoramaView.Options viewOptions;
        WeakReference<Bitmap> lastBitmap = new WeakReference<>(null);
        String lastName;
        public ImageLoaderTask(VrPanoramaView view, VrPanoramaView.Options viewOptions, String assetName) {
            viewReference = new WeakReference<>(view);
            this.viewOptions = viewOptions;
            this.assetName = assetName;
        }
        @Override
        protected Bitmap doInBackground(AssetManager... params) {
            AssetManager assetManager = params[0];

            if (assetName.equals(lastName) && lastBitmap.get() != null) {
                return lastBitmap.get();
            }

            try(InputStream istr = assetManager.open(assetName)) {
                //Bitmap b = BitmapFactory.decodeStream(istr);
                while(true)
                {
                    if(check)
                        break;
                }
                Bitmap b = bitmap;
                lastBitmap = new WeakReference<>(b);
                lastName = assetName;
                return b;
            } catch (IOException e) {
                Log.e(TAG, "Could not decode default bitmap: " + e);
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            final VrPanoramaView vw = viewReference.get();
            if (vw != null && bitmap != null) {
                vw.loadImageFromBitmap(bitmap, viewOptions);
            }
        }
    }
}
