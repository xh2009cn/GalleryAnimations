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
public class TransFromLT2RB implements IEffect {

    private static final int FRAME_COUNT = 75;

    @Override
    public void run(int frame, Canvas canvas, Matrix matrix, Paint paint) {
        if (frame < 0 || frame >= FRAME_COUNT) {
            return;
        }
        if (frame == 0) {
            BitmapCollections.getInstance().moveNextGallery();
        }
        if (frame < FRAME_COUNT / 2) {
            canvas.drawBitmap(BitmapCollections.getInstance().getLastPhotoBitmap(), 0, 0, null);

            matrix.reset();
            matrix.postTranslate((2 * frame / (float) FRAME_COUNT - 1) * Configs.getWidth(), (2 * frame / (float) FRAME_COUNT - 1) * Configs.getHeight());
            canvas.drawBitmap(BitmapCollections.getInstance().getCurrentBitmap(), matrix, null);
        } else {
            canvas.drawBitmap(BitmapCollections.getInstance().getCurrentBitmap(), 0, 0, null);
        }
    }

    @Override
    public int getFrameCount() {
        return FRAME_COUNT;
    }
}
