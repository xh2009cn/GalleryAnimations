package org.howe.galleryanimations;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private GalleryAnimationView mGalleryAnimationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.init(this);
        setContentView(R.layout.activity_main);
        mGalleryAnimationView = (GalleryAnimationView) findViewById(R.id.photo_surface_view);
        mGalleryAnimationView.setZOrderOnTop(true); //设置画布  背景透明
        mGalleryAnimationView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryAnimationView.start();
            }
        });
        final Button pauseBtn = (Button) findViewById(R.id.btn_pause);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGalleryAnimationView.isRunning() && mGalleryAnimationView.pause()) {
                    pauseBtn.setText("Resume");
                } else if (mGalleryAnimationView.isPaused() && mGalleryAnimationView.resume()) {
                    pauseBtn.setText("Pause");
                }
            }
        });
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryAnimationView.stop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGalleryAnimationView.onDestroy();
    }
}
