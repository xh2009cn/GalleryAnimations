package org.howe.galleryanimations.effects;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.howe.galleryanimations.BitmapCollections;
import org.howe.galleryanimations.Configs;
import org.howe.galleryanimations.particle.ParticleController;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class ZoomFadeIn implements IEffect {

    private static final int FRAME_COUNT = 70;
    private float mScaleX = 2.0f;
    private float mScaleY = 2.0f;
    private ParticleController mParticleController;

    public ZoomFadeIn(ParticleController controller) {
        mParticleController = controller;
    }

    @Override
    public void run(int frame, Canvas canvas, Matrix matrix, Paint paint) {
         if (frame < 0 || frame >= FRAME_COUNT) {
             return;
         }
        if (frame == 0) {
            BitmapCollections.getInstance().moveNextGallery();
        }

        if (frame < FRAME_COUNT / 2) {
            matrix.reset();
            paint.reset();
            float scaleX = mScaleX - (mScaleX - 1) * 2 * frame / (float) FRAME_COUNT;
            float scaleY = mScaleY - (mScaleY - 1) * 2 * frame / (float) FRAME_COUNT;
            matrix.postScale(scaleX, scaleY);
            matrix.postTranslate((1 - scaleX) * Configs.getWidth() / 2, (1 - scaleY) * Configs.getHeight() / 2);
            paint.setAlpha((int) (255 * (frame - FRAME_COUNT / 2f) / (FRAME_COUNT / 2f)));
            canvas.drawBitmap(BitmapCollections.getInstance().getCurrentBitmap(), matrix, paint);

            if (frame < FRAME_COUNT / 4) {
                matrix.reset();
                paint.reset();
                matrix.postScale(Configs.getWidth() / (float) BitmapCollections.getInstance().getStarBackgroundBitmap().getWidth()
                        , Configs.getHeight() / (float) BitmapCollections.getInstance().getStarBackgroundBitmap().getHeight());
                int alpha = (int) (255 * (FRAME_COUNT / 4f - frame) / (FRAME_COUNT / 4f));
                paint.setAlpha(alpha);
                canvas.drawBitmap(BitmapCollections.getInstance().getStarBackgroundBitmap(), matrix, paint);
            }
        } else {
            if (frame == FRAME_COUNT / 2) {
                mParticleController.startBubble();
            }
            matrix.reset();
            //matrix.postScale(1, 1);
            canvas.drawBitmap(BitmapCollections.getInstance().getCurrentBitmap(), matrix, null);
        }
    }

    @Override
    public int getFrameCount() {
        return FRAME_COUNT;
    }
}
