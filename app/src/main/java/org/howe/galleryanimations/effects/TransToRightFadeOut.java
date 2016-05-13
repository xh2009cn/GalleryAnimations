package org.howe.galleryanimations.effects;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.howe.galleryanimations.BitmapCollections;
import org.howe.galleryanimations.Configs;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class TransToRightFadeOut implements IEffect {

    private static final int FRAME_COUNT = 45;
    private float mScaleX = 0.8f;
    private float mScaleY = 0.8f;
    private float mAlpha = 0.6f;

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
            float scaleX =  mScaleX + (1 - mScaleX) * (frame + 1) / (float)(FRAME_COUNT / 2);
            float scaleY =  mScaleY + (1 - mScaleY) * (frame + 1) / (float)(FRAME_COUNT / 2);
            matrix.postScale(scaleX, scaleY);
            matrix.postTranslate((1 - scaleX) * Configs.getWidth() / 2, (1 - scaleY) * Configs.getHeight() / 2);
            paint.setAlpha((int) (255 * (mAlpha + (1 - mAlpha) * 2 * frame / (float) FRAME_COUNT)));
            canvas.drawBitmap(BitmapCollections.getInstance().getCurrentBitmap(), matrix, paint);

            matrix.reset();
            paint.reset();
            matrix.postTranslate((2 * frame / (float) FRAME_COUNT) * Configs.getWidth(), 0);
            paint.setAlpha((int) (255 * (1 - 2 * frame / (float) FRAME_COUNT)));
            canvas.drawBitmap(BitmapCollections.getInstance().getLastPhotoBitmap(), matrix, paint);
        } else {
            canvas.drawBitmap(BitmapCollections.getInstance().getCurrentBitmap(), 0, 0, null);
        }
    }

    @Override
    public int getFrameCount() {
        return FRAME_COUNT;
    }
}
