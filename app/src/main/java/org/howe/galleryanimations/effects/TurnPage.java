package org.howe.galleryanimations.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.howe.galleryanimations.BitmapCollections;
import org.howe.galleryanimations.Configs;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public class TurnPage implements IEffect {

    private static final int FRAME_COUNT = 50;
    private Bitmap mLastBitmapLeft;
    private Bitmap mLastBitmapRight;
    private Bitmap mCurrentBitmapLeft;
    private Bitmap mCurrentBitmapRight;

    @Override
    public void run(int frame, Canvas canvas, Matrix matrix, Paint paint) {
        if (frame < 0 || frame >= FRAME_COUNT) {
            return;
        }
        if (frame == 0) {
            BitmapCollections.getInstance().moveNextGallery();

            matrix.reset();
            Bitmap curBitmap = BitmapCollections.getInstance().getCurrentBitmap();
            matrix.postScale(Configs.getWidth() / (float)curBitmap.getWidth(), Configs.getHeight() / (float)curBitmap.getHeight());
            mCurrentBitmapLeft = Bitmap.createBitmap(curBitmap, 0, 0, curBitmap.getWidth() / 2, curBitmap.getHeight(), matrix, true);
            mCurrentBitmapRight = Bitmap.createBitmap(curBitmap, curBitmap.getWidth() / 2, 0, curBitmap.getWidth() / 2, curBitmap.getHeight(), matrix, true);

            matrix.reset();
            Bitmap lastBitmap = BitmapCollections.getInstance().getLastPhotoBitmap();
            matrix.postScale(Configs.getWidth() / (float)lastBitmap.getWidth(), Configs.getHeight() / (float)lastBitmap.getHeight());
            mLastBitmapLeft = Bitmap.createBitmap(lastBitmap, 0, 0, lastBitmap.getWidth() / 2, lastBitmap.getHeight(), matrix, true);
            mLastBitmapRight = Bitmap.createBitmap(lastBitmap, lastBitmap.getWidth() / 2, 0, lastBitmap.getWidth() / 2, lastBitmap.getHeight(), matrix, true);
        }
        if (frame < FRAME_COUNT / 2) {
            canvas.drawBitmap(mLastBitmapLeft, 0, 0, null);

            matrix.reset();
            matrix.postTranslate(Configs.getWidth() / 2, 0);
            canvas.drawBitmap(mCurrentBitmapRight, matrix, null);

            if (frame < FRAME_COUNT / 4) {
                matrix.reset();
                matrix.postScale(1 - 4 * frame / (float) FRAME_COUNT, 1);
                matrix.postTranslate(Configs.getWidth() / 2, 0);
                canvas.drawBitmap(mLastBitmapRight, matrix, null);
            } else {
                matrix.reset();
                float scale = 4 * frame / (float) FRAME_COUNT - 1;
                matrix.postScale(scale, 1);
                matrix.postTranslate(Math.max(0, 1 - scale) * Configs.getWidth() / 2, 0);
                canvas.drawBitmap(mCurrentBitmapLeft, matrix, null);
            }
        } else {
            canvas.drawBitmap(BitmapCollections.getInstance().getCurrentBitmap(), 0, 0, null);

            if (frame == FRAME_COUNT - 1) {
                mLastBitmapLeft.recycle();
                mLastBitmapLeft = null;
                mLastBitmapRight.recycle();
                mLastBitmapRight = null;
                mCurrentBitmapLeft.recycle();
                mCurrentBitmapLeft = null;
                mCurrentBitmapRight.recycle();
                mCurrentBitmapRight = null;
            }
        }
    }

    @Override
    public int getFrameCount() {
        return FRAME_COUNT;
    }
}
