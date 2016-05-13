package org.howe.galleryanimations.effects;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * @author xh2009cn
 * @version 1.0.0
 */
public interface IEffect {
    public int getFrameCount();
    public void run(int frame, Canvas canvas, Matrix matrix, Paint paint);
}
