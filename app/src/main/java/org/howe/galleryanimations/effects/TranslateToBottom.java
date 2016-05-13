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
public class TranslateToBottom implements IEffect {

    private static final int FRAME_COUNT = 50;

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
            matrix.postTranslate(0, (2 * frame / (float) FRAME_COUNT) * Configs.getHeight());
            canvas.drawBitmap(BitmapCollections.getInstance().getLastPhotoBitmap(), matrix, null);

            matrix.reset();
            matrix.postTranslate(0, (2 * frame / (float) FRAME_COUNT - 1) * Configs.getHeight());
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
